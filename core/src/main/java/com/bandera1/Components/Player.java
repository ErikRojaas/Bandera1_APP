package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.Component;

public class Player extends Component {
    public String id;
    public String name;
    public int skinId = 1; 

    public Player() {}

    public Player(String id) {
        this.id = id;
    }

    public Player(String id, int skinId) {
        this.id = id;
        this.skinId = skinId;
    }
}
