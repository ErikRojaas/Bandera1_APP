package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.ImageRenderer;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Engine.GameObjects.GameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

public class AttackButton extends Component {
    private ImageRenderer imageRenderer;

    @Override
    public void init() {
        Texture buttonTexture = new Texture(Gdx.files.internal("attack_button.png"));
        imageRenderer = new ImageRenderer(buttonTexture, 288, 288); // Tamaño más pequeño
        gameObject.addComponent(imageRenderer);

        imageRenderer.setScreenPosition(1900, 80); // Abajo a la derecha para pantalla 800x480
    }

    @Override
    public void update() {
        if (Gdx.input.justTouched()) {
            Vector2 touch = new Vector2(Gdx.input.getX(), SceneSystem.height - Gdx.input.getY());

            if (imageRenderer.isTouched(touch.x, touch.y)) {
                PlayerManager.requestAttack();
            }
        }
    }
}
