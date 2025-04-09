package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.Transform;

public class PositionSync extends Component {
    public Vector2 targetPosition = new Vector2(0,0);
    public Transform transform;
    public float moveSpeed = 300f;
    public final int ServerFPS = 120;
    public float syncThreshold = 500f;
    public float snapThreshold = 10.0f;
    public boolean snap = true;

    @Override
    public void init() {
        targetPosition = new Vector2(gameObject.transform.position);
        transform = gameObject.transform;
    }

    @Override
    public void update() {
        //calculate direction
        Vector2 direction = targetPosition.cpy().sub(transform.position);
        direction.nor();
        transform.translate(direction.cpy().scl(moveSpeed * 1f/ServerFPS));

        //sync
        if (Math.abs(transform.position.x - targetPosition.x) > syncThreshold) {
            transform.position.x = targetPosition.x;
        }
        if (Math.abs(transform.position.y - targetPosition.y) > syncThreshold) {
            transform.position.y = targetPosition.y;
        }

        //snap to grid
        if (snap && Math.abs(transform.position.x - targetPosition.x) < syncThreshold) {
            transform.position.x = targetPosition.x;
        }
        if (snap && Math.abs(transform.position.y - targetPosition.y) < syncThreshold) {
            transform.position.y = targetPosition.y;
        }
    }

    void snap() {
        transform.position = targetPosition.cpy();
    }

    public void setTargetPosition(float x, float y) {
        targetPosition.set(x,y);
    }
}
