package com.bandera1.Scenes;

import com.bandera1.Engine.GameObjects.AnimationRenderer;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.RectangleCollider;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bandera1.Components.LastWinnerManager;
import com.bandera1.Components.PlayerCountText;
import com.bandera1.Components.SceneChangingButton;
import com.bandera1.Components.TimerManager;

public class RoomScene extends Scene {

    public RoomScene() {
        super();
        float labelOffsetX = 0;
        float labelOffsetY = 50f;

        // Background
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

        // Player Text
        GameObject playerCountText = new GameObject("playerCountText");
        playerCountText.addComponent(new TextRenderer("Players: 0"));
        playerCountText.addComponent(new PlayerCountText());
        playerCountText.transform.position.set(0, 200);
        playerCountText.transform.scale.set(2, 2);
        addGameObject(playerCountText);

        // Won Text
        GameObject lastWonText = new GameObject("lastWonText");
        lastWonText.addComponent(new TextRenderer("No game has been played yet"));
        lastWonText.addComponent(new LastWinnerManager());
        lastWonText.transform.position.set(0, -30);
        lastWonText.transform.scale.set(4, 4);
        addGameObject(lastWonText);

        // Timer
        GameObject roomTimer = new GameObject("Timer");
        roomTimer.addComponent(new TimerManager());
        roomTimer.addComponent(new TextRenderer("2:00"));
        roomTimer.transform.position.set(0, 77);
        roomTimer.transform.scale.set(2, 2);
        addGameObject(roomTimer);
        // Back bottom Text
        GameObject backBottomText = new GameObject("backBottomText");
        backBottomText.addComponent(new TextureRenderer(new Texture("buttonLogin.png")));
        backBottomText.addComponent(new SceneChangingButton("Menu"));
        TextRenderer backText = new TextRenderer("Back to menu");
        backText.fontScale = 0.28f;
        backBottomText.addComponent(backText);
        backBottomText.addComponent(new RectangleCollider(100, 50));
        backBottomText.transform.position.set(-350, -200);
        backBottomText.transform.scale.set(7f, 7f);
        addGameObject(backBottomText);
    }
}
