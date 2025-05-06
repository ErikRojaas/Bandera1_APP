package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.bandera1.Components.PlayerAnimator.Direction;
import com.bandera1.Engine.GameObjects.AnimationRenderer;
import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.Systems.InputSystem;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.ServerUtils;

import java.util.List;

public class PlayerMovement extends Component {

    private ServerUtils server;
    private AnimationRenderer animationRenderer;
    private AttackButton attackButton;
    private Direction lastDirection = Direction.DOWN;

    @Override
    public void start() {
        server = ServerUtils.instance;
        animationRenderer = gameObject.getComponent(AnimationRenderer.class);
        attackButton = GameObject.Find("attackButton").getComponent(AttackButton.class);
    }

    @Override
    public void update() {
        if (animationRenderer == null) {
            animationRenderer = gameObject.getComponent(AnimationRenderer.class);
            if (animationRenderer == null) return;
        }

        List<Vector2> touches = InputSystem.getCurrentTouches();
        boolean move = false;
        boolean touchAttack = false;

        for (Vector2 touch : touches) {
            if (!attackButton.isTouched(touch)) move = true;
            if (attackButton.isTouched(touch)) touchAttack = true;
        }

        if (InputSystem.onTouch(0)) {
            if (move) {
                float worldX = InputSystem.getTouchX();
                float worldY = InputSystem.getTouchY();
                Vector2 direction = new Vector2(worldX, worldY).sub(gameObject.transform.position).nor();

                JsonValue data = new JsonValue(JsonValue.ValueType.object);
                JsonValue directionJson = new JsonValue(JsonValue.ValueType.object);
                directionJson.addChild("dx", new JsonValue(direction.x));
                directionJson.addChild("dy", new JsonValue(direction.y));
                data.addChild("direction", directionJson);
                server.send(new ServerMessage("direction", data));

                if (touchAttack) {
                    handleAttackAnimation(worldX, worldY);
                } else {
                    handleWalkAnimation(worldX, worldY);
                }
            }
        } 

        if (InputSystem.onTouchUp(0) || !move) {
            JsonValue data = new JsonValue(JsonValue.ValueType.object);
            JsonValue directionJson = new JsonValue(JsonValue.ValueType.object);
            directionJson.addChild("dx", new JsonValue(0));
            directionJson.addChild("dy", new JsonValue(0));
            data.addChild("direction", directionJson);
            server.send(new ServerMessage("direction", data));

            float worldX = InputSystem.getTouchX();
            float worldY = InputSystem.getTouchY();
            if (touchAttack) {
                handleAttackAnimation(worldX, worldY);
            } else {
                handleIdleAnimation(worldX, worldY);
            }
        }

        Player player = gameObject.getComponent(Player.class);
        if (player != null) {
            player.lastDirection = lastDirection;
            Gdx.app.log("PlayerMovement", "player.lastDirection: " + player.lastDirection.name());
        }
    }

    private void handleWalkAnimation(float worldX, float worldY) {
        Vector2 direction = new Vector2(worldX, worldY).sub(gameObject.transform.position).nor();
        if (Math.abs(direction.x) > Math.abs(direction.y)) {
            if (direction.x > 0) {
                animationRenderer.play("WALK_RIGHT");
                lastDirection = Direction.RIGHT;
            } else {
                animationRenderer.play("WALK_LEFT");
                lastDirection = Direction.LEFT;
            }
        } else {
            if (direction.y > 0) {
                animationRenderer.play("WALK_DOWN");
                lastDirection = Direction.DOWN;
            } else {
                animationRenderer.play("WALK_UP");
                lastDirection = Direction.UP;
            }
        }
        Gdx.app.log("PlayerMovement", "lastDirection: " + lastDirection.name());
    }

    private void handleIdleAnimation(float worldX, float worldY) {
        switch (lastDirection) {
            case RIGHT:
                animationRenderer.play("IDLE_RIGHT");
                break;
            case LEFT:
                animationRenderer.play("IDLE_LEFT");
                break;
            case UP:
                animationRenderer.play("IDLE_UP");
                break;
            case DOWN:
            default:
                animationRenderer.play("IDLE_DOWN");
                break;
        }
        Gdx.app.log("PlayerMovement", "IDLE con lastDirection: " + lastDirection.name());
    }

    private void handleAttackAnimation(float worldX, float worldY) {
        Vector2 direction = new Vector2(worldX, worldY).sub(gameObject.transform.position).nor();
        if (Math.abs(direction.x) > Math.abs(direction.y)) {
            if (direction.x > 0) {
                animationRenderer.play("ATTACK_RIGHT");
                lastDirection = Direction.RIGHT;
            } else {
                animationRenderer.play("ATTACK_LEFT");
                lastDirection = Direction.LEFT;
            }
        } else {
            if (direction.y > 0) {
                animationRenderer.play("ATTACK_DOWN");
                lastDirection = Direction.DOWN;
            } else {
                animationRenderer.play("ATTACK_UP");
                lastDirection = Direction.UP;
            }
        }
        Gdx.app.log("PlayerMovement", "lastDirection: " + lastDirection.name());
    }
}
