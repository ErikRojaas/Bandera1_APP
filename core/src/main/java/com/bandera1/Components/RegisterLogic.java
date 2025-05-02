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
import com.bandera1.Engine.GameObjects.Checkbox;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.WebSocketEventListener;

public class RegisterLogic extends Component implements WebSocketEventListener {

    private Collider collider;
    
    // Hardcoded GameObject names
    private static final String NICKNAME_OBJECT_NAME = "registerNickname";
    private static final String EMAIL_OBJECT_NAME = "registerEmail";
    private static final String PHONE_OBJECT_NAME = "registerPhone";
    private static final String PASSWORD_OBJECT_NAME = "registerPassword";
    private static final String PASSWORD_CONFIRMATION_OBJECT_NAME = "registerPasswordConfirmation";
    private static final String TERMS_CHECKBOX_NAME = "termsCheckbox";
    
    // Cache references for better performance
    private GameObject nicknameObj;
    private GameObject emailObj;
    private GameObject phoneObj;
    private GameObject passwordObj;
    private GameObject passwordConfirmationObj;
    private GameObject termsCheckboxObj;
    private Textfield nicknameField;
    private Textfield emailField;
    private Textfield phoneField;
    private Textfield passwordField;
    private Textfield passwordConfirmationField;
    private Checkbox termsCheckbox;

    public RegisterLogic() {
        // No parameters needed anymore
    }

    @Override
    public void start() {
        collider = gameObject.getComponent(Collider.class);
        if (collider == null) {
            throw new RuntimeException("RegisterLogic component requires a Collider component on the same GameObject");
        }
        
        // Find GameObjects and get components in start()
        nicknameObj = GameObject.Find(NICKNAME_OBJECT_NAME);
        emailObj = GameObject.Find(EMAIL_OBJECT_NAME);
        phoneObj = GameObject.Find(PHONE_OBJECT_NAME);
        passwordObj = GameObject.Find(PASSWORD_OBJECT_NAME);
        passwordConfirmationObj = GameObject.Find(PASSWORD_CONFIRMATION_OBJECT_NAME);
        termsCheckboxObj = GameObject.Find(TERMS_CHECKBOX_NAME);
        
        boolean allObjectsFound = nicknameObj != null && emailObj != null && phoneObj != null && 
                                  passwordObj != null && passwordConfirmationObj != null && 
                                  termsCheckboxObj != null;
        
        if (allObjectsFound) {
            nicknameField = nicknameObj.getComponent(Textfield.class);
            emailField = emailObj.getComponent(Textfield.class);
            phoneField = phoneObj.getComponent(Textfield.class);
            passwordField = passwordObj.getComponent(Textfield.class);
            passwordConfirmationField = passwordConfirmationObj.getComponent(Textfield.class);
            termsCheckbox = termsCheckboxObj.getComponent(Checkbox.class);
            
            boolean allComponentsFound = nicknameField != null && emailField != null && phoneField != null && 
                                        passwordField != null && passwordConfirmationField != null &&
                                        termsCheckbox != null;
            
            if (!allComponentsFound) {
                Gdx.app.error("RegisterLogic", "One or more components not found");
            }
        } else {
            Gdx.app.error("RegisterLogic", "One or more registration GameObjects not found");
        }
        
        // Register as a listener for server events
        if (ServerUtils.instance != null) {
            ServerUtils.instance.addListener(this);
        } else {
            Gdx.app.error("RegisterLogic", "ServerUtils instance is null");
        }
    }

    @Override
    public void update() {
        if (InputSystem.onTouchDown(0)) {
            float x = InputSystem.getTouchX();
            float y = InputSystem.getTouchY();

            if (collider.isInside(new Vector2(x, y))) {
                // Use the cached references from start()
                boolean allComponentsExist = nicknameField != null && emailField != null && phoneField != null && 
                                           passwordField != null && passwordConfirmationField != null &&
                                           termsCheckbox != null;
                
                if (allComponentsExist) {
                    String nickname = nicknameField.text;
                    String email = emailField.text;
                    String phone = phoneField.text;
                    String password = passwordField.text;
                    String passwordConfirmation = passwordConfirmationField.text;
                    boolean termsAccepted = termsCheckbox.isChecked();
                    
                    if (nickname.isEmpty() || email.isEmpty() || phone.isEmpty() || 
                        password.isEmpty() || passwordConfirmation.isEmpty()) {
                        Gdx.app.log("RegisterLogic", "All fields must be filled");
                        return;
                    }
                    
                    if (!password.equals(passwordConfirmation)) {
                        Gdx.app.log("RegisterLogic", "Passwords do not match");
                        return;
                    }
                    
                    if (!termsAccepted) {
                        Gdx.app.log("RegisterLogic", "You must accept the Terms of Service");
                        return;
                    }
                    
                    // Create registration data
                    JsonValue registerData = new JsonValue(JsonValue.ValueType.object);
                    registerData.addChild("nickname", new JsonValue(nickname));
                    registerData.addChild("email", new JsonValue(email));
                    registerData.addChild("phone", new JsonValue(phone));
                    registerData.addChild("password", new JsonValue(password));
                    registerData.addChild("termsAccepted", new JsonValue(termsAccepted));
                    
                    // Send registration message to server
                    ServerMessage registerMessage = new ServerMessage("register", registerData);
                    if (ServerUtils.instance != null && ServerUtils.instance.isConnected()) {
                        ServerUtils.instance.send(registerMessage);
                        Gdx.app.log("RegisterLogic", "Sending registration request: " + registerMessage.toString());
                    } else {
                        Gdx.app.error("RegisterLogic", "ServerUtils instance is null or not connected");
                        // If not connected, go directly to Room scene for testing
                        SceneSystem.changeScene("Room");
                    }
                } else {
                    Gdx.app.error("RegisterLogic", "One or more registration field components not available");
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
        Gdx.app.log("RegisterLogic", "Connected to server");
    }

    @Override
    public void onDisconnect(int closeCode, String reason) {
        Gdx.app.log("RegisterLogic", "Disconnected from server: " + reason);
    }

    @Override
    public void onMessage(ServerMessage message) {
        if (message.type.equals("register")) {
            boolean success = message.data.getBoolean("success", false);
            if (success) {
                Gdx.app.log("RegisterLogic", "Registration successful");
                // Change to Room scene on successful registration
                SceneSystem.changeScene("Room");
            } else {
                String errorMessage = message.data.getString("message", "Unknown error");
                Gdx.app.log("RegisterLogic", "Registration failed: " + errorMessage);
            }
        }
    }

    @Override
    public void onBinaryMessage(byte[] data) {
        // Not handling binary messages
    }

    @Override
    public void onError(Throwable error) {
        Gdx.app.error("RegisterLogic", "WebSocket error: " + error.getMessage());
    }
} 