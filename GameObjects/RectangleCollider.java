package oscar.medina.galvez.engine.GameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;

public class RectangleCollider extends Collider {
    public Rectangle collider;

    public RectangleCollider(Rectangle collider) {
        this.collider = collider;
    }

    public RectangleCollider(float x, float y, float width, float height) {
        this.collider = new Rectangle(x, y, width, height);
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
}
