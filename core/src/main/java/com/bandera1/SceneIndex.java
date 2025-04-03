package com.bandera1;

import com.bandera1.Engine.Exemples.TestScene;
import com.bandera1.Engine.Systems.SceneSystem;

public class SceneIndex {
    public static void addAllScenes() {
        SceneSystem.addScene(new TestScene());
    }
}
