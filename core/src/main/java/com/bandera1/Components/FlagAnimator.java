package com.bandera1.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

    public class FlagAnimator {

        private static final int FRAME_WIDTH = 60;  // Ajusta si el tamaño del frame es diferente
        private static final int FRAME_HEIGHT = 60;
        private static final float FRAME_DURATION = 0.1f;

        private Animation<TextureRegion> animation;

        public FlagAnimator() {
            Texture keyTexture = new Texture(Gdx.files.internal("flag_anim.png")); // Usa el nombre correcto
            keyTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

            TextureRegion[][] tmp = TextureRegion.split(keyTexture, FRAME_WIDTH, FRAME_HEIGHT);
            TextureRegion[] frames = tmp[0]; // Primera fila

            Array<TextureRegion> animationFrames = new Array<>(frames);
            animation = new Animation<>(FRAME_DURATION, animationFrames, Animation.PlayMode.LOOP);
        }

        public Animation<TextureRegion> getAnimation() {
            return animation;
        }
    }
