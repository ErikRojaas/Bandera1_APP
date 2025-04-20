package com.bandera1.Components;

import org.w3c.dom.Text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;
import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Utils.*;

public class TimerManager extends Component implements WebSocketEventListener {

    TextRenderer timer;
    boolean active;

    @Override
    public void init() {
        ServerUtils.instance.addListener(this);
    }

    @Override
    public void start() {
        timer = GameObject.Find("Timer").getComponent(TextRenderer.class);
        active = true;
    }

    @Override
    public void onMessage(ServerMessage message) {
        if (!active) return;
        Gdx.app.log("TimerManager", "Data: " + message.data);
        if (message.type.equals("update")) {
            JsonValue data = message.data;
            if (data.has("room")) {
                if (data.get("room").has("timer")) {
                    timer.text = data.get("room").getString("timer");
                }
                if (data.get("room").has("justStarted")) {
                    if (data.get("room").getBoolean("justStarted")) {
                        active = false;
                        SceneSystem.changeScene("Game");
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
