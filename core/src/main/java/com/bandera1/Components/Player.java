package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.Component;

public class Player extends Component {
    public String id;
    public String name;
    public int skinId = 1; 
    public boolean hasKey = false;
    public boolean hasFlag = false;
    public int points = 0;
    public int teamId;

    // NUEVO
    public int health = 3;
    public boolean isDead = false;
    public float respawnTimeLeft = 0f;

    public Player() {}

    public Player(String id, int skinId, boolean hasKey, boolean hasFlag, int teamId) {
        this.id = id;
        this.skinId = skinId;
        this.hasKey = hasKey;
        this.hasFlag = hasFlag;
        this.teamId = teamId;
    }

    public Player(String id, int skinId) {
        this.id = id;
        this.skinId = skinId;
    }
}

