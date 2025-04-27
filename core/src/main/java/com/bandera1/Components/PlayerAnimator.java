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
        DOWN, UP, LEFT, RIGHT
    }

    private static final int FRAME_WIDTH = 80;    // Ancho de cada frame
    private static final int FRAME_HEIGHT = 80;   // Alto de cada frame

    private final Map<Action, TextureRegion[][]> animationsRaw = new HashMap<>();
    private final Map<Action, Map<Direction, Animation<TextureRegion>>> animations = new HashMap<>();

    // Cache de texturas para no recargar la misma imagen varias veces
    private static final Map<String, Texture> textureCache = new HashMap<>();

    public PlayerAnimator(String basePath, String walkFile, String idleFile, String attackFile, String deathFile) {
        loadSpriteSheet(Action.IDLE, basePath + idleFile);
        loadSpriteSheet(Action.WALK, basePath + walkFile);
        loadSpriteSheet(Action.CARRY_IDLE, basePath + "Char_Carry_Idle.png");
        loadSpriteSheet(Action.CARRY_WALK, basePath + "Char_Carry_Walk.png");
        loadSpriteSheet(Action.ATTACK, basePath + attackFile);
        loadSpriteSheet(Action.DEATH, basePath + deathFile);

        generateAnimations();
    }

    private void loadSpriteSheet(Action action, String path) {
        Texture texture = textureCache.get(path);

        if (texture == null) {
            texture = new Texture(Gdx.files.internal(path));
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            textureCache.put(path, texture);
            Gdx.app.debug("PlayerAnimator", "Loaded and cached texture: " + path);
        } else {
            Gdx.app.debug("PlayerAnimator", "Reusing cached texture: " + path);
        }

        TextureRegion[][] frames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        animationsRaw.put(action, frames);
    }

    private void generateAnimations() {
        for (Action action : animationsRaw.keySet()) {
            TextureRegion[][] sheet = animationsRaw.get(action);
            Map<Direction, Animation<TextureRegion>> directionMap = new HashMap<>();

            if (sheet.length >= 4) {
                directionMap.put(Direction.DOWN, createAnimation(sheet[0]));
                directionMap.put(Direction.UP, createAnimation(sheet[1]));
                directionMap.put(Direction.LEFT, createAnimation(sheet[2]));
                directionMap.put(Direction.RIGHT, createAnimation(sheet[3]));
            } else {
                Gdx.app.error("PlayerAnimator", "Sprite sheet for " + action + " doesn't have enough rows (" + sheet.length + ")");
                if (sheet.length > 0) directionMap.put(Direction.DOWN, createAnimation(sheet[0]));
                if (sheet.length > 1) directionMap.put(Direction.UP, createAnimation(sheet[1]));
                if (sheet.length > 2) directionMap.put(Direction.LEFT, createAnimation(sheet[2]));
                if (sheet.length > 3) directionMap.put(Direction.RIGHT, createAnimation(sheet[3]));
            }

            animations.put(action, directionMap);
        }
    }

    private Animation<TextureRegion> createAnimation(TextureRegion[] frames) {
        Array<TextureRegion> frameArray = new Array<>(frames);
        return new Animation<>(0.1f, frameArray, Animation.PlayMode.LOOP);
    }

    public Animation<TextureRegion> getAnimation(Action action, Direction direction) {
        if (animations.containsKey(action)) {
            return animations.get(action).get(direction);
        }
        return null;
    }

    public static void disposeTextures() {
        for (Texture texture : textureCache.values()) {
            texture.dispose();
        }
        textureCache.clear();
        Gdx.app.debug("PlayerAnimator", "Disposed all cached textures.");
    }
}
