package oscar.medina.galvez.engine.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TiledRenderer extends TextureRenderer {

    int repeatX;
    int repeatY;
    int offsetX;
    int offsetY;

    public TiledRenderer(Texture texture) {
        super(texture);
        repeatX = 1;
        repeatY = 1;
        offsetX = 0; // Initialize offsets if not provided
        offsetY = 0; // Initialize offsets if not provided
    }

    public TiledRenderer(Texture texture, int repeatX, int repeatY, int offsetX, int offsetY) {
        super(texture);
        this.repeatX = repeatX;
        this.repeatY = repeatY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public void render(SpriteBatch batch) {
        // Store the original center position of the GameObject
        // Use .cpy() if you might modify originalCenterPos later, but here it's read-only within the loops, so direct reference is fine.
        final float originalCenterX = gameObject.transform.position.x;
        final float originalCenterY = gameObject.transform.position.y;

        // --- Consider Scale for Tile Spacing (Optional but often desired) ---
        // If you want tiles spaced according to their scaled size:
        final float tileWidth = texture.getWidth() * gameObject.transform.scale.x;
        final float tileHeight = texture.getHeight() * gameObject.transform.scale.y;
        // If you want tiles spaced by their original texture size (ignore scale for spacing):
        // final float tileWidth = texture.getWidth();
        // final float tileHeight = texture.getHeight();
        // --- End Scale Consideration ---


        for (int i = -offsetX; i < repeatX - offsetX; i++) {
            for (int j = -offsetY; j < repeatY - offsetY; j++) {

                // Calculate the CENTER position for THIS specific tile
                float currentTileCenterX = originalCenterX + i * tileWidth;
                float currentTileCenterY = originalCenterY + j * tileHeight;

                // Calculate the bottom-left draw coordinates based on the tile's center
                float drawX = currentTileCenterX - texture.getWidth() / 2f; // Still use original texture width/height for origin offset
                float drawY = currentTileCenterY - texture.getHeight() / 2f; // Still use original texture width/height for origin offset

                // Draw the tile using the calculated position and the GameObject's scale/rotation
                batch.draw(texture,
                    drawX,
                    drawY,
                    texture.getWidth() / 2f,  // Origin X for rotation/scaling (center of the texture)
                    texture.getHeight() / 2f, // Origin Y for rotation/scaling (center of the texture)
                    texture.getWidth(),       // Width of the texture to draw
                    texture.getHeight(),      // Height of the texture to draw
                    gameObject.transform.scale.x, // Use the GameObject's scale
                    gameObject.transform.scale.y, // Use the GameObject's scale
                    gameObject.transform.rotation,// Use the GameObject's rotation
                    0, 0,                     // Source rectangle top-left corner (texture coordinates)
                    texture.getWidth(),       // Source rectangle width (texture coordinates)
                    texture.getHeight(),      // Source rectangle height (texture coordinates)
                    false, false);            // Flip flags
            }
        }
    }
}