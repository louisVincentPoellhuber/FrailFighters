package com.mygdx.frailfighters.vue.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.frailfighters.FrailFighters;
import com.mygdx.frailfighters.controleur.Controleur;
import com.mygdx.frailfighters.modele.Modele;
import com.mygdx.frailfighters.vue.PlatformMap;
import com.mygdx.frailfighters.vue.screens.resources.Ressource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Écran de sélection des personnages
 *
 * @author Louis-Vincent
 */
public class CharacterScreen extends DefaultScreen {

    SpriteBatch batch;
    public Stage gameStage;
    private TextureAtlas atlas;
    protected Skin skin;
    private Game game;
    private Ressource res;
    private ArrayList<String> listePersos = new ArrayList<String>();
    private ArrayList<String> persoSelection = new ArrayList<String>();
    private PlatformMap mapSelectionee;

    private Animation animationPerso1;
    private Animation animationPerso2;

    public static final int SCREEN_W = Ressource.TILE_SIZE * 80;
    public static final int SCREEN_H = Ressource.TILE_SIZE * 40;

    public CharacterScreen(FrailFighters game, PlatformMap mapSelectionee, ArrayList<String> persoSelection) {
        super(game);
        this.game = game;
        this.mapSelectionee = mapSelectionee;
        atlas = new TextureAtlas("core/assets/ui/vhs-ui.atlas");
        skin = new Skin(Gdx.files.internal("core/assets/ui/vhs-ui.json"), atlas);
        this.persoSelection = persoSelection;

        batch = new SpriteBatch();
        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);
        gameStage = new Stage(viewport, batch);
        res = game.res;

        detecterPersos();
    }

    /**
     * Crée les boutons lorsque cet écran devient l'écran actuel
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(gameStage);
        ArrayList<TextButton> listeBoutons = new ArrayList<TextButton>();
        TextButton playButton = new TextButton("PLAY!", skin);
        TextButton restartButton = new TextButton("Restart", skin);
        TextButton backButton = new TextButton("Back", skin);
        Table characterTable = new Table();
        Table optionsTable = new Table();
        characterTable.setFillParent(true);
        optionsTable.setFillParent(true);
        characterTable.top();
        optionsTable.bottom();

        for (int i = 0; i < listePersos.size(); i++) {
            final int j = i;

            listeBoutons.add(new TextButton(listePersos.get(i).toUpperCase(), skin));

            listeBoutons.get(i).addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (persoSelection.size() < Modele.NBR_MAX_JOUEURS) {
                        persoSelection.add(listePersos.get(j));
                        ((Game) Gdx.app.getApplicationListener()).setScreen(
                                new CharacterScreen((FrailFighters) game, mapSelectionee, persoSelection));
                    }
                }
            });
            characterTable.row();
            characterTable.row();
            characterTable.row();
            characterTable.add(listeBoutons.get(i));
        }

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (persoSelection.size() >= 2) {
                    Controleur controleur = new Controleur((FrailFighters) game, persoSelection, mapSelectionee);
                }
            }
        });
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                persoSelection.clear();
                ((Game) Gdx.app.getApplicationListener()).setScreen(
                        new CharacterScreen((FrailFighters) game, mapSelectionee, persoSelection));
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(
                        new MapScreen((FrailFighters) game));
            }
        });

        optionsTable.add(playButton, restartButton, backButton);

        gameStage.addActor(characterTable);
        gameStage.addActor(optionsTable);
    }

    /**
     * Détecte les personnages du fichier de personnages
     */
    private void detecterPersos() {
        try {
            FileOutputStream fos = new FileOutputStream("core/assets/characters/characters.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeInt(2);
            oos.writeUTF("adventurer");
            oos.writeUTF("dwarf");

            oos.close();

        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.out.println("why me");
        }
        try {
            FileInputStream fichier = new FileInputStream("core/assets/characters/characters.dat");
            ObjectInputStream ois = new ObjectInputStream(fichier);
            int temp = ois.readInt();

            for (int i = 0; i < temp; i++) {
                listePersos.add(ois.readUTF());
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.out.println("Erreur1");
        }

    }

    /**
     * Affiche les personnages choisis
     */
    private void drawSelection() {
        TextureRegion[] animationFrame;
        int index = 0;
        for (String s : persoSelection) {
            Texture t = new Texture(Gdx.files.internal("core/assets/characters/" + s + "/select.png"));
            batch.draw(t, (1 + index * 2) * SCREEN_W / 4 - t.getWidth() / 2, SCREEN_H / 2);
            index++;
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

        batch.begin();

        drawSelection();

        batch.end();

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
