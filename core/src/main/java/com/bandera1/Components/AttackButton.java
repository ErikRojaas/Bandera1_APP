package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.ImageRenderer;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Engine.GameObjects.GameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.bandera1.Engine.GameObjects.RectangleCollider;
import com.bandera1.Engine.Systems.InputSystem;

import java.util.List;
import java.util.Set;

public class AttackButton extends Component {
    private ImageRenderer imageRenderer;
    private static RectangleCollider buttonCollider;
    private static boolean initialized = false;
    public static boolean touchedThisFrame = false;

    private static final float BUTTON_WIDTH = 300f;
    private static final float BUTTON_HEIGHT = 300f;
    private static float BUTTON_POS_X = 0f;
    private static float BUTTON_POS_Y = 0f;

    public static boolean isScreenPosTouchingButton(Vector2 screenPos) {
        if (buttonCollider == null) return false;
        return buttonCollider.collider.contains(screenPos);
    }

    public static boolean isTouchOnButton(int pointerId) {
        if (buttonCollider == null || !InputSystem.isPointerDown(pointerId)) return false;

        Vector2 screenPos = InputSystem.getPointerPosition(pointerId);

        return false;
    }

    @Override
    public void init() {
        Texture buttonTexture = new Texture(Gdx.files.internal("attack_button.png"));

        imageRenderer = new ImageRenderer(buttonTexture, BUTTON_WIDTH, BUTTON_HEIGHT);
        gameObject.addComponent(imageRenderer);

        BUTTON_POS_X = SceneSystem.width - 400f;
        BUTTON_POS_Y = 60f;
        imageRenderer.setScreenPosition(BUTTON_POS_X, BUTTON_POS_Y);

        if (!initialized) {
            buttonCollider = new RectangleCollider(BUTTON_WIDTH, BUTTON_HEIGHT);
            buttonCollider.collider.setPosition(BUTTON_POS_X, BUTTON_POS_Y);
            initialized = true;
            Gdx.app.log("AttackButton", "Collider initialized at (" + BUTTON_POS_X + ", " + BUTTON_POS_Y + ") size (" + BUTTON_WIDTH + ", " + BUTTON_HEIGHT + ")");
        }
    }

    @Override
    public void update() {
        List<Vector2> currentTouch = InputSystem.getCurrentTouches();
        //if any touch is inside the collider, request attack
        boolean inside = false;
        for (Vector2 touch : currentTouch) {
            if (buttonCollider.isInside(touch)) {
                inside = true;
                break;
            }
        }
        PlayerManager.requestAttack();
    }
}
