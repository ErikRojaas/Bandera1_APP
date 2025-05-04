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
    public static boolean touchedThisFrame = false;

    public static boolean isTouchingButton(Vector2 touch) {
        float posX = SceneSystem.width - 400f;
        float posY = 60f;
        float width = 300f;
        float height = 300f;

        return touch.x >= posX && touch.x <= posX + width &&
               touch.y >= posY && touch.y <= posY + height;
    }

    @Override
    public void init() {
        Texture buttonTexture = new Texture(Gdx.files.internal("attack_button.png"));

        float width = 300f;
        float height = 300f;

        imageRenderer = new ImageRenderer(buttonTexture, width, height);
        gameObject.addComponent(imageRenderer);

        float posX = SceneSystem.width - 400f;
        float posY = 60f;
        imageRenderer.setScreenPosition(posX, posY);
    }

    @Override
    public void update() {
        touchedThisFrame = false;

        if (Gdx.input.justTouched()) {
            Vector2 touch = new Vector2(Gdx.input.getX(), SceneSystem.height - Gdx.input.getY());

            if (isTouchingButton(touch)) {
                touchedThisFrame = true;
                PlayerManager.requestAttack();
            }
        }
    }
}
