package com.bandera1.Scenes;

import com.bandera1.Engine.GameObjects.AnimationRenderer;
import com.bandera1.Engine.GameObjects.CircleCollider;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Engine.GameObjects.ImageRenderer;
import com.bandera1.Components.*;

import com.badlogic.gdx.graphics.Texture;

public class GameScene extends Scene {
    public GameScene() {
        super();

        // Gestores
        GameObject gameManager = new GameObject("gameManager");
        gameManager.addComponent(new PlayerManager());
        gameManager.addComponent(new KeyManager());
        gameManager.addComponent(new FlagManager());
        addGameObject(gameManager);

        // Fondo
        GameObject background = new GameObject("background");
        background.addComponent(new TextureRenderer(new Texture("background.png")));
        background.transform.scale.set(4f, 4f);
        addGameObject(background);

        // Jugador local
        GameObject player = new GameObject("player");
        player.addComponent(new Player());
        player.addComponent(new PlayerMovement());
        player.addComponent(new PositionSync());
        player.addComponent(new FollowCamera());
        player.addComponent(new RandomCircleCamera());
        player.transform.scale.set(4f, 4f);
        addGameObject(player);

        // Timer
        GameObject gameTimer = new GameObject("gameTimer");
        gameTimer.addComponent(new GameTimerManager());
        TextRenderer timerText = new TextRenderer("2:00");
        timerText.setScreenPosition(1150, 1000);
        gameTimer.addComponent(timerText);
        gameTimer.transform.scale.set(5, 5);
        addGameObject(gameTimer);

        // Puntos del jugador
        GameObject playerPointsText = new GameObject("playerPointsText");
        TextRenderer playerPointsTextRenderer = new TextRenderer("Points: 0");
        playerPointsTextRenderer.setScreenPosition(1150, 900);
        playerPointsText.addComponent(playerPointsTextRenderer);
        playerPointsText.transform.scale.set(5, 5);
        addGameObject(playerPointsText);

        // Life Indicator (HUD)
        GameObject lifeIndicator = new GameObject("lifeIndicator");
        ImageRenderer heartImage = new ImageRenderer(new Texture("Life_Bar.png"), 110, 110);
        heartImage.setScreenPosition(50, 950); // Top-left corner
        lifeIndicator.addComponent(heartImage);

        TextRenderer healthText = new TextRenderer("100"); // Default value, should be updated from server
        healthText.setScreenPosition(82, 990); // To the right of the heart image
        lifeIndicator.addComponent(healthText);
        lifeIndicator.transform.scale.set(4, 4); // Adjust scale as needed
        addGameObject(lifeIndicator);

        GameObject attackButton = new GameObject("attackButton");
        attackButton.addComponent(new CircleCollider(50));
        attackButton.addComponent(new AttackButton());
        attackButton.addComponent(new TextureRenderer(new Texture("attack_button.png")));
        attackButton.transform.scale.set(0.4f, 0.4f);
        addGameObject(attackButton);
    }
}
