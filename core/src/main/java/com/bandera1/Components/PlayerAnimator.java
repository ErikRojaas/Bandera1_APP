package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;
import java.util.Map;

public class PlayerAnimator {

    public enum Action {
        IDLE, WALK, CARRY_IDLE, CARRY_WALK, ATTACK, DEATH
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private static final int FRAME_WIDTH = 80;  // Ajusta si cambia el tamaño real
    private static final int FRAME_HEIGHT = 80;

    private final Map<Action, TextureRegion[][]> animationsRaw = new HashMap<>();
    private final Map<Action, Map<Direction, Animation<TextureRegion>>> animations = new HashMap<>();


    // Constructor que permite cargar cualquier carpeta
    public PlayerAnimator(String basePath, String walkFile, String idleFile, String attackFile, String deathFile) {
        loadSpriteSheet(Action.IDLE, basePath + idleFile);
        loadSpriteSheet(Action.WALK, basePath + walkFile);
        loadSpriteSheet(Action.CARRY_IDLE, basePath + "Char_Carry_Idle.png"); // Opcional: puedes pasarlo también por parámetro
        loadSpriteSheet(Action.CARRY_WALK, basePath + "Char_Carry_Walk.png"); // Opcional
        loadSpriteSheet(Action.ATTACK, basePath + attackFile);
        loadSpriteSheet(Action.DEATH, basePath + deathFile);
    
        generateAnimations();
    }

    private void loadSpriteSheet(Action action, String path) {
        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegion[][] frames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);

        Gdx.app.debug("PlayerAnimator", "Loaded " + path + " with " + frames.length +
                      " rows and " + (frames.length > 0 ? frames[0].length : 0) + " columns");

        animationsRaw.put(action, frames);
    }

    private void generateAnimations() {
        for (Action action : animationsRaw.keySet()) {
            TextureRegion[][] sheet = animationsRaw.get(action);
            Map<Direction, Animation<TextureRegion>> map = new HashMap<>();

            if (sheet.length >= 4) {
                map.put(Direction.DOWN, createAnimation(sheet[0]));
                map.put(Direction.UP, createAnimation(sheet[1]));
                map.put(Direction.LEFT, createAnimation(sheet[2]));
                map.put(Direction.RIGHT, createAnimation(sheet[3]));
            } else {
                Gdx.app.error("PlayerAnimator", "Sprite sheet for " + action +
                              " doesn't have enough rows (" + sheet.length + ")");
                if (sheet.length > 0) map.put(Direction.DOWN, createAnimation(sheet[0]));
                if (sheet.length > 1) map.put(Direction.UP, createAnimation(sheet[1]));
                if (sheet.length > 2) map.put(Direction.LEFT, createAnimation(sheet[2]));
                if (sheet.length > 3) map.put(Direction.RIGHT, createAnimation(sheet[3]));
            }

            animations.put(action, map);
        }
    }

    private Animation<TextureRegion> createAnimation(TextureRegion[] frames) {
        Array<TextureRegion> array = new Array<>(frames);
        return new Animation<>(0.1f, array, Animation.PlayMode.LOOP);
    }

    public Animation<TextureRegion> getAnimation(Action action, Direction direction) {
        if (animations.containsKey(action)) {
            return animations.get(action).get(direction);
        }
        return null;
    }
}
