package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import com.bandera1.Engine.Systems.InputSystem;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.Collider;

public class SceneChangingButton extends Component {

    String sceneName;
    Collider collider;

    public SceneChangingButton(String sceneName) {
        this.sceneName = sceneName;
    }

    @Override
    public void start() {
        collider = gameObject.getComponent(Collider.class);
        if (collider == null) {
            throw new RuntimeException("SceneChangingButton component requires a Collider component on the same GameObject");
        }
    }

    @Override
    public void update() {
        if (InputSystem.onTouchDown(0)) {
            float x = InputSystem.getTouchX();
            float y = InputSystem.getTouchY();
            Gdx.app.log("SceneChangingButton", "Touch down at " + x + ", " + y);

            if (collider.isInside(new Vector2(x, y)))
                SceneSystem.changeScene(sceneName);
        }
    }
}
