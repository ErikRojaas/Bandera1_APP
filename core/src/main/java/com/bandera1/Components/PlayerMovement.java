package com.bandera1.Components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.bandera1.Engine.GameObjects.AnimationRenderer;
import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Engine.Systems.InputSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.bandera1.Utils.ServerMessage;
import java.lang.Math;
import com.bandera1.Components.AttackButton;
import com.bandera1.Engine.Systems.SceneSystem;

public class PlayerMovement extends Component {

    ServerUtils server;
    AnimationRenderer animationRenderer;
    AttackButton attackButton;

    @Override
    public void start() {
        server = ServerUtils.instance;
        animationRenderer = (AnimationRenderer) gameObject.getComponent(AnimationRenderer.class);
        attackButton = GameObject.Find("attackButton").getComponent(AttackButton.class);
    }

    @Override
    public void update() {

        if (animationRenderer == null) {
            animationRenderer = gameObject.getComponent(AnimationRenderer.class);
            if (animationRenderer == null) return;
        }

        if (InputSystem.onTouch(0)) {
            Vector2 screenTouchPos = new Vector2(Gdx.input.getX(), SceneSystem.height - Gdx.input.getY());

            boolean touchIsOnAttackButton = false;//attackButton.isScreenPosTouchingButton(screenTouchPos);

            if (!touchIsOnAttackButton) {
                float worldX = InputSystem.getTouchX();
                float worldY = InputSystem.getTouchY();
                Vector2 direction = new Vector2(worldX, worldY).sub(gameObject.transform.position);
                direction.nor();

                JsonValue data = new JsonValue(JsonValue.ValueType.object);
                JsonValue directionJson = new JsonValue(JsonValue.ValueType.object);
                directionJson.addChild("dx", new JsonValue(direction.x * 1.0));
                directionJson.addChild("dy", new JsonValue(direction.y * 1.0));
                data.addChild("direction", directionJson);
                server.send(new ServerMessage("direction", data));

                handleWalkAnimation(worldX, worldY);
            }
        } else if (InputSystem.onTouchUp(0)) {
            JsonValue data = new JsonValue(JsonValue.ValueType.object);
            JsonValue directionJson = new JsonValue(JsonValue.ValueType.object);
            directionJson.addChild("dx", new JsonValue(0));
            directionJson.addChild("dy", new JsonValue(0));
            data.addChild("direction", directionJson);
            server.send(new ServerMessage("direction", data));

            float worldX = InputSystem.getTouchX();
            float worldY = InputSystem.getTouchY();
            handleIdleAnimation(worldX, worldY);
        }
    }

    public void handleWalkAnimation(float worldX, float worldY) {
        Vector2 direction = new Vector2(worldX, worldY).sub(gameObject.transform.position);
        direction.nor();
        if (Math.abs(direction.x) > Math.abs(direction.y)) {
            if (direction.x > 0) {
                animationRenderer.play("WALK_RIGHT");
            } else {
                animationRenderer.play("WALK_LEFT");
            }
        } else {
            if (direction.y > 0) {
                animationRenderer.play("WALK_DOWN");
            } else {
                animationRenderer.play("WALK_UP");
            }
        }
    }

    public void handleIdleAnimation(float worldX, float worldY) {
        Vector2 direction = new Vector2(worldX, worldY).sub(gameObject.transform.position);
        direction.nor();
        if (Math.abs(direction.x) > Math.abs(direction.y)) {
            if (direction.x > 0) {
                animationRenderer.play("IDLE_RIGHT");
            } else {
                animationRenderer.play("IDLE_LEFT");
            }
        } else {
            if (direction.y > 0) {
                animationRenderer.play("IDLE_DOWN");
            } else {
                animationRenderer.play("IDLE_UP");
            }
        }
    }
}
