package com.bandera1.Scenes;

import com.badlogic.gdx.graphics.Texture;
import com.bandera1.Engine.GameObjects.AnimationRenderer;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.bandera1.Components.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class MenuScene extends Scene {
    public MenuScene() {
        super();
        // Fondo con animación
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

        // Texto con de numero de jugadores
        GameObject playerCountText = new GameObject("playerCountText");
        playerCountText.addComponent(new TextRenderer("Players: 0"));
        playerCountText.addComponent(new PlayerCountText());
        playerCountText.transform.position.set(0, 200);
        playerCountText.transform.scale.set(2, 2);
        addGameObject(playerCountText);

        // Botón de inicio de juego
        GameObject startGameButton = new GameObject("startGameButton");
        startGameButton.addComponent(new TextureRenderer(new Texture("button.jpg")));
        startGameButton.addComponent(new TextRenderer("Start Game"));
        startGameButton.addComponent(new StartButton());
        startGameButton.transform.scale.set(3, 3);
        addGameObject(startGameButton);
    }
}
