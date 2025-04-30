package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.Transform;

public class PositionSync extends Component {
    public Vector2 targetPosition = new Vector2(0,0);
    public Transform transform;
    public Vector2 velocity = new Vector2(0,0);
    public float syncThreshold = 1000f;
    public float lerpThreshold = 500f;
    public float snapThreshold = 1f;
    public boolean snap = false;

    @Override
    public void init() {
        targetPosition = new Vector2(gameObject.transform.position);
        transform = gameObject.transform;
    }

    @Override
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
    
        Player playerComponent = gameObject.getComponent(Player.class);
        float speedModifier = 1.0f;
        if (playerComponent != null) {
            if (playerComponent.hasKey || playerComponent.hasFlag) {
                speedModifier = 0.5f;
            }
        }
    
        transform.translate(velocity.x * delta * speedModifier, velocity.y * delta * speedModifier);
    
        float discrepancy = transform.position.dst(targetPosition);
    
        if (snap) {
            transform.position.set(targetPosition);
            snap = false;
            return;
        }
    
        if (discrepancy > syncThreshold) {
            transform.position.set(targetPosition);
        } else if (discrepancy > snapThreshold) {
            Vector2 correction = new Vector2(targetPosition).sub(transform.position).nor().scl(delta * 100f * speedModifier);
            transform.translate(correction);
        }
    }
    
}
