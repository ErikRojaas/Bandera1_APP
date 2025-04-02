package oscar.medina.galvez.engine.GameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TextureRenderer extends Component {
    public Texture texture;
    public float offsetX = 0;
    public float offsetY = 0;

    public TextureRenderer(Texture texture) {
        this.texture = texture;
    }

    public TextureRenderer(Texture texture, float offsetX, float offsetY) {
        this.texture = texture;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public void render(SpriteBatch batch) {
        Vector2 position = gameObject.transform.position;
        batch.draw(texture,
            position.x - texture.getWidth() / 2f + offsetX,
            position.y - texture.getHeight() / 2f + offsetY,
            texture.getWidth() / 2f,
            texture.getHeight() / 2f,
            texture.getWidth(),
            texture.getHeight(),
            gameObject.transform.scale.x,
            gameObject.transform.scale.y,
            gameObject.transform.rotation,
            0, 0,
            texture.getWidth(),
            texture.getHeight(),
            false, false);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
