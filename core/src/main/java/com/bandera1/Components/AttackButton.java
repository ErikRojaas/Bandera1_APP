package com.bandera1.Components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.bandera1.Engine.GameObjects.CircleCollider;
import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Scene;
import com.bandera1.Engine.Systems.InputSystem;
import com.bandera1.Engine.Systems.SceneSystem;

import java.util.List;

public class AttackButton extends Component {
    GameObject player;
    float offsetX = 300;
    float offsetY = -150;
    private CircleCollider collider;

    @Override
    public void start() {
        player = GameObject.Find("player");
        collider = gameObject.getComponent(CircleCollider.class);
    }

    public boolean isTouched(Vector2 touch) {
        Gdx.app.log("CircleCollider", ""+touch.x + ", " + touch.y);
        Vector3 position = SceneSystem.camera.unproject(new Vector3(touch.x, touch.y, 0));
        return collider.isInside(new Vector2(position.x, position.y));
    }

    @Override
    public void update() {
       gameObject.transform.position.set(player.transform.position.cpy().add(offsetX, offsetY));

       List<Vector2> touches = InputSystem.getCurrentTouches();
       boolean isTouched = false;
       for (Vector2 touch : touches) {
           if (isTouched(touch)) {
               isTouched = true;
               break;
           }
       }
       if (isTouched) {
           PlayerManager.requestAttack();
       }
    }
}
