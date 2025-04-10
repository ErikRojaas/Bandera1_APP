package com.bandera1.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.WebSocketEventListener;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Engine.GameObjects.AnimationRenderer;
import com.bandera1.Engine.GameObjects.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class PlayerManager extends Component implements WebSocketEventListener {
    private GameObject player;
    private Map<String, GameObject> otherPlayers;
    private boolean active;

    @Override
    public void init() {
        ServerUtils.instance.addListener(this);
        otherPlayers = new HashMap<>();
    }

    @Override
    public void start() {
        active = true;
        player = GameObject.Find("player");
    }

    @Override
    public void onConnect() {}

    @Override
    public void onDisconnect(int closeCode, String reason) {
        for (GameObject otherPlayer : otherPlayers.values()) {
            GameObject.Destroy(otherPlayer);
        }
        otherPlayers.clear();
    }

    @Override
    public void onMessage(ServerMessage message) {
        if (!active) return;

        if (message.type.equals("update")) {
            JsonValue data = message.data;

            if (data.has("clientPlayer")) {
                JsonValue clientPlayerData = data.get("clientPlayer");

                if (player.getComponent(AnimationRenderer.class) == null) {
                    int skinId = clientPlayerData.has("skinId") ? clientPlayerData.getInt("skinId") : 1;
                    PlayerAnimator animator = new PlayerAnimator("Characters/Character" + skinId + "/");
                    Gdx.app.log("PlayerManager", "Local player skinId = " + skinId);
                    Animation<TextureRegion> idleDownAnim = animator.getAnimation(PlayerAnimator.Action.IDLE, PlayerAnimator.Direction.DOWN);
                    TextureRegion initialFrame = idleDownAnim.getKeyFrame(0);
                    AnimationRenderer animationRenderer = new AnimationRenderer(initialFrame);                    for (PlayerAnimator.Action action : PlayerAnimator.Action.values()) {
                        for (PlayerAnimator.Direction direction : PlayerAnimator.Direction.values()) {
                            animationRenderer.addAnimation(action.name() + "_" + direction.name(), animator.getAnimation(action, direction));
                        }
                    }
                    animationRenderer.play("IDLE_DOWN");
                    player.addComponent(animationRenderer);
                }

                updatePlayer(player, clientPlayerData, true);
            }

            if (data.has("otherPlayers")) {
                JsonValue otherPlayersArray = data.get("otherPlayers");
                updateOtherPlayers(otherPlayersArray);
            }
        }
    }

    private void updatePlayer(GameObject player, JsonValue playerData, boolean isLocal) {
        if (player != null && playerData != null) {
            float x = playerData.getFloat("x");
            float y = playerData.getFloat("y");
            JsonValue moveVector = playerData.get("moveVector");
            float dx = moveVector.getFloat("dx");
            float dy = moveVector.getFloat("dy");

            PositionSync positionSync = player.getComponent(PositionSync.class);
            positionSync.targetPosition.set(x, y);
            positionSync.velocity.set(dx, dy);
            positionSync.snap = (dx == 0 && dy == 0);

            if (isLocal) return;

            AnimationRenderer animationRenderer = player.getComponent(AnimationRenderer.class);
            if (dx == 0 && dy == 0) {
                String playedAnimation = animationRenderer.currentAnimationName;
                if (playedAnimation.contains("WALK")) {
                    animationRenderer.play("IDLE_" + playedAnimation.substring(5));
                }
            } else if (Math.abs(dx) > Math.abs(dy)) {
                animationRenderer.play(dx > 0 ? "IDLE_RIGHT" : "IDLE_LEFT");
            } else {
                animationRenderer.play(dy > 0 ? "IDLE_DOWN" : "IDLE_UP");
            }
        } else {
            Gdx.app.log("PlayerManager", "Player or playerData is null");
        }
    }

    private void updateOtherPlayers(JsonValue otherPlayersArray) {
        Set<String> currentPlayerIds = new HashSet<>();

        for (JsonValue playerData : otherPlayersArray) {
            String playerId = playerData.getString("id");
            int skinId = playerData.has("skinId") ? playerData.getInt("skinId") : 1;
            currentPlayerIds.add(playerId);
            GameObject otherPlayer = otherPlayers.get(playerId);

            if (otherPlayer == null) {
                float x = playerData.getFloat("x");
                float y = playerData.getFloat("y");

                PlayerAnimator animator = new PlayerAnimator("Characters/Character" + skinId + "/");
                Animation<TextureRegion> initialAnimation = animator.getAnimation(PlayerAnimator.Action.IDLE, PlayerAnimator.Direction.DOWN);
                TextureRegion initialFrame = initialAnimation.getKeyFrame(0);
                AnimationRenderer animationRenderer = new AnimationRenderer(initialFrame);

                for (PlayerAnimator.Action action : PlayerAnimator.Action.values()) {
                    for (PlayerAnimator.Direction direction : PlayerAnimator.Direction.values()) {
                        animationRenderer.addAnimation(action.name() + "_" + direction.name(), animator.getAnimation(action, direction));
                    }
                }

                GameObject newPlayer = new GameObject("player " + playerId);
                newPlayer.addComponent(new Player(playerId));
                newPlayer.addComponent(animationRenderer);
                newPlayer.addComponent(new PositionSync());
                newPlayer.transform.position.set(x, y);
                newPlayer.transform.scale.set(4f, 4f);

                JsonValue moveVector = playerData.get("moveVector");
                float dx = moveVector.getFloat("dx");
                float dy = moveVector.getFloat("dy");

                if (Math.abs(dx) > Math.abs(dy)) {
                    animationRenderer.play(dx > 0 ? "IDLE_RIGHT" : "IDLE_LEFT");
                } else {
                    animationRenderer.play(dy > 0 ? "IDLE_DOWN" : "IDLE_UP");
                }

                SceneSystem.activeScene.addGameObject(newPlayer);
                otherPlayers.put(playerId, newPlayer);
                Gdx.app.log("PlayerManager", "Created new player: " + playerId);
            } else {
                updatePlayer(otherPlayer, playerData, false);
            }
        }

        otherPlayers.entrySet().removeIf(entry -> {
            if (!currentPlayerIds.contains(entry.getKey())) {
                GameObject.Destroy(entry.getValue());
                return true;
            }
            return false;
        });
    }

    @Override
    public void onBinaryMessage(byte[] data) {}

    @Override
    public void onError(Throwable error) {
        Gdx.app.log("PlayerManager", "WebSocket error in PlayerManager: " + error.getMessage());
    }
}
