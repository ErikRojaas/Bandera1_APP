package com.bandera1.Engine.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.bandera1.Engine.Systems.SceneSystem;
import com.badlogic.gdx.math.Vector3;

public class CircleCollider extends Collider {
    public Circle collider;
    public Transform transform;
    public float radius;

    public CircleCollider(float radius) {
        this.collider = new Circle(0, 0, radius);
        this.radius = radius;
    }
    
    @Override
    public void start() {
        if (gameObject != null) {
            transform = gameObject.transform;
        } else {
            Gdx.app.error("CircleCollider", "GameObject es null en start");
        }
    }

    @Override
    public void update() {
        if (transform == null) {
            if (gameObject != null)
                transform = gameObject.transform;
            if (transform == null)
                return;
        }

        Vector3 position = SceneSystem.camera.project(new Vector3(transform.position.x, transform.position.y, 0));
        collider.setPosition(position.x, position.y);
        collider.setRadius(radius);
    }

    @Override
    public boolean collidesWith(Collider other) {
        Class otherClass = other.getClass();
        if (otherClass == CircleCollider.class) {
            return collider.overlaps(((CircleCollider) other).collider);
        } else if (otherClass == RectangleCollider.class) {
            return Intersector.overlaps(collider, ((RectangleCollider) other).collider);
        }
        return false;
    }

    @Override
    public boolean isInside(Vector2 point) {
        return collider.contains(point.x, point.y);
    }
}
