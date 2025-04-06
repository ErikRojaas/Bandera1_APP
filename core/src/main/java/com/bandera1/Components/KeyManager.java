package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.WebSocketEventListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.graphics.Texture;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class KeyManager extends Component implements WebSocketEventListener {

    Map<String, GameObject> keys;
    GameObject keyPrefab;

    @Override
    public void start() {
        keyPrefab = new GameObject("key");
        keyPrefab.addComponent(new TextureRenderer(new Texture("key.png")));
    }

    @Override
    public void onConnect() {}

    @Override
    public void onDisconnect(int closeCode, String reason) {}

    @Override
    public void onMessage(ServerMessage message) {
        if (message.type.equals("update")) {
            if (message.data.has("keys")) {
                JsonValue keys = message.data.get("keys");
                updateKeys(keys);
            }
        }
    }

    private void updateKeys(JsonValue data) {
        Set<String> currentKeys = new HashSet<>();
        for (JsonValue keyData : data) {
            String keyId = keyData.getString("id");
            currentKeys.add(keyId);
            if (keys.containsKey(keyId)) {
                GameObject key = keys.get(keyId);
                key.transform.position = new Vector2(keyData.getFloat("x"), keyData.getFloat("y"));
            } else {
                GameObject key = GameObject.instantiate(keyPrefab, new Vector2(keyData.getFloat("x"), keyData.getFloat("y")));
                keys.put(keyId, key);
            }
        }
        keys.entrySet().removeIf(entry -> {
            if (!currentKeys.contains(entry.getKey())) {
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
        System.err.println("KeyManager WebSocket error: " + error.getMessage());
    }
}
