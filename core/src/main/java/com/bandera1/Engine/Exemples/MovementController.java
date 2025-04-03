package com.bandera1.Engine.Exemples;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import com.bandera1.Engine.Systems.InputSystem;
import com.bandera1.Engine.GameObjects.Component;

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
            gameObject.transform.translate(new Vector2(direction.x * speed * delta, direction.y * speed * delta));
        }

        //mover la camera a este objeto
        //SceneSystem.MoveCameraTo(gameObject.transform.position);
    }
}
