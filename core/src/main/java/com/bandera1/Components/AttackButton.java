package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.Systems.SceneSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;

public class AttackButton extends Component {
    private Texture buttonTexture;
    private Vector2 positionScreen;
    private Vector2 positionWorld;
    private float width;
    private float height;

    @Override
    public void init() {
        buttonTexture = new Texture(Gdx.files.internal("attack_button.png"));
        width = 10f; // Más pequeño
        height = 10f;
        positionScreen = new Vector2(
            SceneSystem.width - width - 20f,
            20f
        );
        // Convertimos la posición de pantalla a posición de mundo
        positionWorld = SceneSystem.ScreenToWorldPoint(positionScreen);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(buttonTexture, positionWorld.x, positionWorld.y, width, height);
    }

    @Override
    public void update() {
        if (Gdx.input.justTouched()) {
            Vector2 touchScreen = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 touchWorld = SceneSystem.ScreenToWorldPoint(touchScreen);

            if (touchWorld.x >= positionWorld.x && touchWorld.x <= positionWorld.x + width &&
                touchWorld.y >= positionWorld.y && touchWorld.y <= positionWorld.y + height) {
                PlayerManager.requestAttack();
            }
        }
    }

    @Override
    public void dispose() {
        if (buttonTexture != null) {
            buttonTexture.dispose();
        }
    }
}
