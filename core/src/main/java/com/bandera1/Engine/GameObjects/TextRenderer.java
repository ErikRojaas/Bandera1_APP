package com.bandera1.Engine.GameObjects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.bandera1.Engine.Systems.SceneSystem;
import com.badlogic.gdx.math.Matrix4;

public class TextRenderer extends Component {
    public BitmapFont font;
    public String text;
    public float offsetX = 0;
    public float offsetY = 0;
    public boolean useScreenSpace = false;
    public float screenX = 0;
    public float screenY = 0;

    public TextRenderer(BitmapFont font, String text) {
        this.font = font;
        this.text = text;
    }

    public TextRenderer(String text) {
        this.text = text;
        this.font = new BitmapFont();
    }
    
    public void setText(String text) {
        this.text = text;
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
        GlyphLayout layout = new GlyphLayout(font, text);
        font.setUseIntegerPositions(false);
        font.getData().setScale(gameObject.transform.scale.x, gameObject.transform.scale.y);
        
        if (useScreenSpace) {
            // Save the current projection matrix
            Matrix4 originalMatrix = batch.getProjectionMatrix().cpy();
            
            // Use the identity matrix for screen space rendering
            batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, 
                SceneSystem.width, SceneSystem.height));
            
            // Draw text in screen coordinates
            font.draw(
                batch,
                text,
                screenX - layout.width / 2f + offsetX,
                screenY + layout.height / 2f + offsetY
            );
            
            // Restore the original projection matrix for world space rendering
            batch.setProjectionMatrix(originalMatrix);
        } else {
            // Render in world space coordinates (original behavior)
            font.draw(
                batch,
                text,
                gameObject.transform.position.x - layout.width / 2f + offsetX,
                gameObject.transform.position.y + layout.height / 2f + offsetY // Y is from baseline, not bottom
            );
        }
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
