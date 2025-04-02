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
        background.transform.position = new Vector2(0, 0);
        background.transform.scale = new Vector2(1, 1);
        Texture backgroundTexture = new Texture("desert.png");
        for (int i = -1 ; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                background.addComponent(new TextureRenderer(backgroundTexture, i * 2400, j * 1600));
            }
        }
        addGameObject(background);

        Texture texture = new Texture("spritesheet.png");
        TextureRegion[][] spriteSheet = TextureRegion.split(texture, 8*20, 8*20);

        // Extract individual frames
        TextureRegion[] walkRigthFrames = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            walkRigthFrames[i] = spriteSheet[0][i];
        }

        TextureRegion[] walkDownFrames = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            walkDownFrames[i] = spriteSheet[1][i];
        }

        TextureRegion[] walkLeftFrames = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            walkLeftFrames[i] = spriteSheet[0][i];
        }

        TextureRegion[] walkUpFrames = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            walkUpFrames[i] = spriteSheet[2][i];
        }

        Animation<TextureRegion> walkRightAnimation = new Animation<>(0.1f, walkRigthFrames);
        Animation<TextureRegion> walkDownAnimation = new Animation<>(0.1f, walkDownFrames);
        Animation<TextureRegion> walkLeftAnimation = new Animation<>(0.1f, walkLeftFrames);
        Animation<TextureRegion> walkUpAnimation = new Animation<>(0.1f, walkUpFrames);

        AnimationRenderer animationRenderer = new AnimationRenderer();
        animationRenderer.addAnimation("rigth", walkRightAnimation);
        animationRenderer.addAnimation("down", walkDownAnimation);
        animationRenderer.addAnimation("left", walkLeftAnimation);
        animationRenderer.addAnimation("up", walkUpAnimation);
        animationRenderer.defaultTexture = spriteSheet[0][0];

        GameObject player = new GameObject("player");
        player.addComponent(animationRenderer);
        player.addComponent(new MovementController());
        addGameObject(player);
    }
}
