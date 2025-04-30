package com.bandera1.Engine.GameObjects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class UrlRenderer extends TextRenderer {

    public String url;

    public UrlRenderer(BitmapFont font, String text, String url) {
        super(font, text);
    }

    public UrlRenderer(String text, String url) {
        super(text);
    }


}
