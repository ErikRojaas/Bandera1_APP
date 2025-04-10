package com.bandera1.Scenes;

import com.bandera1.Engine.GameObjects.AnimationRenderer;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.bandera1.Components.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScene extends Scene {
    public GameScene() {
        super();

        // Gestores
        GameObject gameManager = new GameObject("gameManager");
        gameManager.addComponent(new PlayerManager());
        gameManager.addComponent(new KeyManager());
        addGameObject(gameManager);

        // Fondo
        GameObject background = new GameObject("background");
        background.addComponent(new TextureRenderer(new Texture("background.png")));
        background.transform.scale.set(4f, 4f);
        addGameObject(background);

        // Jugador local
        GameObject player = new GameObject("player");

        int skinId = 1;

        Player playerComponent = new Player("local", skinId);

        // Animaciones personalizadas por skin
        PlayerAnimator animator = new PlayerAnimator("Characters/Character" + skinId + "/");
        AnimationRenderer animationRenderer = new AnimationRenderer();
        for (PlayerAnimator.Action action : PlayerAnimator.Action.values()) {
            for (PlayerAnimator.Direction direction : PlayerAnimator.Direction.values()) {
                animationRenderer.addAnimation(action.name() + "_" + direction.name(), animator.getAnimation(action, direction));
            }
        }
        animationRenderer.play("IDLE_DOWN");

        player.addComponent(animationRenderer);
        player.addComponent(new PlayerMovement());
        player.addComponent(new PositionSync());
        player.addComponent(playerComponent);
        player.addComponent(new FollowCamera());
        player.transform.scale.set(4f, 4f);

        addGameObject(player);
    }
}
