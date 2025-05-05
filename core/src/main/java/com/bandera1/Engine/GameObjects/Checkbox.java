package com.bandera1.Engine.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bandera1.Engine.Systems.InputSystem;

public class Checkbox extends Component {
    private Texture checkedTexture;
    private Texture uncheckedTexture;
    private boolean isChecked = false;
    public float offsetX = 0;
    public float offsetY = 0;

    public Checkbox(Texture uncheckedTexture, Texture checkedTexture) {
        this.uncheckedTexture = uncheckedTexture;
        this.checkedTexture = checkedTexture;
    }

    public Checkbox(Texture uncheckedTexture, Texture checkedTexture, boolean initialState) {
        this.uncheckedTexture = uncheckedTexture;
        this.checkedTexture = checkedTexture;
        this.isChecked = initialState;
    }

    @Override
    public void update() {
        if (InputSystem.onTouchDown(0)) {
            Vector2 mousePos = InputSystem.getTouchPosition();
            Rectangle bounds = getBounds();

            if (bounds.contains(mousePos)) {
                isChecked = !isChecked;
                Gdx.app.log("Checkbox", "Checkbox state changed to: " + isChecked);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Texture currentTexture = isChecked ? checkedTexture : uncheckedTexture;
        if (currentTexture != null) {
            Vector2 position = gameObject.transform.position;
            Vector2 scale = gameObject.transform.scale;

            float width = currentTexture.getWidth() * scale.x;
            float height = currentTexture.getHeight() * scale.y;
            float originX = width / 2f;
            float originY = height / 2f;
            float drawX = position.x - originX + offsetX;
            float drawY = position.y - originY + offsetY;

            batch.draw(currentTexture,
                drawX,
                drawY,
                originX,
                originY,
                width,
                height,
                1, // Scale is applied to width/height directly
                1,
                gameObject.transform.rotation,
                0, 0,
                currentTexture.getWidth(),
                currentTexture.getHeight(),
                false, false);
        }
    }

    private Rectangle getBounds() {
        Vector2 position = gameObject.transform.position;
        Vector2 scale = gameObject.transform.scale;
        Texture texture = isChecked ? checkedTexture : uncheckedTexture;

        float width = texture.getWidth() * scale.x;
        float height = texture.getHeight() * scale.y;
        float x = position.x - width / 2f + offsetX;
        float y = position.y - height / 2f + offsetY;

        return new Rectangle(x, y, width, height);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    @Override
    public void dispose() {
        // The textures are likely managed elsewhere, so we don't dispose them here
    }
}