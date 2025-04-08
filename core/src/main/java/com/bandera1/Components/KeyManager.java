package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.WebSocketEventListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.bandera1.Engine.Systems.SceneSystem;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class KeyManager extends Component implements WebSocketEventListener {

    Map<String, GameObject> keys;
    boolean active;
    Texture keyTexture;

    @Override
    public void init() {
        keys = new HashMap<>();
        ServerUtils.instance.addListener(this);
        keyTexture = new Texture("key.png");
    }

    @Override
    public void start() {
        active = true;
    }

    @Override
    public void onConnect() {}

    @Override
    public void onDisconnect(int closeCode, String reason) {}

    @Override
    public void onMessage(ServerMessage message) {
        if (!active) return;

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
                GameObject key = new GameObject("key " + keyId);
                key.addComponent(new TextureRenderer(keyTexture));
                key.addComponent(new Key());
                key.transform.position = new Vector2(keyData.getFloat("x"), keyData.getFloat("y"));
                key.transform.scale.set(0.5f,0.5f);
                SceneSystem.activeScene.addGameObject(key);
                Gdx.app.log("KeyManager", "Creating new key");
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
