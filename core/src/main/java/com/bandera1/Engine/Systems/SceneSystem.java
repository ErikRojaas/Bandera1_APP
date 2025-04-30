package com.bandera1.Engine.Systems;

import com.bandera1.SceneIndex;
import com.bandera1.Engine.GameObjects.*;
import com.bandera1.Utils.KeyboardHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;


public class SceneSystem {
    public static Map<String, Scene> scenes;
    public static Scene activeScene;
    public static List<GameObject> newGameObjects;
    public static FitViewport viewport;
    public static OrthographicCamera camera;
    public static float zoom = 1;
    public static int width = 800;
    public static int height = 480;

    private static KeyboardHelper keyboardHelper;

    public SceneSystem(int w, int h, KeyboardHelper keyboardHelper) {
        width = w;
        height = h;
        scenes = new HashMap<>();
        viewport = new FitViewport(width, height);
        camera = new OrthographicCamera(width, height);
        camera.position.set(0, 0, 0);
        this.keyboardHelper = keyboardHelper;
        newGameObjects = new ArrayList<>();
        SceneIndex.addAllScenes();
    }

    public static void showKeyboard() {
        if (keyboardHelper != null) {
            keyboardHelper.showKeyboard();
        }
    }

    public static void hideKeyboard() {
        if (keyboardHelper != null) {
            keyboardHelper.hideKeyboard();
        }
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
        Gdx.gl.glClearColor(0, 0, 0, 1); // Black background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setProjectionMatrix(SceneSystem.camera.combined);
        if (activeScene != null) {
            activeScene.render(batch);
        }
        batch.end();
    }

    public static void addScene(String name, Scene scene) {
        scenes.put(name, scene);
        if (scenes.size() == 1) {
            activeScene = scene;
        }
    }

    public static void removeScene(String name) {
        scenes.remove(name);
        if (scenes.size() == 0) {
            activeScene = null;
        }
    }

    public static void changeScene(String name) {
        if (scenes.containsKey(name)) {
            MoveCameraTo(0, 0);
            activeScene = scenes.get(name);
            newGameObjects.clear();
            newGameObjects.addAll(activeScene.gameObjects);
        }
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
        
        for (GameObject gameObject : newObjects) {
            if (activeScene != null) {
                activeScene.addGameObject(gameObject); 
            }
            for (Component component : gameObject.components) {
                component.start(); 
            }
        }
        
        if (activeScene != null) {
            activeScene.update();
        }
    }

    public void dispose() {
        for (Scene scene : scenes.values()) {
            scene.dispose();
        }
    }

    public void resize(int w, int h) {
        width = w;
        height = h;
        viewport.update(width, height, true);
        camera.update();
    }
}
