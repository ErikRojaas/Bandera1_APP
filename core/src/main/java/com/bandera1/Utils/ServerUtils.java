package com.bandera1.Utils;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;

public class ServerUtils implements WebSocketListener {
    public static ServerUtils instance;
    private WebSocket socket;
    private boolean connected = false;
    private String lastResponse = null;

    public ServerUtils(String url) {
        socket = WebSockets.newSocket(url);
        socket.addListener(this);
        instance = this;
    }

    public void connect() {
        if (!connected) {
            Gdx.app.log("ServerUtils", "Connecting...");
            socket.connect();
            Gdx.app.log("ServerUtils", "Connected");
        }
    }

    public void disconnect() {
        if (socket != null && socket.isOpen()) {
            socket.close();
        }
        connected = false;
    }

    public void send(String message) {
        if (isConnected()) {
            socket.send(message);
        }
    }

    public boolean isConnected() {
        return connected && socket != null && socket.isOpen();
    }

    public boolean hasNewResponse() {
        return lastResponse != null;
    }

    public String getLatestResponse() {
        String response = lastResponse;
        lastResponse = null;
        return response;
    }

    @Override
    public boolean onOpen(WebSocket webSocket) {
        System.out.println("Opening...");
        connected = true;
        return false;
    }

    @Override
    public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
        System.out.println("Closing...");
        connected = false;
        return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, String packet) {
        System.out.println("Message:" + packet);
        lastResponse = packet;
        return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, byte[] packet) {
        System.out.println("Message:" + packet);
        return false;
    }

    @Override
    public boolean onError(WebSocket webSocket, Throwable error) {
        System.out.println("ERROR:" + error.toString());
        return false;
    }
}
