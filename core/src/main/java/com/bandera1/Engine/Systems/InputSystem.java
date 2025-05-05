package com.bandera1.Engine.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InputSystem implements InputProcessor {
    private static Set<Integer> keysDown = new HashSet<>();
    private static Set<Integer> keysUp = new HashSet<>();
    private static Set<Integer> keys = new HashSet<>();
    private static Set<Character> keysTyped = new HashSet<>();

    // New multi-touch tracking structures
    private static Map<Integer, Vector2> currentTouches = new HashMap<>(); // pointerId -> position
    private static Map<Integer, Integer> currentButtons = new HashMap<>(); // pointerId -> button
    private static Set<Integer> justDownPointers = new HashSet<>(); // Pointers that went down this frame
    private static Set<Integer> justUpPointers = new HashSet<>();   // Pointers that went up this frame
    private static Map<Integer, Integer> justUpPointerButtons = new HashMap<>(); // Keep track of buttons for pointers that went up this frame

    private static float mouseX = -1, mouseY = -1;
    private static float scrollX = 0, scrollY = 0;
    private static boolean mouseMoved = false;
    private static boolean scrolled = false;

    // Key input methods
    public static boolean onKeyDown(int keycode) {
        return keysDown.contains(keycode);
    }

    public static boolean onKeyUp(int keycode) {
        return keysUp.contains(keycode);
    }

    public static boolean onKey(int keycode) {
        return keys.contains(keycode);
    }

    public static Set<Integer> getKeys() {
        return keys;
    }

    public static Set<Integer> getKeysDown() {
        return keysDown;
    }

    public static Set<Integer> getKeysUp() {
        return keysUp;
    }

    public static Set<Character> getKeysTyped() {
        return keysTyped;
    }

    // --- Legacy Touch input methods (for backward compatibility) ---

    /** @return True if any finger just touched the screen this frame. */
    public static boolean onTouchDown() {
        return !justDownPointers.isEmpty();
    }

    /** @return True if any finger is currently touching the screen. */
    public static boolean onTouch() {
        return !currentTouches.isEmpty();
    }

    /** @return True if any finger was just released from the screen this frame. */
    public static boolean onTouchUp() {
        return !justUpPointers.isEmpty();
    }

    /** @return True if any finger just touched the screen with the specified button this frame. */
    public static boolean onTouchDown(int button) {
        if (justDownPointers.isEmpty()) return false;
        for (int pointer : justDownPointers) {
            if (currentButtons.getOrDefault(pointer, -1) == button) {
                return true;
            }
        }
        return false;
    }

    /** @return True if any finger is currently touching the screen with the specified button. */
    public static boolean onTouch(int button) {
        if (currentTouches.isEmpty()) return false;
        for (int pointer : currentTouches.keySet()) {
             if (currentButtons.getOrDefault(pointer, -1) == button) {
                return true;
            }
        }
        return false;
    }

    /** @return True if any finger was just released from the screen with the specified button this frame. */
    public static boolean onTouchUp(int button) {
         if (justUpPointers.isEmpty()) return false;
         for (int pointer : justUpPointers) {
             if (justUpPointerButtons.getOrDefault(pointer, -1) == button) {
                 return true;
             }
         }
         return false;
    }

    /** @return The world coordinates of the touch with the lowest pointer ID, or (-1, -1) if no fingers are touching. */
    public static Vector2 getTouchPosition() {
        if (currentTouches.isEmpty()) {
            // Return a default value consistent with previous behavior if possible
            // Be cautious as (-1, -1) screen coordinates might map to valid world coordinates
             return SceneSystem.ScreenToWorldPoint(new Vector2(-1, -1)); // Or consider returning null
        }
        int minPointer = Collections.min(currentTouches.keySet());
        return SceneSystem.ScreenToWorldPoint(currentTouches.get(minPointer));
    }

    /** @return The world X coordinate of the touch with the lowest pointer ID, or a default value if no fingers are touching. */
    public static float getTouchX() {
        return getTouchPosition().x; // Assumes getTouchPosition handles the "no touch" case
    }

    /** @return The world Y coordinate of the touch with the lowest pointer ID, or a default value if no fingers are touching. */
    public static float getTouchY() {
        return getTouchPosition().y; // Assumes getTouchPosition handles the "no touch" case
    }

    /** @return The button associated with the touch with the lowest pointer ID, or -1 if no fingers are touching. */
    public static int getTouchButton() {
        if (currentTouches.isEmpty()) {
            return -1;
        }
        int minPointer = Collections.min(currentTouches.keySet());
        return currentButtons.getOrDefault(minPointer, -1);
    }

    // --- New Multi-Touch input methods ---

    /** @return True if the specified pointer just touched the screen this frame. */
    public static boolean isPointerJustDown(int pointer) {
        return justDownPointers.contains(pointer);
    }

    /** @return True if the specified pointer is currently touching the screen. */
    public static boolean isPointerDown(int pointer) {
        return currentTouches.containsKey(pointer);
    }

     /** @return True if the specified pointer was just released from the screen this frame. */
    public static boolean isPointerJustUp(int pointer) {
        return justUpPointers.contains(pointer);
    }

    /** @return The world coordinates for the specified pointer, or null if the pointer is not down. */
    public static Vector2 getPointerPosition(int pointer) {
        Vector2 screenPos = currentTouches.get(pointer);
        return (screenPos != null) ? SceneSystem.ScreenToWorldPoint(screenPos) : null;
    }

    /** @return The button associated with the specified pointer, or -1 if the pointer is not down. */
    public static int getPointerButton(int pointer) {
        return currentButtons.getOrDefault(pointer, -1);
    }

    /** @return The set of currently active pointer IDs. */
    public static Set<Integer> getActivePointers() {
        return Collections.unmodifiableSet(currentTouches.keySet());
    }

     /** @return The set of pointer IDs that just touched down this frame. */
    public static Set<Integer> getJustDownPointers() {
        return Collections.unmodifiableSet(justDownPointers);
    }

    /** @return The set of pointer IDs that were just released this frame. */
    public static Set<Integer> getJustUpPointers() {
         return Collections.unmodifiableSet(justUpPointers);
    }

    public static List<Vector2> getCurrentTouches() {
        List<Vector2> touches = new ArrayList<Vector2>();
        for (int pointer : currentTouches.keySet()) {
            touches.add(currentTouches.get(pointer));
        }
        return touches;
    }

    // Mouse movement methods
    public static boolean onMouseMoved() {
        return mouseMoved;
    }

    public static Vector2 getMousePosition() {
        return SceneSystem.ScreenToWorldPoint(new Vector2(mouseX, mouseY));
    }

    public static float getMouseX() {
        return SceneSystem.ScreenToWorldPoint(new Vector2(mouseX, mouseY)).x;
    }

    public static float getMouseY() {
        return SceneSystem.ScreenToWorldPoint(new Vector2(mouseX, mouseY)).y;
    }

    // Scroll methods
    public static boolean onScrolled() {
        return scrolled;
    }

    public static float getScrollX() {
        return scrollX;
    }

    public static float getScrollY() {
        return scrollY;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!keys.contains(keycode)) {
            keysDown.add(keycode);
            keys.add(keycode);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keysUp.add(keycode);
        keys.remove(keycode);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("Touch", "Touch down: " + pointer + ", " + button);
        justDownPointers.add(pointer);
        currentTouches.put(pointer, new Vector2(screenX, screenY));
        currentButtons.put(pointer, button);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (currentTouches.containsKey(pointer)) {
            justUpPointers.add(pointer);
            justUpPointerButtons.put(pointer, currentButtons.getOrDefault(pointer, -1));
            currentTouches.remove(pointer);
            currentButtons.remove(pointer);
        }
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        if (currentTouches.containsKey(pointer)) {
            justUpPointers.add(pointer);
            justUpPointerButtons.put(pointer, currentButtons.getOrDefault(pointer, -1));
            currentTouches.remove(pointer);
            currentButtons.remove(pointer);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (currentTouches.containsKey(pointer)) {
            currentTouches.get(pointer).set(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseMoved = true;
        mouseX = screenX;
        mouseY = screenY;
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        scrolled = true;
        scrollX = amountX;
        scrollY = amountY;
        return true;
    }

    public static void update() {
        keysDown.clear();
        keysUp.clear();
        keysTyped.clear();

        justDownPointers.clear();
        justUpPointers.clear();
        justUpPointerButtons.clear();

        mouseMoved = false;
        scrolled = false;
        scrollX = 0;
        scrollY = 0;
    }

    @Override
    public boolean keyTyped(char character) {
        keysTyped.add(character);
        return true;
    }
}
