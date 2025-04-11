package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.AnimationRenderer;
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
    KeyAnimator animator;

    @Override
    public void init() {
        keys = new HashMap<>();
        ServerUtils.instance.addListener(this);
        animator = new KeyAnimator();
        //keyTexture = new Texture("key.png");
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
                Gdx.app.log("KeyManager", "Recibido JSON de llaves: " + keys.toString());
                updateKeys(keys);
            } else {
                Gdx.app.log("KeyManager", "No se encontraron llaves en el mensaje.");
            }
        }
    }


    private void updateKeys(JsonValue data) {
        Set<String> currentKeys = new HashSet<>();
        for (JsonValue keyData : data) {
            String keyId = keyData.getString("id");
            currentKeys.add(keyId);
            float x = keyData.getFloat("x");
            float y = keyData.getFloat("y");

            if (keys.containsKey(keyId)) {
                GameObject key = keys.get(keyId);
                key.transform.position = new Vector2(x, y);
            } else {
                Gdx.app.log("KeyManager", "Creando nueva llave con ID: " + keyId + " en (" + x + ", " + y + ")");
                GameObject key = new GameObject("key_" + keyId);
                KeyAnimator animator = new KeyAnimator();
                AnimationRenderer renderer = new AnimationRenderer(animator.getAnimation().getKeyFrame(0));
                renderer.addAnimation("idle", animator.getAnimation());
                renderer.play("idle");

                key.addComponent(renderer);
                key.addComponent(new Key());
                key.transform.position = new Vector2(x, y);
                key.transform.scale.set(2f, 2f);
                SceneSystem.activeScene.addGameObject(key);
                keys.put(keyId, key);
            }
        }

        keys.entrySet().removeIf(entry -> {
            if (!currentKeys.contains(entry.getKey())) {
                Gdx.app.log("KeyManager", "Eliminando llave con ID: " + entry.getKey());
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
