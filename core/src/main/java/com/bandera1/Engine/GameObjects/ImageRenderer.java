package com.bandera1.Engine.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.bandera1.Engine.Systems.SceneSystem;

public class ImageRenderer extends Component {
    private Texture texture;
    private float width;
    private float height;
    private float screenX;
    private float screenY;
    private boolean useScreenSpace = false;

    public ImageRenderer(Texture texture, float width, float height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    public void setScreenPosition(float x, float y) {
        this.screenX = x;
        this.screenY = y;
        this.useScreenSpace = true;
    }

    public void useWorldSpace() {
        this.useScreenSpace = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (useScreenSpace) {
            Matrix4 originalMatrix = batch.getProjectionMatrix().cpy();

            batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, SceneSystem.width, SceneSystem.height));

            batch.draw(
                texture,
                screenX - width / 5f,
                screenY - height / 5f,
                width,
                height
            );

            batch.setProjectionMatrix(originalMatrix);
        } else {
            batch.draw(
                texture,
                screenX - width / 1.5f,
                screenY - height / 1.5f,
                width,
                height
            );
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public boolean isTouched(float touchX, float touchY) {
        float minX = screenX - width / 1.5f;
        float maxX = screenX + width / 1.5f;
        float minY = screenY - height / 1.5f;
        float maxY = screenY + height / 1.5f;

        return useScreenSpace &&
               touchX >= minX && touchX <= maxX &&
               touchY >= minY && touchY <= maxY;
    }
}
