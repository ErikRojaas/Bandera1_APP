package com.bandera1.Scenes;

import com.badlogic.gdx.graphics.Texture;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextRenderer;
import com.bandera1.Engine.GameObjects.TextureRenderer;

public class MenuScene extends Scene {
    public MenuScene() {
        super();
        GameObject playerCountText = new GameObject("playerCountText");
        playerCountText.addComponent(new TextRenderer("Players: 0"));
        playerCountText.transform.position.set(0, 200);
        addGameObject(playerCountText);

        GameObject startGameButton = new GameObject("startGameButton");
        startGameButton.addComponent(new TextRenderer("Start Game"));
        startGameButton.transform.position.set(200, 0);
        addGameObject(startGameButton);
    }
}
