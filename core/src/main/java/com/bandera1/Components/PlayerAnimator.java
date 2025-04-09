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

    private static final int FRAME_WIDTH = 160;  // Cambiar según el tamaño real de cada frame
    private static final int FRAME_HEIGHT = 160;

    private final Map<Action, TextureRegion[][]> animationsRaw = new HashMap<>();
    private final Map<Action, Map<Direction, Animation<TextureRegion>>> animations = new HashMap<>();

    public PlayerAnimator() {
        loadSpriteSheet(Action.IDLE, "Character/Char1_Idle.png");
        loadSpriteSheet(Action.WALK, "Character/Char1_Walk.png");
        loadSpriteSheet(Action.CARRY_IDLE, "Character/Char1_Carry_Idle.png");
        loadSpriteSheet(Action.CARRY_WALK, "Character/Char1_Carry_Walk.png");
        loadSpriteSheet(Action.ATTACK, "Character/Char1_Attack.png");
        loadSpriteSheet(Action.DEATH, "Character/Char1_Death.png");

        generateAnimations();
    }

    private void loadSpriteSheet(Action action, String path) {
        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Split the texture into a 2D array of frames
        // First dimension is rows (directions: down, left, right, up)
        // Second dimension is columns (animation frames)
        TextureRegion[][] frames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);

        // Debug output to check dimensions
        Gdx.app.debug("PlayerAnimator", "Loaded " + path + " with " + frames.length +
                      " rows and " + (frames.length > 0 ? frames[0].length : 0) + " columns");

        animationsRaw.put(action, frames);
    }

    private void generateAnimations() {
        for (Action action : animationsRaw.keySet()) {
            TextureRegion[][] sheet = animationsRaw.get(action);
            Map<Direction, Animation<TextureRegion>> map = new HashMap<>();

            // Check if we have enough rows in the sheet
            if (sheet.length >= 4) {
                map.put(Direction.DOWN, createAnimation(sheet[0]));
                map.put(Direction.UP, createAnimation(sheet[1]));
                map.put(Direction.LEFT, createAnimation(sheet[2]));
                map.put(Direction.RIGHT, createAnimation(sheet[3]));
            } else {
                Gdx.app.error("PlayerAnimator", "Sprite sheet for " + action +
                              " doesn't have enough rows (" + sheet.length + ")");

                // Use what's available or create empty animations
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
