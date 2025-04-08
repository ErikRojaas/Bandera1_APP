package com.bandera1.Components;

import org.w3c.dom.Text;

import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.WebSocketEventListener;

public class PlayerCountText extends Component implements WebSocketEventListener {

    TextRenderer textRenderer;
    int count;

    @Override
    public void init() {
        ServerUtils.instance.addListener(this);
    }

    @Override
    public void start() {
        textRenderer = gameObject.getComponent(TextRenderer.class);
        count = 0;
    }

    @Override
    public void update() {
        textRenderer.text = "Players: " + count;
    }

    @Override
    public void onMessage(ServerMessage message) {
        if (message.type.equals("playerCount")) {
            count = message.data.asInt();
        } else if (message.type.equals("update")) {
            count = message.data.get("otherPlayers").size + 1;
        }
    }

    @Override public void onConnect() {}
    @Override public void onDisconnect(int closeCode, String reason) {}
    @Override public void onBinaryMessage(byte[] data) {}
    @Override public void onError(Throwable error) {}

}
