package com.bandera1.Utils;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public class ServerMessage {
    public String type;
    public JsonValue data;

    public ServerMessage() {}

    public ServerMessage(String type, JsonValue data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{\"type\":\"" + type + "\",\"data\":" + data.toJson(JsonWriter.OutputType.json) + "}";
    }
}
