package com.bandera1.Scenes;

import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.TextureRenderer;
import com.bandera1.Components.*;


import com.badlogic.gdx.graphics.Texture;

public class GameScene extends Scene {
    public GameScene() {
        super();
        GameObject gameManger = new GameObject("gameManager");
        gameManger.addComponent(new PlayerManager());
        addGameObject(gameManger);

        GameObject background = new GameObject("background");
        background.addComponent(new TextureRenderer(new Texture("background.png")));
        addGameObject(background);

        GameObject player = new GameObject("player");
        player.addComponent(new TextureRenderer(new Texture("player.png")));
        player.addComponent(new Player());
        player.transform.scale.set(0.5f,0.5f);
        player.addComponent(new PlayerMovement());
        player.addComponent(new FollowCamera());
        addGameObject(player);


    }
}
