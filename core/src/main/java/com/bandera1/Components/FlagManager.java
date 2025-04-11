package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.AnimationRenderer;
import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.WebSocketEventListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FlagManager extends Component implements WebSocketEventListener {

    private Map<String, GameObject> flags;
    private boolean active;
    FlagAnimator animator;

    @Override
    public void init() {
        flags = new HashMap<>();
        ServerUtils.instance.addListener(this);
        animator = new FlagAnimator();
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
        Gdx.app.log("Bandera",message.toString());
        if (message.type.equals("update") && message.data.has("flags")) {
            updateFlags(message.data.get("flags"));
        }
    }

    private void updateFlags(JsonValue data) {
        Set<String> currentFlags = new HashSet<>();

        for (JsonValue flagData : data) {
            String flagId = flagData.getString("id");
            currentFlags.add(flagId);

            if (flags.containsKey(flagId)) {
                GameObject flag = flags.get(flagId);
                flag.transform.position = new Vector2(flagData.getFloat("x"), flagData.getFloat("y"));
            } else {
                GameObject flag = new GameObject("flag " + flagId);

                AnimationRenderer renderer = new AnimationRenderer();
                renderer.addAnimation("idle", animator.getAnimation());
                renderer.play("idle");

                flag.addComponent(renderer);
                flag.addComponent(new Flag()); 
                flag.transform.position = new Vector2(flagData.getFloat("x"), flagData.getFloat("y"));
                flag.transform.scale.set(2, 2f);

                SceneSystem.activeScene.addGameObject(flag);
                Gdx.app.log("FlagManager", "Created flag: " + flagId);
                flags.put(flagId, flag);
            }
        }

        // Eliminar banderas que ya no están
        flags.entrySet().removeIf(entry -> {
            if (!currentFlags.contains(entry.getKey())) {
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
        Gdx.app.error("FlagManager", "WebSocket error: " + error.getMessage());
    }
}
