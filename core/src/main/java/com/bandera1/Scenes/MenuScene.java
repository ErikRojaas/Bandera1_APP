package com.bandera1.Scenes;

import com.badlogic.gdx.graphics.Texture;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.bandera1.Components.*;

public class MenuScene extends Scene {
    public MenuScene() {
        super();

        GameObject background = new GameObject("background");
        background.addComponent(new TextureRenderer(new Texture("MainMenuBackground/frame0.png")));
        background.transform.scale.set(1.4f, 1.4f);
        addGameObject(background);

        GameObject playerCountText = new GameObject("playerCountText");
        playerCountText.addComponent(new TextRenderer("Players: 0"));
        playerCountText.addComponent(new PlayerCountText());
        playerCountText.transform.position.set(0, 200);
        playerCountText.transform.scale.set(2, 2);
        addGameObject(playerCountText);

        GameObject startGameButton = new GameObject("startGameButton");
        startGameButton.addComponent(new TextureRenderer(new Texture("button.jpg")));
        startGameButton.addComponent(new TextRenderer("Start Game"));
        startGameButton.addComponent(new StartButton());
        startGameButton.transform.scale.set(3, 3);
        addGameObject(startGameButton);
    }
}
