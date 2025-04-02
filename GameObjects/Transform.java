package oscar.medina.galvez.engine.GameObjects;

import com.badlogic.gdx.math.Vector2;

public class Transform extends Component {
    public Vector2 position;
    public float rotation;
    public Vector2 scale;

    public Transform() {
        this.position = Vector2.Zero;
        this.rotation = 0;
        this.scale = new Vector2(1,1);
    }
    public Transform(Vector2 position) {
        this.position = position;
        this.rotation = 0;
        this.scale = new Vector2(1,1);
    }

    public Transform(Vector2 position, float rotation) {
        this.position = position;
        this.rotation = rotation;
        this.scale = new Vector2(1,1);
    }

    public Transform(Vector2 position, float rotation, Vector2 scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void translate(Vector2 translation) {
        this.position = this.position.add(translation);
    }

    public void translateX(float x) {
        this.position = this.position.add(new Vector2(x, 0));
    }

    public void translateY(float y) {
        this.position = this.position.add(new Vector2(0, y));
    }

    public void rotate(float angle) {
        this.rotation += angle;
    }

    public void scale(Vector2 scaleFactor) {
        this.scale = this.scale.scl(scaleFactor);
    }

    public void scale(float scaleFactor) {
        this.scale = this.scale.scl(scaleFactor);
    }

    public void scale(float x, float y) {
        this.scale = this.scale.scl(x, y);
    }

    public void scaleX(float x) {
        this.scale = this.scale.scl(x, 1);
    }

    public void scaleY(float y) {
        this.scale = this.scale.scl(1, y);
    }
}
