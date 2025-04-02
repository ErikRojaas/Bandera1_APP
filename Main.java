package oscar.medina.galvez.engine;



import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import oscar.medina.galvez.engine.Systems.SceneSystem;
import oscar.medina.galvez.engine.Systems.InputSystem;
import oscar.medina.galvez.engine.Systems.CollisionSystem;
import com.badlogic.gdx.Gdx;

public class Main extends ApplicationAdapter {

    public SpriteBatch batch;
    public SceneSystem sceneSystem;
    public InputSystem inputSystem;
    public CollisionSystem collisionSystem;

    @Override
    public void create() {
        batch = new SpriteBatch();
        sceneSystem = new SceneSystem(800, 480);
        inputSystem = new InputSystem();
        Gdx.input.setInputProcessor(inputSystem);
        collisionSystem = new CollisionSystem();
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
