package oscar.medina.galvez.engine.Systems;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

import oscar.medina.galvez.engine.GameObjects.Collider;
import oscar.medina.galvez.engine.GameObjects.Component;
import oscar.medina.galvez.engine.GameObjects.Scene;
import oscar.medina.galvez.engine.GameObjects.GameObject;

public class CollisionSystem {
    private Set<CollisionPair> activeCollisions = new HashSet<>();
    public List<CollisionEvent> collisionEvents = new ArrayList<>();

    private class CollisionPair {
        GameObject a;
        GameObject b;

        CollisionPair(GameObject a, GameObject b) {
            if (a.id.compareTo(b.id) < 0) {
                this.a = a;
                this.b = b;
            } else {
                this.a = b;
                this.b = a;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CollisionPair)) return false;
            CollisionPair that = (CollisionPair) o;
            return a.equals(that.a) && b.equals(that.b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }
    public class CollisionEvent {
        public GameObject object;
        public GameObject other;
        public CollisionType type;

        public CollisionEvent(GameObject object, GameObject other, CollisionType type) {
            this.object = object;
            this.other = other;
            this.type = type;
        }
    }

    public enum CollisionType {
        ENTER, STAY, EXIT
    }

    public void checkCollisions() {
        Scene activeScene = SceneSystem.activeScene;
        Map<GameObject, Collider> colliders = new HashMap<>();

        // Collect all active colliders
        for (GameObject gameObject : activeScene.gameObjects) {
            if (!gameObject.enabled) continue;
            Collider collider = gameObject.getComponent(Collider.class);
            if (collider != null) {
                colliders.put(gameObject, collider);
            }
        }

        collisionEvents.clear();
        Set<CollisionPair> currentFrameCollisions = new HashSet<>();

        // Convert colliders to a list for indexed access
        List<Map.Entry<GameObject, Collider>> colliderList = new ArrayList<>(colliders.entrySet());

        // Check each pair only once using indexed access
        for (int i = 0; i < colliderList.size(); i++) {
            GameObject obj = colliderList.get(i).getKey();
            Collider collider = colliderList.get(i).getValue();

            for (int j = i + 1; j < colliderList.size(); j++) {
                GameObject otherObj = colliderList.get(j).getKey();
                Collider otherCollider = colliderList.get(j).getValue();

                CollisionPair pair = new CollisionPair(obj, otherObj);

                if (collider.collidesWith(otherCollider)) {
                    currentFrameCollisions.add(pair);

                    if (activeCollisions.contains(pair)) {
                        // STAY events
                        collisionEvents.add(new CollisionEvent(obj, otherObj, CollisionType.STAY));
                        collisionEvents.add(new CollisionEvent(otherObj, obj, CollisionType.STAY));
                    } else {
                        // ENTER events
                        collisionEvents.add(new CollisionEvent(obj, otherObj, CollisionType.ENTER));
                        collisionEvents.add(new CollisionEvent(otherObj, obj, CollisionType.ENTER));
                    }
                }
            }
        }

        // Check for collisions that ended this frame
        for (CollisionPair pair : activeCollisions) {
            if (!currentFrameCollisions.contains(pair)) {
                // EXIT events
                collisionEvents.add(new CollisionEvent(pair.a, pair.b, CollisionType.EXIT));
                collisionEvents.add(new CollisionEvent(pair.b, pair.a, CollisionType.EXIT));
            }
        }

        activeCollisions = currentFrameCollisions;
    }

    public void dispatchCollisionEvents() {
        for (CollisionEvent event : collisionEvents) {
            switch (event.type) {
                case ENTER:
                    for (Component component : event.object.components) {
                        if (component.enabled) {
                            component.onCollisionEnter(event.other);
                        }
                    }
                    break;
                case STAY:
                    for (Component component : event.object.components) {
                        if (component.enabled) {
                            component.onCollisionStay(event.other);
                        }
                    }
                    break;
                case EXIT:
                    for (Component component : event.object.components) {
                        if (component.enabled) {
                            component.onCollisionExit(event.other);
                        }
                    }
                    break;
            }
        }
    }
}