package oscar.medina.galvez.engine.GameObjects;
import java.util.List;
import java.util.UUID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import oscar.medina.galvez.engine.Systems.SceneSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class GameObject implements Serializable {
    public String name;
    public UUID id;
    public boolean enabled;
    public Transform transform;
    public List<Component> components;

    public GameObject(String name) {
        this.name = name;
        id = UUID.randomUUID();
        enabled = true;
        components = new ArrayList<>();
        transform = new Transform();
        components.add(transform);
    }

    public GameObject(Texture texture) {
        id = UUID.randomUUID();
        enabled = true;
        components = new ArrayList<>();
        transform = new Transform();
        components.add(transform);
    }

    public final void render(SpriteBatch batch) {
        for (Component component : components) {
            if (component.enabled) {
                component.render(batch);
            }
        }
    }

    public final void dispose() {
        for (Component component : components) {
            component.dispose();
        }
    }

    public final <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isInstance(component)) {
                return componentClass.cast(component);
            }
        }
        return null;
    }

    public final void addComponent(Component component) {
        components.add(component);
        component.gameObject = this;
    }

    public final void removeComponent(Component component) {
        components.remove(component);
        component.gameObject = null;
    }

    public GameObject deepClone() {
        try {
            // Serialize the object to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();
            byte[] byteData = bos.toByteArray();

            // Deserialize the byte array to a new object
            ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
            ObjectInputStream in = new ObjectInputStream(bis);
            return (GameObject) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error cloning object, make sure that all fields of components implement Serializable interface");
            e.printStackTrace();
            return null;
        }
    }

    public static void instantiate(GameObject gameObject, Vector2 position, float rotation) {
        GameObject clone = gameObject.deepClone();
        gameObject.transform.position = position;
        gameObject.transform.rotation = rotation;
        SceneSystem.activeScene.addGameObject(clone);
    }

    public static void instantiate(GameObject gameObject, float rotation) {
        GameObject clone = gameObject.deepClone();
        gameObject.transform.rotation = rotation;
        SceneSystem.activeScene.addGameObject(clone);
    }

    public static void instantiate(GameObject gameObject, Vector2 position) {
        GameObject clone = gameObject.deepClone();
        gameObject.transform.position = position;
        SceneSystem.activeScene.addGameObject(clone);
    }

    public static void instantiate(GameObject gameObject) {
        GameObject clone = gameObject.deepClone();
        SceneSystem.activeScene.addGameObject(clone);
    }

    public static GameObject Find(String name) {
        for (GameObject gameObject : SceneSystem.activeScene.gameObjects) {
            if (gameObject.name.equals(name)) {
                return gameObject;
            }
        }
        return null;
    }
}

