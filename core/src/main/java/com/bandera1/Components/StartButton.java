package com.bandera1.Components;

import com.bandera1.Engine.Systems.InputSystem;
import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Engine.GameObjects.Component;

public class StartButton extends Component {

    @Override
    public void update() {
        if (InputSystem.onTouchDown(0)) {
            SceneSystem.changeScene("Game");
        }
    }
}
