package com.bandera1.Scenes;

import com.bandera1.Engine.GameObjects.AnimationRenderer;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.bandera1.Components.*;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScene extends Scene {
    public GameScene() {
        super();
        GameObject gameManger = new GameObject("gameManager");
        gameManger.addComponent(new PlayerManager());
        gameManger.addComponent(new KeyManager());
        addGameObject(gameManger);

        GameObject background = new GameObject("background");
        background.addComponent(new TextureRenderer(new Texture("background.png")));
        background.transform.scale.set(0.8f, 0.8f);
        addGameObject(background);

        GameObject player = new GameObject("player");

        PlayerAnimator animator = new PlayerAnimator();
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
        //player.addComponent(new TextureRenderer(new Texture("player.png")));
        player.addComponent(new Player());
        player.transform.scale.set(4f,4f);
        player.addComponent(new FollowCamera());

        addGameObject(player);
    }
}
