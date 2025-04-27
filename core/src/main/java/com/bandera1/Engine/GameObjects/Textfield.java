package com.bandera1.Engine.GameObjects;

import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bandera1.Engine.Systems.InputSystem;
import com.bandera1.Engine.Systems.SceneSystem;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.math.Matrix4;

public class Textfield extends Component {

    public String text = ""; // Initialize text
    public BitmapFont font;
    public Texture idleTexture;
    public Texture activeTexture;
    public float fontScale = 1;
    public float offsetX = 0;
    public float offsetY = 0;
    public float textOffsetX = 0;
    public float textOffsetY = 0;

    private boolean isActive = false;
    private GlyphLayout layout = new GlyphLayout(); // For text measurement


    public Textfield(Texture idle, Texture active) {
        this.font = new BitmapFont(); // Use default font
        this.idleTexture = idle;
        this.activeTexture = active;
    }

    public Textfield(BitmapFont font, Texture idle, Texture active) {
        this.font = font;
        this.idleTexture = idle;
        this.activeTexture = active;
    }

    @Override
    public void update() {
        Vector2 mousePos = InputSystem.getTouchPosition();
        boolean mouseClicked = InputSystem.onTouchDown(Input.Buttons.LEFT);

        Texture currentTexture = isActive ? activeTexture : idleTexture;
        Rectangle bounds = getBounds(currentTexture);

        if (mouseClicked) {
            if (bounds != null && bounds.contains(mousePos)) {
                isActive = true;
                Gdx.input.setOnscreenKeyboardVisible(true);
            } else {
                if (isActive) {
                    Gdx.input.setOnscreenKeyboardVisible(false);
                }
                isActive = false;
            }
        }

        if (isActive) {
            Set<Character> justTyped = InputSystem.getKeysTyped();
            for (char character : justTyped) {
                if (character == '\b' && text.length() > 0) {
                    text = text.substring(0, text.length() - 1);
                } else if (character >= 32 && character < 127) {
                    text += character;
                } // enter
                else if (character == 10) {
                    isActive = false;
                    Gdx.input.setOnscreenKeyboardVisible(false);
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Texture currentTexture = isActive ? activeTexture : idleTexture;
        Vector2 position = gameObject.transform.position;
        Vector2 scale = gameObject.transform.scale;
        Rectangle worldBounds = null; // To store world bounds

        // Draw background texture
        if (currentTexture != null) {
            float width = currentTexture.getWidth() * scale.x;
            float height = currentTexture.getHeight() * scale.y;
            float originX = width / 2f;
            float originY = height / 2f;
            float drawX = position.x - originX + offsetX;
            float drawY = position.y - originY + offsetY;

            // Store world bounds for clipping
            worldBounds = new Rectangle(drawX, drawY, width, height);

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

        // Draw text, clipped within bounds
        if (font != null && worldBounds != null) {
            // --- Clipping Start ---
            batch.flush(); // Flush batch before changing render state

            // *** Use the static camera from SceneSystem ***
            Camera camera = SceneSystem.camera; // Use the static camera field
            if (camera == null) {
                 // Handle error: Camera not found or not yet initialized
                 System.err.println("Textfield Error: SceneSystem.camera not available for clipping.");
                 // Draw unclipped text as fallback
                 drawUnclippedText(batch, position, scale);
                 return;
            }

            Rectangle scissors = new Rectangle();
            Matrix4 transformMatrix = batch.getTransformMatrix();
            ScissorStack.calculateScissors(camera, transformMatrix, worldBounds, scissors);

            boolean scissorsPushed = ScissorStack.pushScissors(scissors);
            // --- Clipping End ---

            // Prepare and draw text
            font.getData().setScale(fontScale); // Apply font scale
            layout.setText(font, text); // Update layout to measure text

            float textX = position.x - layout.width / 2f + offsetX + textOffsetX; // Center text horizontally
            float textY = position.y + layout.height / 2f + offsetY + textOffsetY; // Center text vertically

            font.draw(batch, layout, textX, textY);


            // --- Restore Rendering State ---
            if (scissorsPushed) {
                batch.flush(); // Flush before popping scissors
                ScissorStack.popScissors();
            }
            // --- End Restore ---
        } else if (font != null) {
             // Draw text without clipping if there's no background texture to define bounds
             drawUnclippedText(batch, position, scale);
        }
    }

    // Helper method to draw text without clipping (used as fallback or if no bounds)
    private void drawUnclippedText(SpriteBatch batch, Vector2 position, Vector2 scale) {
        font.getData().setScale(fontScale); // Apply font scale
        layout.setText(font, text); // Update layout to measure text
        float textX = position.x - layout.width / 2f + offsetX + textOffsetX; // Center text horizontally
        float textY = position.y + layout.height / 2f + offsetY + textOffsetY; // Center text vertically
        font.draw(batch, layout, textX, textY);
    }

    // Helper to get bounds based on the current texture
    private Rectangle getBounds(Texture texture) {
        if (texture == null || gameObject == null) return null;

        Vector2 position = gameObject.transform.position;
        Vector2 scale = gameObject.transform.scale;
        float width = texture.getWidth() * scale.x;
        float height = texture.getHeight() * scale.y;
        float x = position.x - width / 2f;
        float y = position.y - height / 2f;

        return new Rectangle(x, y, width, height);
    }

    @Override
    public void dispose() {
        if (font != null) font.dispose();
        if (idleTexture != null) idleTexture.dispose();
        if (activeTexture != null) activeTexture.dispose();
    }

}
