package oscar.medina.galvez.engine.Scenes;

import oscar.medina.galvez.engine.Systems.SceneSystem;

public class SceneIndex {
    public static void addAllScenes() {
        SceneSystem.addScene(new TestScene());
    }
}
