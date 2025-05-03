package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.Systems.SceneSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class RandomCircleCamera extends Component {
    public boolean active = false;
    public float radius = 10f;

    @Override
    public void update() {
        if (!active) return;
        // Get player position
        Vector2 playerPos = gameObject.transform.position;
        // Random angle in radians
        float angle = MathUtils.random(0f, MathUtils.PI2);
        float x = playerPos.x + radius * MathUtils.cos(angle);
        float y = playerPos.y + radius * MathUtils.sin(angle);
        SceneSystem.MoveCameraTo(x, y);
    }
} 