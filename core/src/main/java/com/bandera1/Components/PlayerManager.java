package com.bandera1.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.bandera1.Engine.GameObjects.TextRenderer;
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
    private TextRenderer textRenderer;
    private PlayerAnimator.Direction lastDirection = PlayerAnimator.Direction.DOWN;
    public static boolean attackRequested = false;

    @Override
    public void init() {
        ServerUtils.instance.addListener(this);
        otherPlayers = new HashMap<>();
    }

    @Override
    public void start() {
        active = true;
        player = GameObject.Find("player");
        textRenderer = GameObject.Find("playerPointsText").getComponent(TextRenderer.class);
        GameObject attackButtonObj = new GameObject("attackButton");
        attackButtonObj.addComponent(new AttackButton());
        SceneSystem.activeScene.addGameObject(attackButtonObj);

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
                    createAnimationRenderer(player, clientPlayerData);
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
        if (player == null || playerData == null) return;

        float x = playerData.getFloat("x");
        float y = playerData.getFloat("y");
        JsonValue moveVector = playerData.get("moveVector");
        float dx = moveVector.getFloat("dx");
        float dy = moveVector.getFloat("dy");
        JsonValue speedVector = playerData.get("speedVector");
        float speedX = speedVector.getFloat("speedX");
        float speedY = speedVector.getFloat("speedY");
        String nickname = playerData.getString("nickname", isLocal ? "You" : "Player");
        int teamId = playerData.getInt("teamId", 0);
        int skinId = playerData.getInt("skinId", 1);
        boolean hasKey = playerData.getBoolean("hasKey", false);
        boolean hasFlag = playerData.getBoolean("hasFlag", false);

        PositionSync positionSync = player.getComponent(PositionSync.class);
        if (positionSync != null) {
            positionSync.targetPosition.set(x, y);
            positionSync.velocity.set(speedX, speedY);
            positionSync.snap = (speedX == 0 && speedY == 0);
        }

        Player playerComponent = player.getComponent(Player.class);
        if (playerComponent != null) {
            if (playerComponent.teamId != teamId) {
                playerComponent.teamId = teamId;
                ensureTextRenderers(player, nickname, teamId);
            }

            if (playerComponent.skinId != skinId || 
                playerComponent.hasKey != hasKey || 
                playerComponent.hasFlag != hasFlag) {
                playerComponent.skinId = skinId;
                playerComponent.hasKey = hasKey;
                playerComponent.hasFlag = hasFlag;
                updateAnimationRenderer(player, skinId, hasKey, hasFlag);
            }
        }

        if (isLocal && textRenderer != null && playerData.has("points")) {
            int newPoints = playerData.getInt("points");
            textRenderer.setText("Points: " + newPoints);
        }

        AnimationRenderer animationRenderer = player.getComponent(AnimationRenderer.class);
        if (animationRenderer != null) {
            if (dx == 0 && dy == 0) {
                animationRenderer.play("IDLE_" + lastDirection.name());
            } else {
                if (Math.abs(dx) > Math.abs(dy)) {
                    lastDirection = dx > 0 ? PlayerAnimator.Direction.RIGHT : PlayerAnimator.Direction.LEFT;
                } else {
                    lastDirection = dy > 0 ? PlayerAnimator.Direction.UP : PlayerAnimator.Direction.DOWN;
                }
                animationRenderer.play("WALK_" + lastDirection.name());
            }
        } 
    }

    private void updateOtherPlayers(JsonValue otherPlayersArray) {
        Set<String> currentPlayerIds = new HashSet<>();

        for (JsonValue playerData : otherPlayersArray) {
            String playerId = playerData.getString("id");
            currentPlayerIds.add(playerId);

            GameObject otherPlayer = otherPlayers.get(playerId);

            if (otherPlayer == null) {
                otherPlayer = createNewOtherPlayer(playerData);
                otherPlayers.put(playerId, otherPlayer);
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

    private GameObject createNewOtherPlayer(JsonValue playerData) {
        String playerId = playerData.getString("id");
        float x = playerData.getFloat("x");
        float y = playerData.getFloat("y");
        int skinId = playerData.getInt("skinId", 1);
        boolean hasKey = playerData.getBoolean("hasKey", false);
        boolean hasFlag = playerData.getBoolean("hasFlag", false);
        int teamId = playerData.getInt("teamId", 0);
        String nickname = playerData.getString("nickname", "Player");

        GameObject newPlayer = new GameObject("player " + playerId);

        PlayerAnimator animator = createAnimatorForSkin(skinId, hasKey, hasFlag);
        Animation<TextureRegion> idleDownAnim = animator.getAnimation(PlayerAnimator.Action.IDLE, PlayerAnimator.Direction.DOWN);
        TextureRegion initialFrame = idleDownAnim.getKeyFrame(0);
        AnimationRenderer animationRenderer = new AnimationRenderer(initialFrame);

        for (PlayerAnimator.Action action : PlayerAnimator.Action.values()) {
            for (PlayerAnimator.Direction direction : PlayerAnimator.Direction.values()) {
                animationRenderer.addAnimation(action.name() + "_" + direction.name(), animator.getAnimation(action, direction));
            }
        }
        animationRenderer.play("IDLE_DOWN");

        newPlayer.addComponent(new PositionSync());
        newPlayer.addComponent(animationRenderer);
        newPlayer.addComponent(new Player(playerId, skinId, hasKey, hasFlag, teamId));
        newPlayer.transform.position.set(x, y);
        newPlayer.transform.scale.set(4f, 4f);

        ensureTextRenderers(newPlayer, nickname, teamId);

        SceneSystem.activeScene.addGameObject(newPlayer);
        return newPlayer;
    }

    private void ensureTextRenderers(GameObject player, String nickname, int teamId) {
        TextRenderer teamText = player.getComponent(TextRenderer.class);
        if (teamText == null) {
            teamText = new TextRenderer(getTeamName(teamId));
            teamText.offsetY = 70f;
            teamText.fontScale = 0.3f;
            player.addComponent(teamText);
        } else {
            teamText.setText(getTeamName(teamId));
        }

        TextRenderer nicknameText = player.getComponent(TextRenderer.class);
        if (nicknameText == null) {
            nicknameText = new TextRenderer(nickname);
            nicknameText.offsetY = 40f;
            nicknameText.fontScale = 0.3f;
            player.addComponent(nicknameText);
        } else {
            nicknameText.setText(nickname);
        }
    }

    private PlayerAnimator createAnimatorForSkin(int skinId, boolean hasKey, boolean hasFlag) {
        String path = "Characters/Character" + skinId + "/";
        String walk, idle;
        if (hasFlag) {
            walk = "Char_Carry_Flag_Walk.png";
            idle = "Char_Carry_Flag_Idle.png";
        } else if (hasKey) {
            walk = "Char_Carry_Key_Walk.png";
            idle = "Char_Carry_Key_Idle.png";
        } else {
            walk = "Char_Walk.png";
            idle = "Char_Idle.png";
        }
        return new PlayerAnimator(path, walk, idle, "Char_Attack.png", "Char_Death.png");
    }

    private void updateAnimationRenderer(GameObject player, int skinId, boolean hasKey, boolean hasFlag) {
        AnimationRenderer animationRenderer = player.getComponent(AnimationRenderer.class);
        if (animationRenderer == null) return;

        PlayerAnimator animator = createAnimatorForSkin(skinId, hasKey, hasFlag);

        animationRenderer.clearAnimations();
        for (PlayerAnimator.Action action : PlayerAnimator.Action.values()) {
            for (PlayerAnimator.Direction direction : PlayerAnimator.Direction.values()) {
                animationRenderer.addAnimation(action.name() + "_" + direction.name(), animator.getAnimation(action, direction));
            }
        }
        animationRenderer.play("IDLE_" + lastDirection.name());
    }

    private void createAnimationRenderer(GameObject player, JsonValue playerData) {
        int skinId = playerData.getInt("skinId", 1);
        boolean hasKey = playerData.getBoolean("hasKey", false);
        boolean hasFlag = playerData.getBoolean("hasFlag", false);

        PlayerAnimator animator = createAnimatorForSkin(skinId, hasKey, hasFlag);
        Animation<TextureRegion> idleDownAnim = animator.getAnimation(PlayerAnimator.Action.IDLE, PlayerAnimator.Direction.DOWN);
        TextureRegion initialFrame = idleDownAnim.getKeyFrame(0);
        AnimationRenderer animationRenderer = new AnimationRenderer(initialFrame);

        for (PlayerAnimator.Action action : PlayerAnimator.Action.values()) {
            for (PlayerAnimator.Direction direction : PlayerAnimator.Direction.values()) {
                animationRenderer.addAnimation(action.name() + "_" + direction.name(), animator.getAnimation(action, direction));
            }
        }
        animationRenderer.play("IDLE_DOWN");

        player.addComponent(animationRenderer);
        player.addComponent(new Player(player.getName(), skinId, hasKey, hasFlag, 0));
    }

    private String getTeamName(int teamId) {
        switch (teamId) {
            case 0: return "Lornwood";
            case 1: return "Vileswamp";
            case 2: return "Asharid";
            case 3: return "Ironhold";
            default: return "Unknown";
        }
    }

    public static void requestAttack() {
        attackRequested = true;
    }

    @Override
    public void onBinaryMessage(byte[] data) {}

    @Override
    public void onError(Throwable error) {
        Gdx.app.log("PlayerManager", "WebSocket error in PlayerManager: " + error.getMessage());
    }
}
