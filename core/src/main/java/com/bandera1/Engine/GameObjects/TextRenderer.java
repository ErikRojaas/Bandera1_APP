package com.bandera1.Engine.GameObjects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextRenderer extends Component {
    public BitmapFont font;
    public String text;
    public float offsetX = 0;
    public float offsetY = 0;

    public TextRenderer(BitmapFont font, String text) {
        this.font = font;
        this.text = text;
    }

    public TextRenderer(String text) {
        this.text = text;
        this.font = new BitmapFont();
    }

    @Override
    public void render(SpriteBatch batch) {
        GlyphLayout layout = new GlyphLayout(font, text);

        font.draw(
            batch,
            text,
            gameObject.transform.position.x - layout.width / 2f + offsetX,
            gameObject.transform.position.y + layout.height / 2f + offsetY // Y is from baseline, not bottom
        );
    }
}
