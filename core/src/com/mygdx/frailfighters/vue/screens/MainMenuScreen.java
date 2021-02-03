/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.frailfighters.vue.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.frailfighters.FrailFighters;
import com.mygdx.frailfighters.vue.screens.resources.Ressource;

/**
 * Menu principal
 *
 * @author Louis-Vincent
 */
public class MainMenuScreen extends DefaultScreen {

    SpriteBatch batch;
    public Stage gameStage;
    private TextureAtlas atlas;
    protected Skin skin;
    private Game game;

    public static final int SCREEN_W = Ressource.TILE_SIZE * 80;
    public static final int SCREEN_H = Ressource.TILE_SIZE * 40;

    public MainMenuScreen(FrailFighters game) {
        super(game);
        this.game = game;
        atlas = new TextureAtlas("core/assets/ui/vhs-ui.atlas");
        skin = new Skin(Gdx.files.internal("core/assets/ui/vhs-ui.json"), atlas);

        batch = new SpriteBatch();
        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);
        gameStage = new Stage(viewport, batch);
    }

    /**
     * Crée les boutons lorsque cet écran devient l'écran actuel
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(gameStage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        TextButton playButton = new TextButton("Play", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MapScreen((FrailFighters) game));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        mainTable.center();
        mainTable.add(playButton);
        mainTable.row();
        mainTable.row();
        mainTable.row();
        mainTable.add(exitButton);

        gameStage.addActor(mainTable);
    }

    /**
     * Affiche l'écran de sélection des personnages
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
