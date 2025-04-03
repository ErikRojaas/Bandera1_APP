package com.bandera1.Engine.Exemples;


import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.TextureRenderer;

import com.badlogic.gdx.graphics.Texture;

public class TestScene extends Scene {
    public TestScene() {
        super();

        GameObject background = new GameObject("background");
        background.transform.scale.set(0.1f,0.1f);
        background.transform.position.set(500,0);
        Texture backgroundTexture = new Texture("background.png");
        background.addComponent(new TextureRenderer(backgroundTexture));
        addGameObject(background);
    }
}
