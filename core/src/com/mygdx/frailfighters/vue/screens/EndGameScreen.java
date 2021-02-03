package com.mygdx.frailfighters.vue.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.frailfighters.FrailFighters;
import com.mygdx.frailfighters.vue.screens.resources.Ressource;

/**
 * Écran affiché à la fin d'une partie
 *
 * @author Léo Brulotte
 */
public class EndGameScreen extends DefaultScreen {

    SpriteBatch batch;
    public Stage gameStage;
    private TextureAtlas atlas;
    protected Skin skin;
    private Game game;
    private String nomGagnant;

    public static final int SCREEN_W = Ressource.TILE_SIZE * 80;
    public static final int SCREEN_H = Ressource.TILE_SIZE * 40;

    public EndGameScreen(FrailFighters game, String nomGagnant) {
        super(game);
        this.game = game;
        atlas = new TextureAtlas("ui/vhs-ui.atlas");
        skin = new Skin(Gdx.files.internal("ui/vhs-ui.json"), atlas);
        this.nomGagnant = nomGagnant;

        batch = new SpriteBatch();
        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);
        gameStage = new Stage(viewport, batch);
    }

    /**
     * Crée les boutons et les labels lorsque l'écran devient l'écran actuel
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(gameStage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Label lblGagnant = new Label(nomGagnant + " wins!", skin);
        TextButton continueButton = new TextButton("Continue", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        lblGagnant.setAlignment(Align.center);

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen((FrailFighters) game));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        mainTable.center();
        mainTable.add(lblGagnant);
        mainTable.row();
        mainTable.row();
        mainTable.row();
        mainTable.add(continueButton);
        mainTable.row();
        mainTable.row();
        mainTable.row();
        mainTable.add(exitButton);

        gameStage.addActor(mainTable);
    }

    /**
     * Affiche l'écran
     *
     * @param delta le temps depuis le dernier appel de render()
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameStage.act();
        gameStage.draw();
    }

    /**
     * Se débarasse des ojets inutiles
     */
    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        Gdx.input.setInputProcessor(null);
    }
}
