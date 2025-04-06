package com.bandera1.Components;

import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.WebSocketEventListener;
import com.bandera1.Engine.GameObjects.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.bandera1.Engine.GameObjects.Component;

public class PlayerManager extends Component implements WebSocketEventListener {
    private GameObject player;
    private Map<String, GameObject> otherPlayers;
    private GameObject playerPrefab;

    @Override
    public void init() {
        ServerUtils.instance.addListener(this);
        otherPlayers = new HashMap<>();
        playerPrefab = new GameObject("player");
        playerPrefab.addComponent(new Player());
        //not adding moving component
    }

    @Override
    public void start() {
        player = GameObject.Find("player");
    }

    @Override
    public void onConnect() {}

    @Override
    public void onDisconnect(int closeCode, String reason) {
        // Clean up all other players when disconnecting
        for (GameObject otherPlayer : otherPlayers.values()) {
            GameObject.Destroy(otherPlayer);
        }
        otherPlayers.clear();
    }

    @Override
    public void onMessage(ServerMessage message) {
        if (message.type.equals("update")) {
            JsonValue data = message.data;
            // Update client player position
            if (data.has("clientPlayer")) {
                JsonValue clientPlayerData = data.get("clientPlayer");
                updatePlayerPosition(player, clientPlayerData);
            }

            // Update other players
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
        }
    }

    private void updateOtherPlayers(JsonValue otherPlayersArray) {
        // Create a set of current player IDs
        Set<String> currentPlayerIds = new HashSet<>();
        
        // Update or create other players
        for (JsonValue playerData : otherPlayersArray) {
            String playerId = playerData.getString("id");
            currentPlayerIds.add(playerId);
            
            GameObject otherPlayer = otherPlayers.get(playerId);
            if (otherPlayer == null) {
                // Create new player using instantiate
                float x = playerData.getFloat("x");
                float y = playerData.getFloat("y");
                otherPlayer = GameObject.instantiate(playerPrefab, new Vector2(x, y));
                if (otherPlayer != null) {
                    otherPlayers.put(playerId, otherPlayer);
                }
            } else {
                // Update position of existing player
                updatePlayerPosition(otherPlayer, playerData);
            }
        }
        
        // Remove disconnected players
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