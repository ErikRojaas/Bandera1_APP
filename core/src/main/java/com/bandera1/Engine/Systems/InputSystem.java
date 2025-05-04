package com.bandera1.Engine.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import java.util.HashSet;
import java.util.Set;

public class InputSystem implements InputProcessor {
    private static Set<Integer> keysDown = new HashSet<>();
    private static Set<Integer> keysUp = new HashSet<>();
    private static Set<Integer> keys = new HashSet<>();
    private static Set<Character> keysTyped = new HashSet<>();

    private static boolean isTouching = false;
    private static boolean justTouched = false;
    private static boolean justReleased = false;
    private static float touchX = -1, touchY = -1;
    private static int touchButton = -1;

    private static float mouseX = -1, mouseY = -1;
    private static float scrollX = 0, scrollY = 0;
    private static boolean mouseMoved = false;
    private static boolean scrolled = false;

    // Multi-touch support
    private static class TouchInfo {
        boolean isTouching = false;
        boolean justTouched = false;
        boolean justReleased = false;
        float x = -1, y = -1;
        int button = -1;
    }
    private static java.util.Map<Integer, TouchInfo> touches = new java.util.HashMap<>();

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

    // Touch input methods
    public static boolean onTouchDown() {
        return justTouched;
    }

    public static boolean onTouch() {
        return isTouching;
    }

    public static boolean onTouchUp() {
        return justReleased;
    }

    public static boolean onTouchDown(int button) {
        return justTouched && touchButton == button;
    }

    public static boolean onTouch(int button) {
        return isTouching && touchButton == button;
    }

    public static boolean onTouchUp(int button) {
        return justReleased && touchButton == button;
    }

    public static Vector2 getTouchPosition() {
        return SceneSystem.ScreenToWorldPoint(new Vector2(touchX, touchY));
    }

    public static float getTouchX() {
        return SceneSystem.ScreenToWorldPoint(new Vector2(touchX, touchY)).x;
    }

    public static float getTouchY() {
        return SceneSystem.ScreenToWorldPoint(new Vector2(touchX, touchY)).y;
    }

    public static int getTouchButton() {
        return touchButton;
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

    // Multi-touch input methods
    public static boolean onTouchDown(int pointer) {
        TouchInfo info = touches.get(pointer);
        return info != null && info.justTouched;
    }
    public static boolean onTouch(int pointer) {
        TouchInfo info = touches.get(pointer);
        return info != null && info.isTouching;
    }
    public static boolean onTouchUp(int pointer) {
        TouchInfo info = touches.get(pointer);
        return info != null && info.justReleased;
    }
    public static Vector2 getTouchPosition(int pointer) {
        TouchInfo info = touches.get(pointer);
        if (info == null) return null;
        return SceneSystem.ScreenToWorldPoint(new Vector2(info.x, info.y));
    }
    public static int getTouchButton(int pointer) {
        TouchInfo info = touches.get(pointer);
        if (info == null) return -1;
        return info.button;
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
        // Multi-touch
        TouchInfo info = touches.get(pointer);
        if (info == null) info = new TouchInfo();
        info.isTouching = true;
        info.justTouched = true;
        info.justReleased = false;
        info.x = screenX;
        info.y = screenY;
        info.button = button;
        touches.put(pointer, info);
        // Old API (primary touch)
        isTouching = true;
        justTouched = true;
        touchX = screenX;
        touchY = screenY;
        touchButton = button;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Multi-touch
        TouchInfo info = touches.get(pointer);
        if (info == null) info = new TouchInfo();
        info.isTouching = false;
        info.justTouched = false;
        info.justReleased = true;
        info.x = screenX;
        info.y = screenY;
        info.button = button;
        touches.put(pointer, info);
        // Old API (primary touch)
        isTouching = false;
        justReleased = true;
        touchButton = button;
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        // Multi-touch
        TouchInfo info = touches.get(pointer);
        if (info == null) info = new TouchInfo();
        info.isTouching = false;
        info.justTouched = false;
        info.justReleased = true;
        info.x = screenX;
        info.y = screenY;
        info.button = button;
        touches.put(pointer, info);
        // Old API (primary touch)
        isTouching = false;
        justReleased = true;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Multi-touch
        TouchInfo info = touches.get(pointer);
        if (info == null) info = new TouchInfo();
        info.x = screenX;
        info.y = screenY;
        touches.put(pointer, info);
        // Old API (primary touch)
        touchX = screenX;
        touchY = screenY;
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
        if (!isTouching)
            touchButton = -1;
        justTouched = false;
        justReleased = false;
        // Multi-touch: reset per-pointer justTouched/justReleased
        for (TouchInfo info : touches.values()) {
            info.justTouched = false;
            info.justReleased = false;
            if (!info.isTouching) {
                info.button = -1;
            }
        }
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
