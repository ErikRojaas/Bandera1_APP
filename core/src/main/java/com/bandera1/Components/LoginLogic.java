package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Json;

import com.bandera1.Engine.Systems.InputSystem;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.Collider;
import com.bandera1.Engine.GameObjects.Textfield;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.WebSocketEventListener;

public class LoginLogic extends Component implements WebSocketEventListener {

    private Collider collider;
    // Hardcoded GameObject names
    private static final String EMAIL_OBJECT_NAME = "loginEmail";
    private static final String PASSWORD_OBJECT_NAME = "loginPassword";
    
    private GameObject emailObj;
    private GameObject passwordObj;
    private Textfield emailField;
    private Textfield passwordField;

    public LoginLogic() {
        // No parameters needed anymore
    }

    @Override
    public void start() {
        collider = gameObject.getComponent(Collider.class);
        if (collider == null) {
            throw new RuntimeException("LoginLogic component requires a Collider component on the same GameObject");
        }
        
        // Find GameObjects and get components in start()
        emailObj = GameObject.Find(EMAIL_OBJECT_NAME);
        passwordObj = GameObject.Find(PASSWORD_OBJECT_NAME);
        
        if (emailObj != null && passwordObj != null) {
            emailField = emailObj.getComponent(Textfield.class);
            passwordField = passwordObj.getComponent(Textfield.class);
            
            if (emailField == null || passwordField == null) {
                Gdx.app.error("LoginLogic", "Email or password field component not found");
            }
        } else {
            Gdx.app.error("LoginLogic", "Email or password game object not found");
        }
        
        // Register as a listener for server events
        if (ServerUtils.instance != null) {
            ServerUtils.instance.addListener(this);
        } else {
            Gdx.app.error("LoginLogic", "ServerUtils instance is null");
        }
    }

    @Override
    public void update() {
        if (InputSystem.onTouchDown(0)) {
            float x = InputSystem.getTouchX();
            float y = InputSystem.getTouchY();

            if (collider.isInside(new Vector2(x, y))) {
                // Use the cached references from start()
                if (emailField != null && passwordField != null) {
                    String email = emailField.text;
                    String password = passwordField.text;
                    
                    if (email.isEmpty() || password.isEmpty()) {
                        Gdx.app.log("LoginLogic", "Email or password is empty");
                        return;
                    }
                    
                    // Create login data
                    Json json = new Json();
                    JsonValue loginData = new JsonValue(JsonValue.ValueType.object);
                    loginData.addChild("email", new JsonValue(email));
                    loginData.addChild("password", new JsonValue(password));
                    
                    // Send login message to server
                    ServerMessage loginMessage = new ServerMessage("login", loginData);
                    if (ServerUtils.instance != null && ServerUtils.instance.isConnected()) {
                        ServerUtils.instance.send(loginMessage);
                        Gdx.app.log("LoginLogic", "Sending login request: " + loginMessage.toString());
                    } else {
                        Gdx.app.error("LoginLogic", "ServerUtils instance is null or not connected");
                        // If not connected, go directly to Room scene for testing
                        SceneSystem.changeScene("Room");
                    }
                } else {
                    Gdx.app.error("LoginLogic", "Email or password field component not available");
                }
            }
        }
    }

    @Override
    public void dispose() {
        // Unregister as a listener
        if (ServerUtils.instance != null) {
            ServerUtils.instance.removeListener(this);
        }
    }

    // WebSocketEventListener implementation
    @Override
    public void onConnect() {
        Gdx.app.log("LoginLogic", "Connected to server");
    }

    @Override
    public void onDisconnect(int closeCode, String reason) {
        Gdx.app.log("LoginLogic", "Disconnected from server: " + reason);
    }

    @Override
    public void onMessage(ServerMessage message) {
        if (message.type.equals("login_result")) {
            boolean success = message.data.getBoolean("success", false);
            if (success) {
                Gdx.app.log("LoginLogic", "Login successful");
                // Change to Room scene on successful login
                SceneSystem.changeScene("Room");
            } else {
                String errorMessage = message.data.getString("message", "Unknown error");
                Gdx.app.log("LoginLogic", "Login failed: " + errorMessage);
            }
        }
    }

    @Override
    public void onBinaryMessage(byte[] data) {
        // Not handling binary messages
    }

    @Override
    public void onError(Throwable error) {
        Gdx.app.error("LoginLogic", "WebSocket error: " + error.getMessage());
    }
} 