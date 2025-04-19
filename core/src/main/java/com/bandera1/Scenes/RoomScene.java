package com.bandera1.Scenes;

import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.RectangleCollider;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Engine.GameObjects.TextureRenderer;

import com.badlogic.gdx.graphics.Texture;

import com.bandera1.Components.PlayerCountText;

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
        playerCountText.addComponent(new TextRenderer("No game has been played yet"));
        playerCountText.transform.position.set(0, 0);
        playerCountText.transform.scale.set(2, 2);
        addGameObject(lastWonText);

        // Timer
        GameObject roomTimer = new GameObject("roomTimer");
        playerCountText.addComponent(new TextRenderer("2:00"));
        playerCountText.transform.position.set(0, -100);
        playerCountText.transform.scale.set(2, 2);
        addGameObject(roomTimer);
        // Back bottom Text
        GameObject backBottomText = new GameObject("backBottomText");
        backBottomText.addComponent(new TextRenderer("Back to menu"));
        backBottomText.addComponent(new TextureRenderer(new Texture("button.jpg")));
        backBottomText.addComponent(new RectangleCollider(100, 50));
        backBottomText.transform.position.set(0, -200);
        backBottomText.transform.scale.set(2, 2);
        addGameObject(backBottomText);
    }
}
