package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.Component;
import com.bandera1.Engine.GameObjects.GameObject;
import com.bandera1.Engine.GameObjects.Collider;
import com.bandera1.Engine.Systems.InputSystem;

import java.util.List;
import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;

public class ObjectHideShowButton extends Component {
    public List<GameObject> hideObjects = new ArrayList<>();
    private List<String> hideObjectsNames;
    public List<GameObject> showObjects = new ArrayList<>();
    private List<String> showObjectsNames;
    public boolean hideSelf = false;
    public Collider collider;

    public ObjectHideShowButton(List<String> hideObjectsNames, List<String> showObjectsNames, boolean hideSelf) {
        this.hideObjectsNames = hideObjectsNames;
        this.showObjectsNames = showObjectsNames;
        this.hideSelf = hideSelf;
    }

    public ObjectHideShowButton(List<String> hideObjectsNames, List<String> showObjectsNames) {
        this.hideObjectsNames = hideObjectsNames;
        this.showObjectsNames = showObjectsNames;
    }

    public void setHideSelf(boolean hideSelf) {
        this.hideSelf = hideSelf;
    }

    @Override
    public void start() {
        collider = gameObject.getComponent(Collider.class);
        if (collider == null) {
            throw new RuntimeException("ObjectHideShowButton component requires a Collider component on the same GameObject");
        }
        for (String name : hideObjectsNames) {
            GameObject ob = GameObject.Find(name);
            if (ob == null) continue;
            hideObjects.add(ob);
        }
        for (String name : showObjectsNames) {
            GameObject ob = GameObject.Find(name);
            if (ob == null) continue;
            showObjects.add(ob);
        }
    }

    @Override
    public void update() {
        if (InputSystem.onTouchDown(0)) {
            float x = InputSystem.getTouchX();
            float y = InputSystem.getTouchY();

            if (collider.isInside(new Vector2(x, y))) {
                hideObjects.forEach(GameObject::hide);
                showObjects.forEach(GameObject::show);
                if (hideSelf) {
                    gameObject.hide();
                }
            }
        }
    }
}
