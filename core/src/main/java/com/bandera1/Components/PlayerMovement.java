package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Engine.Systems.InputSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.bandera1.Utils.ServerMessage;
import java.lang.Math;

public class PlayerMovement extends Component {

    ServerUtils server;

    @Override
    public void start() {
        server = ServerUtils.instance;
    }
    /*
     * 
        const DIRECTIONS = {
            "up":         { dx: 0, dy: -1 },
            "left":       { dx: -1, dy: 0 },
            "down":       { dx: 0, dy: 1 },
            "right":      { dx: 1, dy: 0 },
            "none":       { dx: 0, dy: 0 },
        };
     */
    @Override
    public void update() {
        if (InputSystem.onTouchDown(0)) {
            float x = InputSystem.getTouchX();
            float y = InputSystem.getTouchY();
            Vector2 direction = new Vector2(x, y).sub(gameObject.transform.position);
            direction.nor();
            //get the direction as a string for the server
            JsonValue data = new JsonValue(JsonValue.ValueType.object);
            if (Math.abs(direction.x) > Math.abs(direction.y)) {
                data.addChild("direction", new JsonValue((direction.x > 0) ? "right" : "left"));
            } else {
                data.addChild("direction", new JsonValue((direction.y > 0) ? "down" : "up"));
            }
            //send the direction to the server
            server.send(new ServerMessage("direction", data));
        } else if (InputSystem.onTouchUp(0)) {
            //stop moving
            JsonValue data = new JsonValue(JsonValue.ValueType.object);
            data.addChild("direction", new JsonValue("none"));
            server.send(new ServerMessage("direction", data));
        }
    }
}
