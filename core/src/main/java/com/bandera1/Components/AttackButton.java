package com.bandera1.Components;

import com.badlogic.gdx.Game;
import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.GameObject;

public class AttackButton extends Component {
    GameObject player;
    float offsetX = 0;
    float offsetY = 0;

    @Override
    public void start() {
        player = GameObject.Find("player");
    }

    @Override
    public void update() {
       gameObject.transform.position.set(player.transform.position.cpy().add(offsetX, offsetY));
    }
}
