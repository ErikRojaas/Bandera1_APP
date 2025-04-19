package com.bandera1;

import com.bandera1.Scenes.*;
import com.bandera1.Engine.Systems.SceneSystem;

public class SceneIndex {
    public static void addAllScenes() {
        SceneSystem.addScene("Menu",new MenuScene());
        SceneSystem.addScene("Game",new GameScene());
        SceneSystem.addScene("Room",new RoomScene());
    }
}
