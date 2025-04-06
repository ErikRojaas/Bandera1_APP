package com.bandera1.Scenes;

import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.bandera1.Components.*;


import com.badlogic.gdx.graphics.Texture;

public class GameScene extends Scene {
    GameScene() {
        super();
        GameObject background = new GameObject("background");
        background.addComponent(new TextureRenderer(new Texture("background.png")));
        addGameObject(background);

        GameObject player = new GameObject("player");
        player.addComponent(new TextureRenderer(new Texture("player.png")));
        player.addComponent(new Player());
        player.addComponent(new FollowCamera());
        player.addComponent(new PlayerMovement());
        addGameObject(player);

        GameObject gameManger = new GameObject("gameManager");
        gameManger.addComponent(new PlayerManager());
        addGameObject(gameManger);
    }
}
