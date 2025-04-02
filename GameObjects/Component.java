package oscar.medina.galvez.engine.GameObjects;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Component implements Serializable {
    public boolean enabled;
    public GameObject gameObject;

    public Component() {
        enabled = true;
    }

    public void start() {

    }

    public void update() {

    }

    public void render(SpriteBatch batch) {

    }

    public void dispose() {

    }

    public void onCollisionEnter(GameObject other) {

    }

    public void onCollisionExit(GameObject other) {

    }

    public void onCollisionStay(GameObject other) {

    }
}