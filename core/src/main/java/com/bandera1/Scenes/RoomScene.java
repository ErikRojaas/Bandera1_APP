package com.bandera1.Scenes;

import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.RectangleCollider;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import com.bandera1.Components.PlayerCountText;
import com.bandera1.Components.SceneChangingButton;
import com.bandera1.Components.TimerManager;

public class RoomScene extends Scene {

    public RoomScene() {
        super();
        // Background

        // Player Text
        GameObject playerCountText = new GameObject("playerCountText");
        playerCountText.addComponent(new TextRenderer("Players: 0"));
        playerCountText.addComponent(new PlayerCountText());
        playerCountText.transform.position.set(0, 200);
        playerCountText.transform.scale.set(2, 2);
        addGameObject(playerCountText);

        // Won Text TODO
        GameObject lastWonText = new GameObject("lastWonText");
        lastWonText.addComponent(new TextRenderer("No game has been played yet"));
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
        backBottomText.addComponent(new TextRenderer("Back to menu"));
        backBottomText.addComponent(new TextureRenderer(new Texture("button.jpg")));
        backBottomText.addComponent(new RectangleCollider(100, 50));
        backBottomText.addComponent(new SceneChangingButton("Menu"));
        backBottomText.transform.position.set(-350, -200);
        backBottomText.transform.scale.set(2, 2);
        addGameObject(backBottomText);
    }
}
