package com.bandera1.Utils;
/**
 * Interface for listening to WebSocket events in the application.
 * Implement this interface to receive WebSocket events from ServerUtils.
 */
public interface WebSocketEventListener {
    /**
     * Called when the WebSocket connection is established.
     */
    void onConnect();

    /**
     * Called when the WebSocket connection is closed.
     *
     * @param closeCode The close code
     * @param reason The reason for closing
     */
    void onDisconnect(int closeCode, String reason);

    /**
     * Called when a text message is received.
     *
     * @param message The received message
     */
    void onMessage(ServerMessage message);

    /**
     * Called when a binary message is received.
     *
     * @param data The received binary data
     */
    void onBinaryMessage(byte[] data);

    /**
     * Called when an error occurs.
     *
     * @param error The error that occurred
     */
    void onError(Throwable error);
}