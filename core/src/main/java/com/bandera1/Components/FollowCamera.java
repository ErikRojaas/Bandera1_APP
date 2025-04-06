package com.bandera1.Components;

import com.bandera1.Engine.Systems.SceneSystem;
import com.bandera1.Engine.GameObjects.Component;

public class FollowCamera extends Component {

    @Override
    public void update() {
        SceneSystem.MoveCameraTo(gameObject.transform.position);
    }
    
}
