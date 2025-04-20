package com.bandera1.Engine.GameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.bandera1.Engine.Systems.SceneSystem;

import com.badlogic.gdx.math.Vector3;

public class RectangleCollider extends Collider {
    public Rectangle collider;
    public Transform transform;
    public float width;
    public float height;

    public RectangleCollider(Rectangle collider) {
        this.collider = collider;
    }

    public RectangleCollider(float width, float height) {
        this.collider = new Rectangle(0, 0, width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void start() {
        transform = gameObject.transform;
    }

    @Override
    public void update() {
        collider.set(transform.position.x - width/2, transform.position.y - height/2, width, height);
    }

    @Override
    public boolean collidesWith(Collider other) {
        Class otherClass = other.getClass();
        if (otherClass == RectangleCollider.class) {
            return collider.overlaps(((RectangleCollider) other).collider);
        } else if (otherClass == CircleCollider.class) {
            return Intersector.overlaps(collider, ((RectangleCollider) other).collider);
        }
        return false;
    }

    @Override
    public boolean isInside(Vector2 point) {
        Gdx.app.log("RectangleCollider","collider: " + collider.x + ", " + collider.y + ", " + collider.width + ", " + collider.height);
        Gdx.app.log("RectangleCollider","point: " + point);
        Gdx.app.log("RectangleCollider","result: " + collider.contains(point.x, point.y));
        return collider.contains(point.x, point.y);
    }
}
