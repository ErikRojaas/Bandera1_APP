package oscar.medina.galvez.engine.Scenes;

import oscar.medina.galvez.engine.GameObjects.Scene;
import oscar.medina.galvez.engine.GameObjects.GameObject;
import oscar.medina.galvez.engine.GameObjects.TextureRenderer;
import oscar.medina.galvez.engine.Components.MovementController;
import oscar.medina.galvez.engine.GameObjects.AnimationRenderer;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

public class TestScene extends Scene {
    public TestScene() {
        super();
        GameObject background = new GameObject("background");
        Texture backgroundTexture = new Texture("background.png");
        background.addComponent(new TextureRenderer(backgroundTexture));
        addGameObject(background);
    }
}
