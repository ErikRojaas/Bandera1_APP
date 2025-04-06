package com.bandera1.Utils;

import com.badlogic.gdx.Gdx;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class ServerUtils implements WebSocketListener {
    public static ServerUtils instance;
    private WebSocket socket;
    private boolean connected = false;
    private List<WebSocketEventListener> listeners = new ArrayList<>();

    public ServerUtils(String url) {
        socket = WebSockets.newSocket(url);
        socket.addListener(this);
        instance = this;
    }

    /**
     * Adds a new WebSocketEventListener to receive WebSocket events.
     * If the listener is already registered, it won't be added again.
     * 
     * @param listener The listener to add
     */
    public void addListener(WebSocketEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a WebSocketEventListener from the registered listeners.
     * 
     * @param listener The listener to remove
     */
    public void removeListener(WebSocketEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Removes all registered WebSocketEventListeners.
     */
    public void clearListeners() {
        listeners.clear();
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

    public void send(ServerMessage message) {
        if (isConnected()) {
            socket.send(message.toString());
        }
    }

    public boolean isConnected() {
        return connected && socket != null && socket.isOpen();
    }

    @Override
    public boolean onOpen(WebSocket webSocket) {
        System.out.println("Opening...");
        connected = true;
        for (WebSocketEventListener listener : listeners) {
            listener.onConnect();
        }
        return false;
    }

    @Override
    public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
        System.out.println("Closing...");
        connected = false;
        for (WebSocketEventListener listener : listeners) {
            listener.onDisconnect(closeCode, reason);
        }
        return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, String packet) {
        System.out.println("Message:" + packet);

        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(packet);
    
        ServerMessage message = new ServerMessage();
        message.type = root.getString("type");
        message.data = root.get("data");

        for (WebSocketEventListener listener : listeners) {            
            listener.onMessage(message);
        }
        return false;
    }

    @Override
    public boolean onMessage(WebSocket webSocket, byte[] packet) {
        System.out.println("Message:" + packet);
        for (WebSocketEventListener listener : listeners) {
            listener.onBinaryMessage(packet);
        }
        return false;
    }

    @Override
    public boolean onError(WebSocket webSocket, Throwable error) {
        System.out.println("ERROR:" + error.toString());
        for (WebSocketEventListener listener : listeners) {
            listener.onError(error);
        }
        return false;
    }
}

