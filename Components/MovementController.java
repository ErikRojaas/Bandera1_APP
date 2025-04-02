package oscar.medina.galvez.engine.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import oscar.medina.galvez.engine.GameObjects.AnimationRenderer;
import oscar.medina.galvez.engine.GameObjects.Component;
import oscar.medina.galvez.engine.GameObjects.GameObject;
import oscar.medina.galvez.engine.Systems.InputSystem;
import oscar.medina.galvez.engine.Systems.SceneSystem;

public class MovementController extends Component {
    public float speed = 750;
    GameObject background;
    float backgroundWidth = 2400;
    float backgroundHeight = 1600;
    AnimationRenderer Ar;

    @Override
    public void start() {
        background = GameObject.Find("background");
        Ar = gameObject.getComponent(AnimationRenderer.class);
    }

    @Override
    public void update() {
        handleInput();
        moveBackground();
        SceneSystem.MoveCameraTo(gameObject.transform.position);
    }

    public void handleInput() {
        float delta = Gdx.graphics.getDeltaTime();
        String animation = null;
        if (InputSystem.onKeyDown(Input.Keys.LEFT)) {
            gameObject.transform.translateX(-speed * delta);
            animation = "walk_left";
        } else if (InputSystem.onKeyDown(Input.Keys.RIGHT)) {
            gameObject.transform.translateX(speed * delta);
            animation = "walk_right";
        } else if (InputSystem.onKeyDown(Input.Keys.UP)) {
            gameObject.transform.translateY(speed * delta);
            animation = "walk_up";
        } else if (InputSystem.onKeyDown(Input.Keys.DOWN)) {
            gameObject.transform.translateY(-speed * delta);
            animation = "walk_down";
        }
        // by touch
        else if (InputSystem.onTouch(0)) {

            float x = InputSystem.getTouchX();
            float y = InputSystem.getTouchY();
            Vector2 direction = new Vector2(x, y).sub(gameObject.transform.position);
            direction.nor();

            String desiredAnimation = null;

            if (direction.x != 0 || direction.y != 0) {
                if (Math.abs(x) > Math.abs(y)) {
                    if (direction.x > 0) {
                        desiredAnimation = "rigth";
                    } else {
                        desirebdAnimation = "left";
                    }
                } else {
                    if (direction.y > 0) {
                        desiredAnimation = "up";
                    } else {
                        desiredAnimation = "down";
                    }
                }
            }

            if (desiredAnimation != null) {
                Gdx.app.log("Animation", "Attempting to play: " + desiredAnimation);
                Ar.play(desiredAnimation);
            } else {
                if (Ar.isPlaying) {
                    Gdx.app.log("Animation", "Stopping animation.");
                    Ar.stop();
                }
            }

            gameObject.transform.translate(direction.scl(speed * delta));
        } else {
            if (Ar.isPlaying) {
                Gdx.app.log("Animation", "Stopping animation.");
                Ar.stop();
            }
        }

    }

    public void moveBackground() {
        if (background.transform.position.x-backgroundWidth > gameObject.transform.position.x) {
            background.transform.position.x -= backgroundWidth;
        }
        else if (background.transform.position.x+backgroundWidth < gameObject.transform.position.x) {
            background.transform.position.x += backgroundWidth;
        }
        else if (background.transform.position.y-backgroundHeight > gameObject.transform.position.y) {
            background.transform.position.y -= backgroundHeight;
        }
        else if (background.transform.position.y+backgroundHeight < gameObject.transform.position.y) {
            background.transform.position.y += backgroundHeight;
        }
    }
}
