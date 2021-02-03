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
import com.mygdx.frailfighters.vue.PlatformMap;
import com.mygdx.frailfighters.vue.screens.resources.Ressource;
import com.mygdx.frailfighters.vue.screens.resources.Tile;
import com.mygdx.frailfighters.vue.screens.resources.TileBackground;
import com.mygdx.frailfighters.vue.screens.resources.TileSolide;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Écran de sélection des map
 *
 * @author Louis-Vincent
 */
public class MapScreen extends DefaultScreen {

    SpriteBatch batch;
    public Stage gameStage;
    private TextureAtlas atlas;
    protected Skin skin;
    private Game game;
    private Ressource res;
    private ArrayList<PlatformMap> listeMap = new ArrayList<PlatformMap>();
    private ArrayList<String> persoSelection = new ArrayList<String>();
    private PlatformMap mapSelectionnee;

    public static final int SCREEN_W = Ressource.TILE_SIZE * 80;
    public static final int SCREEN_H = Ressource.TILE_SIZE * 40;

    public MapScreen(FrailFighters game) {
        super(game);
        this.game = game;
        atlas = new TextureAtlas("core/assets/ui/vhs-ui.atlas");
        skin = new Skin(Gdx.files.internal("core/assets/ui/vhs-ui.json"), atlas);
        batch = new SpriteBatch();
        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);
        gameStage = new Stage(viewport, batch);
        res = game.res;

        detecterMaps();
    }

    /**
     * Crée les boutons lorsque cet écran devient l'écran actuel
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(gameStage);
        ArrayList<TextButton> listeBoutons = new ArrayList<TextButton>();
        TextButton backButton = new TextButton("Back", skin);
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();

        for (int i = 0; i < listeMap.size(); i++) {
            final int j = i;

            listeBoutons.add(new TextButton("Map " + i, skin));

            listeBoutons.get(i).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    initialiserTiles();
                    mapSelectionnee = listeMap.get(j);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(
                            new CharacterScreen((FrailFighters) game, mapSelectionnee, persoSelection));
                }
            });
            mainTable.row();
            mainTable.row();
            mainTable.row();
            mainTable.add(listeBoutons.get(i));

            backButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(
                            new MainMenuScreen((FrailFighters) game));
                }
            });
        }
        backButton.bottom();
        gameStage.addActor(mainTable);
        gameStage.addActor(backButton);
    }

    /**
     * Crée les map à partir du fichier de maps
     */
    private void detecterMaps() {
        try {
            FileInputStream fichier = new FileInputStream("core/assets/Maps/fichierMaps.dat");
            ObjectInputStream ois = new ObjectInputStream(fichier);

            int index = ois.readInt();

            for (int i = 0; i < index; i++) {
                Tile[][] tabRead = (Tile[][]) ois.readObject();
                listeMap.add(new PlatformMap(tabRead, res, (ArrayList<int[]>) ois.readObject()));
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.out.println("Erreur1");

        } catch (ClassNotFoundException e) {
            System.out.println("Erreur classe introuvable");
        }

    }

    /**
     * Initialise les tiles de chaque map
     */
    private void initialiserTiles() {
        for (PlatformMap pm : listeMap) {
            for (int i = 0; i < pm.getMap().length; i++) {
                for (int j = 0; j < pm.getMap()[0].length; j++) {
                    pm.getMap()[i][j].initialiser(res);
                }
            }
        }
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
