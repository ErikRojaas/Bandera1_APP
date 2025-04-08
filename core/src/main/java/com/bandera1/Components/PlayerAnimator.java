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

    private static final int FRAME_WIDTH = 64;  // Cambiar según el tamaño real de cada frame
    private static final int FRAME_HEIGHT = 64;

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
        int rows = 4;
        int cols = texture.getWidth() / FRAME_WIDTH;
        TextureRegion[][] frames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        animationsRaw.put(action, frames);
    }

    private void generateAnimations() {
        for (Action action : animationsRaw.keySet()) {
            TextureRegion[][] sheet = animationsRaw.get(action);
            Map<Direction, Animation<TextureRegion>> map = new HashMap<>();

            map.put(Direction.UP, createAnimation(sheet[0]));
            map.put(Direction.DOWN, createAnimation(sheet[1]));
            map.put(Direction.LEFT, createAnimation(sheet[2]));
            map.put(Direction.RIGHT, createAnimation(sheet[3]));

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
