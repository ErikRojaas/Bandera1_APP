package oscar.medina.galvez.engine.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;

public class AnimationRenderer extends Component {
    public Map<String, Animation<TextureRegion>> animations;
    public Animation<TextureRegion> currentAnimation;
    public float stateTime;
    public boolean isPlaying;
    public TextureRegion defaultTexture;
    public String currentAnimationName;
    public boolean flipX, flipY;

    public AnimationRenderer() {
        animations = new HashMap<>();
        stateTime = 0f;
        isPlaying = false;
        flipX = false;
        flipY = false;
    }

    public AnimationRenderer(TextureRegion defaultTexture) {
        this();
        this.defaultTexture = defaultTexture;
    }

    public void addAnimation(String name, Animation<TextureRegion> animation) {
        animations.put(name, animation);
    }

    public void play(String name) {
        if (!name.equals(currentAnimationName)) { // Prevent unnecessary resets
            Animation<TextureRegion> animation = animations.get(name);
            if (animation != null) {
                currentAnimation = animation;
                currentAnimationName = name;
                stateTime = 0f;
                isPlaying = true;
            }
        }
    }

    public void pause() {
        isPlaying = false;
    }

    public void stop() {
        isPlaying = false;
        stateTime = 0f;
        currentAnimationName = null;
    }

    public void setFlip(boolean flipX, boolean flipY) {
        this.flipX = flipX;
        this.flipY = flipY;
    }

    @Override
    public void update() {
        if (isPlaying && currentAnimation != null) {
            stateTime += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion frame = null;
        if (currentAnimation != null && isPlaying) {
            frame = currentAnimation.getKeyFrame(stateTime, true);
            Gdx.app.log("Animation", "frame: " + frame);
        } else if (defaultTexture != null) {
            frame = defaultTexture;
        }

        if (frame != null) {
            // Save original flip state
            boolean wasFlipX = frame.isFlipX();
            boolean wasFlipY = frame.isFlipY();

            // Apply flipping if needed
            if (frame.isFlipX() != flipX) frame.flip(true, false);
            if (frame.isFlipY() != flipY) frame.flip(false, true);

            // Match the exact parameters of the working version
            Vector2 position = gameObject.transform.position;
            batch.draw(
                frame,                                  // TextureRegion (not Texture)
                position.x - frame.getRegionWidth() / 2f,
                position.y - frame.getRegionHeight() / 2f,
                frame.getRegionWidth() / 2f,            // Origin X
                frame.getRegionHeight() / 2f,           // Origin Y
                frame.getRegionWidth(),                 // Width
                frame.getRegionHeight(),                // Height
                gameObject.transform.scale.x,           // Scale X
                gameObject.transform.scale.y,           // Scale Y
                gameObject.transform.rotation           // Rotation
            );

            // Restore original flip state if needed
            if (frame.isFlipX() != wasFlipX || frame.isFlipY() != wasFlipY) {
                frame.flip(frame.isFlipX() != wasFlipX, frame.isFlipY() != wasFlipY);
            }
        }
    }
}
