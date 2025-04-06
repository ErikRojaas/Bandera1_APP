package com.bandera1.Engine.Exemples;

import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.WebSocketEventListener;
import com.bandera1.Engine.GameObjects.Component;

public class ComponentSocketListener extends Component implements WebSocketEventListener {

    @Override
    public void init() {
       ServerUtils.instance.addListener(this);
    }

    @Override
    public void onConnect() {
        // implement your own logic here
    }

    @Override
    public void onDisconnect(int closeCode, String reason) {
         // implement your own logic here
    }

    @Override
    public void onMessage(ServerMessage message) {
        // implement your own logic here
    }

    @Override
    public void onBinaryMessage(byte[] data) {
        // implement your own logic here
    }

    @Override
    public void onError(Throwable error) {
         // implement your own logic here
    }
    
}
