package com.bandera1;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.bandera1.Engine.Systems.CollisionSystem;
import com.bandera1.Engine.Systems.InputSystem;
import com.bandera1.Engine.Systems.SceneSystem;
import com.badlogic.gdx.Gdx;
import com.bandera1.Utils.ServerUtils;

public class Main extends ApplicationAdapter {

    public SpriteBatch batch;
    public SceneSystem sceneSystem;
    public InputSystem inputSystem;
    public CollisionSystem collisionSystem;
    public ServerUtils serverUtils;

    private static final String SERVER_HOST = "wss://bandera1.ieti.site:443";

    @Override
    public void create() {
        batch = new SpriteBatch();
        sceneSystem = new SceneSystem(800, 480);
        inputSystem = new InputSystem();
        Gdx.input.setInputProcessor(inputSystem);
        collisionSystem = new CollisionSystem();
        
        serverUtils = new ServerUtils(SERVER_HOST);
        serverUtils.connect();
    }

    @Override
    public void render() {
        //Input phase is aLready handled by the InputSystem
        //Collision phase
        collisionSystem.checkCollisions();
        collisionSystem.dispatchCollisionEvents();
        //Update phase
        sceneSystem.update();
        //Render phase
        sceneSystem.render(batch);
        // Clear input state
        InputSystem.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        sceneSystem.dispose();
    }

    @Override
    public void resize(int width, int height) {
        sceneSystem.resize(width, height);
    }

}
