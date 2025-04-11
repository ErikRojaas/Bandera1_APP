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
        gameManager.addComponent(new FlagManager());
        addGameObject(gameManager);

        // Fondo
        GameObject background = new GameObject("background");
        background.addComponent(new TextureRenderer(new Texture("background.png")));
        background.transform.scale.set(4f, 4f);
        addGameObject(background);

        // Jugador local
        GameObject player = new GameObject("player");
        player.addComponent(new Player());
        player.addComponent(new PlayerMovement());
        player.addComponent(new PositionSync());
        player.addComponent(new FollowCamera());
        player.transform.scale.set(4f, 4f);

        addGameObject(player);
    }
}
