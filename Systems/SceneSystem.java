package oscar.medina.galvez.engine.Systems;

import oscar.medina.galvez.engine.GameObjects.Component;
import oscar.medina.galvez.engine.GameObjects.GameObject;
import oscar.medina.galvez.engine.GameObjects.Scene;
import oscar.medina.galvez.engine.Scenes.SceneIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class SceneSystem {
    public static List<Scene> scenes = new java.util.ArrayList<>();
    public static Scene activeScene;
    public static List<GameObject> newGameObjects;
    public static FitViewport viewport;
    public static OrthographicCamera camera;
    public static float zoom = 1;
    public static int width = 800;
    public static int height = 480;

    public SceneSystem(int w, int h) {
        width = w;
        height = h;
        viewport = new FitViewport(width, height);
        camera = new OrthographicCamera(width, height);
        camera.position.set(0, 0, 0);

        newGameObjects = new ArrayList<>();
        SceneIndex.addAllScenes();
    }

    public static Vector2 ScreenToWorldPoint(Vector2 screenPoint) {
        Vector3 v = camera.unproject(new Vector3(screenPoint.x, screenPoint.y, 0));
        return new Vector2(v.x, v.y);
    }

    public static Vector2 WorldToScreenPoint(Vector2 worldPoint) {
        Vector3 v = camera.project(new Vector3(worldPoint.x, worldPoint.y, 0));
        return new Vector2(v.x, v.y);
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.setProjectionMatrix(SceneSystem.camera.combined);
        if (activeScene != null) {
            activeScene.render(batch);
        }
        batch.end();
    }

    public static void addScene(Scene scene) {
        scenes.add(scene);
        if (scenes.size() == 1) {
            activeScene = scene;
        }
    }

    public static void removeScene(Scene scene) {
        scenes.remove(scene);
        if (scenes.size() == 0) {
            activeScene = null;
        }
    }

    public static void changeScene(Scene scene) {
        activeScene = scene;
    }

    public static void MoveCameraBy(float x, float y) {
        camera.position.x += x;
        camera.position.y += y;
    }

    public static void MoveCameraBy(Vector2 vector) {
        camera.position.x += vector.x;
        camera.position.y += vector.y;
    }

    public static void MoveCameraTo(float x, float y) {
        camera.position.x = x;
        camera.position.y = y;
    }

    public static void MoveCameraTo(Vector2 vector) {
        camera.position.x = vector.x;
        camera.position.y = vector.y;
    }

    public void update() {
        camera.update();
        viewport.update(width, height);
        List<GameObject> newObjects = new ArrayList<>(newGameObjects);
        newGameObjects.clear();
        for (GameObject gameObject : newObjects)
        for (Component component : gameObject.components)
            component.start();
        if (activeScene != null) {
            activeScene.update();
        }
    }

    public void dispose() {
        for (Scene scene : scenes) {
            scene.dispose();
        }
    }

    public void resize(int w, int h) {
        width = w;
        height = h;
        viewport.update(width, height, true);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }
}
