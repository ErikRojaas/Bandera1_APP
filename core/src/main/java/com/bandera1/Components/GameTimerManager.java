package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;
import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Utils.*;

public class GameTimerManager extends Component implements WebSocketEventListener {
    
    TextRenderer timer;
    boolean active;

    @Override
    public void init() {
        ServerUtils.instance.addListener(this);
    }

    @Override
    public void start() {
        timer = GameObject.Find("gameTimer").getComponent(TextRenderer.class);
        active = true;
    }

    @Override
    public void onMessage(ServerMessage message) {
        if (!active) return;
        if (message.type.equals("update")) {
            JsonValue data = message.data;
            if (data.has("room")) {
                if (data.get("room").has("timer")) {
                    timer.text = data.get("room").getString("timer");
                }
                if (data.get("room").has("started")) {
                    if (!data.get("room").getBoolean("started")) {
                        active = false;
                        SceneSystem.changeScene("Room");
                    }
                }
            }
        }
    }

    @Override
    public void onConnect() {}
    @Override
    public void onDisconnect(int closeCode, String reason) {}
    @Override
    public void onBinaryMessage(byte[] data) {}
    @Override
    public void onError(Throwable error) {}
} 