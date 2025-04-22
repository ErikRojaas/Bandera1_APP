package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;
import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.WebSocketEventListener;

public class LastWinnerManager extends Component implements WebSocketEventListener {

    TextRenderer lastWonText;
    boolean active;

    @Override
    public void init() {
        ServerUtils.instance.addListener(this);
    }

    @Override
    public void start() {
        lastWonText = GameObject.Find("lastWonText").getComponent(TextRenderer.class);
        active = true;
    }

    @Override
    public void onMessage(ServerMessage message) {
        if (!active) return;
        Gdx.app.log("LastWinnerManager", "Data: " + message.data);
        if (message.type.equals("update")) {
            JsonValue data = message.data;
            if (data.has("room")) {
                if (data.get("room").has("winner")) {
                    String winner = data.get("room").getString("winner");
                    Gdx.app.log("LastWinnerManager", "winner: " + winner);
                    if (winner != null && !winner.isEmpty()) {
                        lastWonText.text = "Last Winner: " + winner;
                    } else {
                        lastWonText.text = "No game has been played yet";
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