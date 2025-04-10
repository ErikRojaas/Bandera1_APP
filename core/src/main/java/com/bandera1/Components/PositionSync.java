package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.Transform;

public class PositionSync extends Component {
    public Vector2 targetPosition = new Vector2(0,0);
    public Transform transform;
    public Vector2 velocity = new Vector2(0,0);
    public float syncThreshold = 500f;
    public float lerpThreshold = 100f;
    public float snapThreshold = 10.0f;
    public boolean snap = false;

    @Override
    public void init() {
        targetPosition = new Vector2(gameObject.transform.position);
        transform = gameObject.transform;
    }

    @Override
    public void update() {
        //calculate direction
        float delta = Gdx.graphics.getDeltaTime();
        transform.translate(velocity.x * delta, velocity.y * delta);

        float discrepancy = transform.position.dst(velocity);

        if (discrepancy > syncThreshold) {
            transform.position.set(targetPosition);
        } else if (discrepancy > lerpThreshold) {
            transform.position.lerp(targetPosition, Gdx.graphics.getDeltaTime());
        }

        if (snap && discrepancy < snapThreshold) {
            transform.position.set(targetPosition);
        }
    }
}
