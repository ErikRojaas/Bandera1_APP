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
    private Map<String, Animation<TextureRegion>> animations = new HashMap<>();

    @Override
    public void init() {
        ServerUtils.instance.addListener(this);
        otherPlayers = new HashMap<>();
    }

    @Override
    public void start() {
        active = true;
        player = GameObject.Find("player");
        PlayerAnimator animator = new PlayerAnimator();
        for (PlayerAnimator.Action action : PlayerAnimator.Action.values()) {
            for (PlayerAnimator.Direction direction : PlayerAnimator.Direction.values()) {
                animations.put(action.name() + "_" + direction.name(), animator.getAnimation(action, direction));
            }
        }
    }

    @Override
    public void onConnect() {}

    @Override
    public void onDisconnect(int closeCode, String reason) {
        // Limpiar todos los otros jugadores al desconectarse
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

            // Actualizar al jugador local
            if (data.has("clientPlayer")) {
                JsonValue clientPlayerData = data.get("clientPlayer");
                updatePlayer(player, clientPlayerData, true);
            }

            // Actualizar a otros jugadores
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
            //Update position
            PositionSync positionSync = player.getComponent(PositionSync.class);
            positionSync.targetPosition.set(x, y);
            positionSync.velocity.set(dx, dy);
            if (dx == 0 && dy == 0) {
                positionSync.snap = true;
            } else {
                positionSync.snap = false;
            }
            if (isLocal) return;
            //Update animation
            AnimationRenderer animationRenderer = (AnimationRenderer) player.getComponent(AnimationRenderer.class);
            if (dx == 0 && dy == 0) {
                String playedAnimation = animationRenderer.currentAnimationName;
                //change WALK_DIR to IDLE_DIR
                if (playedAnimation.contains("WALK")) {
                    animationRenderer.play("IDLE_" + playedAnimation.substring(5));
                }
            } else if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    animationRenderer.play("IDLE_RIGHT");
                } else {
                    animationRenderer.play("IDLE_LEFT");
                }
            } else {
                if (dy > 0) {
                    animationRenderer.play("IDLE_DOWN");
                } else {
                    animationRenderer.play("IDLE_UP");
                }
            }
        } else {
            Gdx.app.log("PlayerManager", "Player or playerData is null");
        }
    }
    private void updateOtherPlayers(JsonValue otherPlayersArray) {
        Set<String> currentPlayerIds = new HashSet<>();
    
        for (JsonValue playerData : otherPlayersArray) {
            String playerId = playerData.getString("id");
            int skinId = playerData.has("skinId") ? playerData.getInt("skinId") : 1; // por defecto Character1
            currentPlayerIds.add(playerId);
            GameObject otherPlayer = otherPlayers.get(playerId);
    
            if (otherPlayer == null) {
                float x = playerData.getFloat("x");
                float y = playerData.getFloat("y");
    
                // Crear animador personalizado con la skin
                PlayerAnimator animator = new PlayerAnimator("Characters/Character" + skinId + "/");
    
                // Frame inicial
                Animation<TextureRegion> initialAnimation = animator.getAnimation(PlayerAnimator.Action.IDLE, PlayerAnimator.Direction.DOWN);
                TextureRegion initialFrame = initialAnimation.getKeyFrame(0);
                AnimationRenderer animationRenderer = new AnimationRenderer(initialFrame);
    
                // Agregar todas las animaciones
                for (PlayerAnimator.Action action : PlayerAnimator.Action.values()) {
                    for (PlayerAnimator.Direction direction : PlayerAnimator.Direction.values()) {
                        Animation<TextureRegion> anim = animator.getAnimation(action, direction);
                        animationRenderer.addAnimation(action.name() + "_" + direction.name(), anim);
                    }
                }
    
                // Crear GameObject del jugador
                GameObject newPlayer = new GameObject("player " + playerId);
                newPlayer.addComponent(new Player(playerId));
                newPlayer.addComponent(animationRenderer);
                newPlayer.addComponent(new PositionSync());
                newPlayer.transform.position.set(x, y);
                newPlayer.transform.scale.set(4f, 4f);
    
                // Inicializar animación correcta según dirección
                JsonValue moveVector = playerData.get("moveVector");
                float dx = moveVector.getFloat("dx");
                float dy = moveVector.getFloat("dy");
    
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0) {
                        animationRenderer.play("IDLE_RIGHT");
                    } else {
                        animationRenderer.play("IDLE_LEFT");
                    }
                } else {
                    if (dy > 0) {
                        animationRenderer.play("IDLE_DOWN");
                    } else {
                        animationRenderer.play("IDLE_UP");
                    }
                }
    
                SceneSystem.activeScene.addGameObject(newPlayer);
                otherPlayers.put(playerId, newPlayer);
                Gdx.app.log("PlayerManager", "Created new player: " + playerId);
            } else {
                updatePlayer(otherPlayer, playerData, false);
            }
        }
    
        // Eliminar jugadores desconectados
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
