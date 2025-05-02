package com.bandera1.Scenes;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bandera1.Components.ObjectHideShowButton;
import com.bandera1.Components.SceneChangingButton;
import com.bandera1.Components.LoginLogic;
import com.bandera1.Components.RegisterLogic;
import com.bandera1.Engine.GameObjects.AnimationRenderer;
import com.bandera1.Engine.GameObjects.ClickableUrlRenderer;
import com.bandera1.Engine.GameObjects  .GameObject;
import com.bandera1.Engine.GameObjects.RectangleCollider;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Engine.GameObjects.Textfield;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.bandera1.Engine.GameObjects.Checkbox;

public class LoginScene extends Scene {
    public LoginScene() {
        super();
        /*desing:
         * 2 views: login and register
         * login has 2 textfields: email and password
         * register has 5 textfields: nickname, email, phone  password and passwordConfirmation
         * a button to play as guest
         * a button to change login and register form
         * a button to confirm registration/login
         */
        float labelOffsetX = 0;
        float labelOffsetY = 50f;
        
        //Background
        GameObject background = new GameObject("background");

        int frameCount = 10;
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < frameCount; i++) {
            Texture texture = new Texture(Gdx.files.internal("MainMenuBackground/frame" + i + ".png"));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            frames.add(new TextureRegion(texture));
        }
        Animation<TextureRegion> backgroundAnimation = new Animation<>(0.08f, frames, Animation.PlayMode.LOOP);
        AnimationRenderer backgroundAnimationRenderer = new AnimationRenderer(backgroundAnimation.getKeyFrame(0));

        backgroundAnimationRenderer.addAnimation("titleAnimation", backgroundAnimation);
        background.addComponent(backgroundAnimationRenderer);
        backgroundAnimationRenderer.play("titleAnimation");
        background.transform.position.set(0, 0);
        background.transform.scale.set(1.4f, 1.4f);
        addGameObject(background);

        //Login Form Elements
        GameObject loginEmail = new GameObject("loginEmail");
        loginEmail.addComponent(new Textfield(new Texture("button.jpg"), new Texture("button4.jpg")));
        TextRenderer emailLabel = new TextRenderer("Email:");
        emailLabel.offsetX = labelOffsetX;
        emailLabel.offsetY = labelOffsetY;
        loginEmail.addComponent(emailLabel);
        loginEmail.transform.position.set(0, 150);
        loginEmail.transform.scale.set(2, 2);
        addGameObject(loginEmail);

        GameObject loginPassword = new GameObject("loginPassword");
        loginPassword.addComponent(new Textfield(new Texture("button.jpg"), new Texture("button4.jpg")));
        TextRenderer passwordLabel = new TextRenderer("Password:");
        passwordLabel.offsetX = labelOffsetX;
        passwordLabel.offsetY = labelOffsetY;
        loginPassword.addComponent(passwordLabel);
        loginPassword.transform.position.set(0, 50);
        loginPassword.transform.scale.set(2, 2);
        addGameObject(loginPassword);

        GameObject confirmLoginButton = new GameObject("confirmLoginButton");
        confirmLoginButton.addComponent(new TextureRenderer(new Texture("button.jpg")));
        TextRenderer confirmLoginText = new TextRenderer("Confirm Login");
        confirmLoginText.fontScale = 0.8f;
        confirmLoginButton.addComponent(confirmLoginText);
        confirmLoginButton.addComponent(new RectangleCollider(100, 50));
        // Add login logic component
        confirmLoginButton.addComponent(new LoginLogic());
        confirmLoginButton.transform.position.set(0, -50);
        confirmLoginButton.transform.scale.set(2f, 2f);
        addGameObject(confirmLoginButton);

        GameObject registerButton = new GameObject("registerButton"); // Button to switch TO register view
        registerButton.addComponent(new TextureRenderer(new Texture("button.jpg")));
        registerButton.addComponent(new TextRenderer("Register"));
        registerButton.addComponent(new RectangleCollider(100, 50));
        registerButton.transform.position.set(-100, -190);
        registerButton.transform.scale.set(2f, 2f);
        addGameObject(registerButton);

        //Register Form Elements
        float registerCol1X = -200f;
        float registerCol2X = 200f;

        GameObject registerNickname = new GameObject("registerNickname");
        registerNickname.addComponent(new Textfield(new Texture("button.jpg"), new Texture("button4.jpg")));
        TextRenderer nicknameLabel = new TextRenderer("Nickname:");
        nicknameLabel.offsetX = labelOffsetX;
        nicknameLabel.offsetY = labelOffsetY;
        registerNickname.addComponent(nicknameLabel);
        registerNickname.transform.position.set(registerCol1X, 150);
        registerNickname.transform.scale.set(2, 2);
        registerNickname.enabled = false;
        addGameObject(registerNickname);

        GameObject registerEmail = new GameObject("registerEmail");
        registerEmail.addComponent(new Textfield(new Texture("button.jpg"), new Texture("button4.jpg")));
        TextRenderer regEmailLabel = new TextRenderer("Email:");
        regEmailLabel.offsetX = labelOffsetX;
        regEmailLabel.offsetY = labelOffsetY;
        registerEmail.addComponent(regEmailLabel);
        registerEmail.transform.position.set(registerCol2X, 150);
        registerEmail.transform.scale.set(2, 2);
        registerEmail.enabled = false;
        addGameObject(registerEmail);

        GameObject registerPhone = new GameObject("registerPhone");
        registerPhone.addComponent(new Textfield(new Texture("button.jpg"), new Texture("button4.jpg")));
        TextRenderer phoneLabel = new TextRenderer("Phone:");
        phoneLabel.offsetX = labelOffsetX;
        phoneLabel.offsetY = labelOffsetY;
        registerPhone.addComponent(phoneLabel);
        registerPhone.transform.position.set(registerCol1X, 50);
        registerPhone.transform.scale.set(2, 2);
        registerPhone.enabled = false;
        addGameObject(registerPhone);

        GameObject registerPassword = new GameObject("registerPassword");
        registerPassword.addComponent(new Textfield(new Texture("button.jpg"), new Texture("button4.jpg")));
        TextRenderer regPasswordLabel = new TextRenderer("Password:");
        regPasswordLabel.offsetX = labelOffsetX;
        regPasswordLabel.offsetY = labelOffsetY;
        registerPassword.addComponent(regPasswordLabel);
        registerPassword.transform.position.set(registerCol2X, 50);
        registerPassword.transform.scale.set(2, 2);
        registerPassword.enabled = false;
        addGameObject(registerPassword);

        GameObject registerPasswordConfirmation = new GameObject("registerPasswordConfirmation");
        registerPasswordConfirmation.addComponent(new Textfield(new Texture("button.jpg"), new Texture("button4.jpg")));
        TextRenderer confirmPwdLabel = new TextRenderer("Confirm Password:");
        confirmPwdLabel.offsetX = labelOffsetX;
        confirmPwdLabel.offsetY = labelOffsetY;
        registerPasswordConfirmation.addComponent(confirmPwdLabel);
        registerPasswordConfirmation.transform.position.set(registerCol1X, -50);
        registerPasswordConfirmation.transform.scale.set(2, 2);
        registerPasswordConfirmation.enabled = false;
        addGameObject(registerPasswordConfirmation);

        GameObject confirmRegisterButton = new GameObject("confirmRegisterButton");
        confirmRegisterButton.addComponent(new TextureRenderer(new Texture("button.jpg")));
        TextRenderer confirmRegisterText = new TextRenderer("Confirm Register");
        confirmRegisterText.fontScale = 0.7f;
        confirmRegisterButton.addComponent(confirmRegisterText);
        confirmRegisterButton.addComponent(new RectangleCollider(100, 50));
        // Add registration logic component
        confirmRegisterButton.addComponent(new RegisterLogic());
        confirmRegisterButton.transform.position.set(registerCol2X, -50);
        confirmRegisterButton.transform.scale.set(2f, 2f);
        confirmRegisterButton.enabled = false;
        addGameObject(confirmRegisterButton);

        // Terms of Service checkbox and link
        GameObject termsCheckbox = new GameObject("termsCheckbox");
        termsCheckbox.addComponent(new Checkbox(new Texture("checkbox_unchecked.png"), new Texture("checkbox_checked.png")));
        termsCheckbox.transform.position.set(-190, -120);
        termsCheckbox.transform.scale.set(0.2f, 0.2f);
        termsCheckbox.enabled = false;
        addGameObject(termsCheckbox);

        GameObject termsOfServiceLink = new GameObject("termsOfServiceLink");
        termsOfServiceLink.addComponent(new ClickableUrlRenderer("I accept the Terms of Service", "http://bandera1.ieti.site/terms_of_services"));
        termsOfServiceLink.transform.position.set(30, -120);
        termsOfServiceLink.transform.scale.set(2, 2);
        termsOfServiceLink.enabled = false; // Initially disabled as part of register view
        addGameObject(termsOfServiceLink);

        GameObject loginButton = new GameObject("loginButton"); // Button to switch TO login view
        loginButton.addComponent(new TextureRenderer(new Texture("button.jpg")));
        loginButton.addComponent(new TextRenderer("Login"));
        loginButton.addComponent(new RectangleCollider(100, 50));
        loginButton.transform.position.set(-100, -190);
        loginButton.transform.scale.set(2f, 2f);
        loginButton.enabled = false;
        addGameObject(loginButton);

        // Shared Elements
        GameObject playGuest = new GameObject("playGuest");
        playGuest.addComponent(new TextureRenderer(new Texture("button.jpg")));
        playGuest.addComponent(new SceneChangingButton("Room"));
        TextRenderer playGuestText = new TextRenderer("Play as Guest");
        playGuestText.fontScale = 0.8f;
        playGuest.addComponent(playGuestText);
        playGuest.addComponent(new RectangleCollider(100, 50));
        playGuest.transform.position.set(100, -190);
        playGuest.transform.scale.set(2f, 2f);
        addGameObject(playGuest);

        // Define elements for each view
        List<String> loginViewElements = Arrays.asList("loginEmail", "loginPassword", "confirmLoginButton", "registerButton");
        List<String> registerViewElements = Arrays.asList("registerNickname", "registerEmail", "registerPhone", 
                "registerPassword", "registerPasswordConfirmation", "confirmRegisterButton", "loginButton", 
                "termsCheckbox", "termsOfServiceLink");
        
        // Add view switching logic to buttons
        loginButton.addComponent(new ObjectHideShowButton(registerViewElements, loginViewElements, true));
        registerButton.addComponent(new ObjectHideShowButton(loginViewElements.subList(0, 3), registerViewElements, true));
    }
}
