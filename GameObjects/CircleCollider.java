package oscar.medina.galvez.engine.GameObjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

public class CircleCollider extends Collider {
    public Circle collider;

    public CircleCollider(Circle collider) {
        this.collider = collider;
    }

    public CircleCollider(float x, float y, float radius) {
        this.collider = new Circle(x, y, radius);
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
}
