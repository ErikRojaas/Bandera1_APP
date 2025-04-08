package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
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

    @Override
    public void update() {
        if (InputSystem.onTouch(0)) {
            float x = InputSystem.getTouchX();
            float y = InputSystem.getTouchY();
            Vector2 direction = new Vector2(x, y).sub(gameObject.transform.position);
            direction.nor();
            //get the direction as a string for the server
            JsonValue data = new JsonValue(JsonValue.ValueType.object);
            JsonValue directionJson = new JsonValue(JsonValue.ValueType.object);
            directionJson.addChild("dx",new JsonValue(direction.x*1000));
            directionJson.addChild("dy",new JsonValue(direction.y*1000));
            data.addChild("direction",directionJson);
            Gdx.app.log("PlayerMovement",directionJson.toString());
            //send the direction to the server
            server.send(new ServerMessage("direction", data));
        } else if (InputSystem.onTouchUp(0)) {
            //stop moving
            JsonValue data = new JsonValue(JsonValue.ValueType.object);
            JsonValue directionJson = new JsonValue(JsonValue.ValueType.object);
            directionJson.addChild("dx",new JsonValue(0));
            directionJson.addChild("dy",new JsonValue(0));
            data.addChild("direction",directionJson);
            server.send(new ServerMessage("direction", data));
        }
    }
}
