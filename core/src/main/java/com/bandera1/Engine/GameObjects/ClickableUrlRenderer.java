package com.bandera1.Engine.GameObjects;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bandera1.Engine.Systems.InputSystem;

public class ClickableUrlRenderer extends TextRenderer {

    public String url;
    private Rectangle bounds;
    private GlyphLayout layout;

    public ClickableUrlRenderer(BitmapFont font, String text, String url) {
        super(font, text);
        this.url = url;
        this.bounds = new Rectangle();
        this.layout = new GlyphLayout();
    }

    public ClickableUrlRenderer(String text, String url) {
        super(text);
        this.url = url;
        this.bounds = new Rectangle();
        this.layout = new GlyphLayout();
    }

    @Override
    public void setScreenPosition(float x, float y) {
        super.setScreenPosition(x, y);
    }

    @Override
    public void useWorldSpace() {
        super.useWorldSpace();
    }

    @Override
    public void update() {
        if (InputSystem.onTouchDown(Input.Buttons.LEFT)) {
            Vector2 touchPos = InputSystem.getTouchPosition();
            float touchX = touchPos.x;
            float touchY = touchPos.y;
            Gdx.app.log("ClickableUrlRenderer", "Touch down at " + touchX + ", " + touchY);
            Gdx.app.log("ClickableUrlRenderer", "Bounds: " + bounds);
            if (bounds.contains(touchX, touchY)) {
                Gdx.net.openURI(url);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        if (font != null && text != null) {
             layout.setText(font, text);
             float w = layout.width;
             float h = layout.height;
             float x, y;
             if (useScreenSpace) {
                 x = screenX - w / 2f + offsetX;
                 y = screenY - h / 2f + offsetY;
             } else {
                 x = gameObject.transform.position.x - w / 2f + offsetX;
                 y = gameObject.transform.position.y - h / 2f + offsetY;
             }
            bounds.set(x, y, w, h);
        } else {
             bounds.set(0, 0, 0, 0);
        }
    }
}
