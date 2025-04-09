package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class FlagAnimator {

    private static final int FRAME_WIDTH = 160;  // Ajusta si el tamaño del frame es diferente
    private static final int FRAME_HEIGHT = 160;
    private static final float FRAME_DURATION = 0.1f;

    private Animation<TextureRegion> animation;

    public FlagAnimator() {
        Texture texture = new Texture(Gdx.files.internal("flag.png")); // Asegúrate del nombre correcto
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegion[][] frames2D = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
        Array<TextureRegion> frames = new Array<>();

        for (TextureRegion[] row : frames2D) {
            for (TextureRegion frame : row) {
                frames.add(frame);
            }
        }

        animation = new Animation<>(FRAME_DURATION, frames, Animation.PlayMode.LOOP);
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }
}
