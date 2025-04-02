package oscar.medina.galvez.engine.GameObjects;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import oscar.medina.galvez.engine.Systems.SceneSystem;

public class Scene {
    public List<GameObject> gameObjects;

    public Scene() {
        gameObjects = new ArrayList<>();
    }

    public Scene(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public void render(SpriteBatch batch) {
        for (GameObject gameObject : gameObjects) {
            if (!gameObject.enabled) continue;
            gameObject.render(batch);
        }
    }

    public void addGameObject(GameObject gameObject) {
        if (gameObjects.contains(gameObject)) return;
        gameObjects.add(gameObject);
        SceneSystem.newGameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    public void update() {
        List<GameObject> objects = new ArrayList<>(this.gameObjects);
        for (GameObject gameObject : objects) {
            if (!gameObject.enabled) continue;
            for (Component component : gameObject.components) {
                component.update();
            }
        }
    }

    public void dispose() {
        for (GameObject gameObject : gameObjects) {
            for (Component component : gameObject.components) {
                component.dispose();
            }
        }
    }

}