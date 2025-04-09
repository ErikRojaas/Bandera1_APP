package com.bandera1.Components;

import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.WebSocketEventListener;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Engine.GameObjects.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
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
        /*player = GameObject.Find("player");

        // Añadimos el PlayerRenderer al jugador local si no lo tiene ya
        if (player != null && player.getComponent(PlayerRenderer.class) == null) {
            PlayerRenderer renderer = new PlayerRenderer();
            player.addComponent(renderer);
            renderer.setAction(PlayerAnimator.Action.IDLE);
            renderer.setDirection(PlayerAnimator.Direction.DOWN);
            player.transform.scale.set(0.5f, 0.5f);
        }*/
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
                updatePlayerPosition(player, clientPlayerData);
            }

            // Actualizar a otros jugadores
            if (data.has("otherPlayers")) {
                JsonValue otherPlayersArray = data.get("otherPlayers");
                updateOtherPlayers(otherPlayersArray);
            }
        }
    }

    private void updatePlayerPosition(GameObject player, JsonValue playerData) {
        if (player != null && playerData != null) {
            float x = playerData.getFloat("x");
            float y = playerData.getFloat("y");
            player.transform.position = new Vector2(x, y);
        } else {
            Gdx.app.log("PlayerManager", "Player or playerData is null");
        }
    }

    private void updateOtherPlayers(JsonValue otherPlayersArray) {
        Set<String> currentPlayerIds = new HashSet<>();

        for (JsonValue playerData : otherPlayersArray) {
            String playerId = playerData.getString("id");
            currentPlayerIds.add(playerId);
            GameObject otherPlayer = otherPlayers.get(playerId);

            if (otherPlayer == null) {
                float x = playerData.getFloat("x");
                float y = playerData.getFloat("y");

                GameObject newPlayer = new GameObject("player " + playerId);
                //PlayerRenderer renderer = new PlayerRenderer();
                //newPlayer.addComponent(renderer);
                //renderer.setAction(PlayerAnimator.Action.IDLE);
                //renderer.setDirection(PlayerAnimator.Direction.DOWN);

                newPlayer.addComponent(new Player(playerId));
                newPlayer.transform.position = new Vector2(x, y);
                newPlayer.transform.scale.set(0.5f, 0.5f);
                SceneSystem.activeScene.addGameObject(newPlayer);
                otherPlayers.put(playerId, newPlayer);

                Gdx.app.log("PlayerManager", "Created new player: " + playerId);
            } else {
                // Actualizar posición del jugador existente
                updatePlayerPosition(otherPlayer, playerData);
            }
        }

        // Eliminar jugadores que se han desconectado
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
