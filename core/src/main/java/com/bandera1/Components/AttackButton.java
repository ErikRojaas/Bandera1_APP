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

        // Redimensionar el botón a algo más pequeño y cómodo
        float width = 300f;
        float height = 300f;

        imageRenderer = new ImageRenderer(buttonTexture, width, height);
        gameObject.addComponent(imageRenderer);

        // Posición visible: esquina inferior derecha
        float posX = SceneSystem.width - 400f; // 800 - 60
        float posY = 60f;
        imageRenderer.setScreenPosition(posX, posY);
    }

    @Override
    public void update() {
        Gdx.app.log("AttackButton", "Update ejecutado");

        if (Gdx.input.justTouched()) {
            Vector2 touch = new Vector2(Gdx.input.getX(), SceneSystem.height - Gdx.input.getY());

            if (imageRenderer.isTouched(touch.x, touch.y)) {
                Gdx.app.log("AttackButton", "Botón de ataque pulsado");
                PlayerManager.requestAttack();
            }
        }
    }
}
