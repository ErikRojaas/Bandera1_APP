package com.bandera1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.bandera1.Utils.ServerMessage;
import com.bandera1.Utils.ServerUtils;
import com.bandera1.Utils.WebSocketEventListener;

public class MainMenuScreen implements Screen, WebSocketEventListener {

    private Stage stage;
    private SpriteBatch batch;
    private Animation<TextureRegion> backgroundAnimation;
    private Animation<TextureRegion> titleAnimation;
    private float stateTime;
    private Image animatedTitle;
    private Label playerCountLabel;

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Animación de fondo
        int frameCount = 10;
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < frameCount; i++) {
            Texture texture = new Texture(Gdx.files.internal("MainMenuBackground/frame" + i + ".png"));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            frames.add(new TextureRegion(texture));
        }
        backgroundAnimation = new Animation<TextureRegion>(0.1f, frames, Animation.PlayMode.LOOP);

        // Animación de título (por frames)
        int titleFrameCount = 15;
        Array<TextureRegion> titleFrames = new Array<>();
        for (int i = 0; i < titleFrameCount; i++) {
            Texture texture = new Texture(Gdx.files.internal("HomeScreenTitle/frame_" + i + ".png"));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            titleFrames.add(new TextureRegion(texture));
        }
        titleAnimation = new Animation<TextureRegion>(0.08f, titleFrames, Animation.PlayMode.LOOP);

        // Fuente personalizada para botón
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter buttonParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        buttonParam.size = 72;
        BitmapFont buttonFont = generator.generateFont(buttonParam);

        // Fuente personalizada para label
        FreeTypeFontGenerator.FreeTypeFontParameter labelParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        labelParam.size = 48;
        BitmapFont labelFont = generator.generateFont(labelParam);
        generator.dispose();

        // Estilo del botón
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;

        Pixmap pixmapUp = new Pixmap(60, 60, Pixmap.Format.RGBA8888);
        pixmapUp.setColor(0.2f, 0.4f, 0.8f, 1f);
        pixmapUp.fill();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(pixmapUp)));

        Pixmap pixmapDown = new Pixmap(60, 60, Pixmap.Format.RGBA8888);
        pixmapDown.setColor(0.1f, 0.2f, 0.5f, 1f);
        pixmapDown.fill();
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(pixmapDown)));

        buttonStyle.fontColor = Color.WHITE;

        // Botón de iniciar juego
        final TextButton startButton = new TextButton("Iniciar juego", buttonStyle);
        startButton.pad(400);
        startButton.getLabel().setFontScale(1.2f);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startButton.addAction(Actions.sequence(
                    Actions.scaleTo(1.1f, 1.1f, 0.1f),
                    Actions.scaleTo(1f, 1f, 0.1f),
                    Actions.run(() -> {
                        // ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
                    })
                ));
            }
        });

        // Label de contador de jugadores
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = labelFont;
        labelStyle.fontColor = Color.WHITE;
        playerCountLabel = new Label("Jugadores conectados: 0", labelStyle);
        playerCountLabel.setAlignment(Align.center);

        // Layout con tabla
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(150);

        // Título animado como Image con escala
        animatedTitle = new Image(titleAnimation.getKeyFrame(0));
        float titleScale = 1.5f;
        animatedTitle.setSize(
            animatedTitle.getWidth() * titleScale,
            animatedTitle.getHeight() * titleScale
        );

        table.add(animatedTitle).center().padBottom(150).row();
        table.add(playerCountLabel).center().padBottom(20).row();
        table.add(startButton).center().width(600).height(200);

        stage.addActor(table);

        // Registrar listener para WebSocket
        ServerUtils.instance.addListener(this);
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
        TextureRegion currentFrame = backgroundAnimation.getKeyFrame(stateTime);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar frame del título animado
        animatedTitle.setDrawable(new TextureRegionDrawable(titleAnimation.getKeyFrame(stateTime)));

        batch.begin();
        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }

    // Implementación del WebSocketEventListener

    @Override
    public void onMessage(ServerMessage message) {
        if (message.type.equals("player_count")) {
            final int count = message.data.getInt("count");
            Gdx.app.postRunnable(() -> {
                playerCountLabel.setText("Jugadores conectados: " + count);
            });
        }
    }

    @Override public void onConnect() {}
    @Override public void onDisconnect(int code, String reason) {}
    @Override public void onBinaryMessage(byte[] data) {}
    @Override public void onError(Throwable error) {}
}
