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

    @Override
    public void update() {
        if (InputSystem.onTouch(0)) {
            float delta = Gdx.graphics.getDeltaTime();
            float x = InputSystem.getTouchX();
            float y = InputSystem.getTouchY();
            Vector2 direction = new Vector2(x, y).sub(gameObject.transform.position);
            direction.nor();
            gameObject.transform.Translate(direction.x * speed * delta, direction.y * speed * delta);
        }

        //mover la camera a este objeto
        //SceneSystem.MoveCameraTo(gameObject.transform.position);
    }
}
