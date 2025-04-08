package com.bandera1.Components;

import com.bandera1.Engine.GameObjects.Component;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.Gdx;

public class PlayerRenderer extends Component {
    private PlayerAnimator animator;
    private PlayerAnimator.Action action;
    private PlayerAnimator.Direction direction;
    private float stateTime;

    @Override
    public void start() {
        animator = new PlayerAnimator();
        action = PlayerAnimator.Action.IDLE;
        direction = PlayerAnimator.Direction.DOWN;
        stateTime = 0f;
    }

    @Override
    public void update() {
        stateTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch batch) {
        Animation<TextureRegion> animation = animator.getAnimation(action, direction);
        if (animation != null) {
            TextureRegion currentFrame = animation.getKeyFrame(stateTime);
            float x = gameObject.transform.position.x;
            float y = gameObject.transform.position.y;
            float width = currentFrame.getRegionWidth();
            float height = currentFrame.getRegionHeight();

            batch.draw(currentFrame, x, y, width * gameObject.transform.scale.x, height * gameObject.transform.scale.y);
        }
    }

    // Setters
    public void setAction(PlayerAnimator.Action action) {
        this.action = action;
        this.stateTime = 0f;
    }

    public void setDirection(PlayerAnimator.Direction direction) {
        this.direction = direction;
    }
}
