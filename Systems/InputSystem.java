package oscar.medina.galvez.engine.Systems;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import oscar.medina.galvez.engine.Systems.SceneSystem;
import java.util.HashSet;
import java.util.Set;

public class InputSystem implements InputProcessor {
    private static Set<Integer> keysDown = new HashSet<>();
    private static Set<Integer> keysUp = new HashSet<>();
    private static Set<Integer> keys = new HashSet<>();

    private static boolean isTouching = false;
    private static boolean justTouched = false;
    private static boolean justReleased = false;
    private static float touchX = -1, touchY = -1;
    private static int touchButton = -1;

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
        isTouching = true;
        justTouched = true;
        touchX = screenX;
        touchY = screenY;
        touchButton = button;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isTouching = false;
        justReleased = true;
        touchButton = button;
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        isTouching = false;
        justReleased = true;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
        if (!isTouching)
            touchButton = -1;
        justTouched = false;
        justReleased = false;


        mouseMoved = false;
        scrolled = false;
        scrollX = 0;
        scrollY = 0;
    }

    @Override
    public boolean keyTyped(char character) {
       return true;
    }
}
