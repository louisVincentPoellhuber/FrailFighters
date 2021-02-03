package com.mygdx.frailfighters.vue.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.frailfighters.FrailFighters;
import com.mygdx.frailfighters.controleur.Controleur;
import com.mygdx.frailfighters.modele.Modele;
import com.mygdx.frailfighters.modele.objets.Arme;
import com.mygdx.frailfighters.modele.objets.Armure;
import com.mygdx.frailfighters.vue.PlatformMap;
import com.mygdx.frailfighters.vue.screens.resources.HealthBar;
import com.mygdx.frailfighters.vue.screens.resources.Item;
import com.mygdx.frailfighters.vue.screens.resources.Player;
import java.util.ArrayList;
import java.util.Random;

/**
 * Écran affiché lors d'une partie
 *
 * @author Léo Brulotte
 */
public class GameScreen extends DefaultScreen implements InputProcessor {

    SpriteBatch batch;

    public static final int SCREEN_W = 16 * 18;
    public static final int SCREEN_H = 16 * 10;

    public Stage gameStage;
    private PlatformMap platformMap;
    private Modele modele;
    private Controleur controleur;
    private Player player1;
    private Player player2;
    private Item item;

    private HealthBar healthbar;

    private ArrayList<Integer> keyList = new ArrayList<Integer>();
    private ArrayList<Player> listeJoueurs = new ArrayList<Player>();
    private ArrayList<Item> listeItems = new ArrayList<Item>();
    private ArrayList<String> persoSelection = new ArrayList<String>();

    private Random rng = new Random();

    public GameScreen(FrailFighters game, PlatformMap platformMap,
            ArrayList<String> persoSelection, Controleur controleur, Modele modele) {
        super(game);
        batch = new SpriteBatch();
        ExtendViewport viewport = new ExtendViewport(SCREEN_W, SCREEN_H);
        gameStage = new Stage(viewport, batch);

        this.controleur = controleur;
        this.modele = modele;

        this.persoSelection = persoSelection;
        this.platformMap = platformMap;

        initialiserPlayers();

        Gdx.input.setInputProcessor(this);

        setUpControllers();
    }

    /**
     * Initialise les joueurs
     */
    private void initialiserPlayers() {
        int positionJoueur1[] = platformMap.getSpawnPositions().get(0);
        int positionJoueur2[] = platformMap.getSpawnPositions().get(1);
        listeJoueurs.add(new Player(controleur, positionJoueur1[0], positionJoueur1[1],
                Input.Keys.W, Input.Keys.A, Input.Keys.D, Input.Keys.SPACE, Input.Keys.E,
                Input.Keys.Q, Input.Keys.S, game.res, modele.getListeJoueurs().get(0),
                platformMap.getMap(), listeJoueurs, listeItems, persoSelection.get(0)));
        listeJoueurs.add(new Player(controleur, positionJoueur2[0], positionJoueur2[1],
                Input.Keys.UP, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.K,
                Input.Keys.L, Input.Keys.J, Input.Keys.DOWN, game.res,
                modele.getListeJoueurs().get(1), platformMap.getMap(), listeJoueurs,
                listeItems, persoSelection.get(1)));

    }

    /**
     * Crée un item aléatoire à une position aléatoire
     *
     * @return un item généré aléatoirement
     */
    private Item creerItem() {
        int type = rng.nextInt(2);
        int niveau = rng.nextInt(4);
        int position[] = platformMap.getSpawnPositions().get(rng.nextInt(platformMap.getSpawnPositions().size()));

        if (type == 0) {
            Arme templateArme = (Arme) modele.getItemTemplates().get(niveau);
            Arme arme = new Arme(templateArme.getDegats(), templateArme.getPuissance(), templateArme.getMasse());
            return new Item(arme, position[0], position[1], game.res, platformMap.getMap(), listeJoueurs, "weapon" + (niveau + 1), controleur);
        } else {
            Armure templateArmure = (Armure) modele.getItemTemplates().get(niveau + 4);
            Armure armure = new Armure(templateArmure.getDefense(), templateArmure.getMasse());
            return new Item(armure, position[0], position[1], game.res, platformMap.getMap(), listeJoueurs, "armor" + (niveau + 1), controleur);
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

        modele.isPartieFinie();
        if (!modele.isPartieFinie()) {

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            ArrayList<Item> listeItemsTemp = new ArrayList<Item>();
            for (Item item : listeItems) {
                if (item.getItem().getVie() <= 0) {
                    listeItemsTemp.add(item);
                }
            }
            listeItems.removeAll(listeItemsTemp);

            for (Player joueur : listeJoueurs) {
                joueur.bouger(keyList);
            }

            for (Item item : listeItems) {
                item.bouger();
            }

            if (rng.nextInt(750) == 1) {
                try {
                    listeItems.add(creerItem());
                } catch (NullPointerException e) {
                    System.out.println("Image not found");
                } catch (IllegalArgumentException e) {
                    System.out.println("No spawn available");
                }
            }

            batch.begin();
            platformMap.draw(gameStage);

            for (Item item : listeItems) {
                item.draw(batch);
            }

            for (Player joueur : listeJoueurs) {
                joueur.draw(batch);
            }

            batch.end();
            gameStage.draw();

        } else {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new EndGameScreen((FrailFighters) game, modele.getGagnant().getPersoSelection()));
        }
    }

    /**
     * Une touche du clavier est appuyée
     *
     * @param i le code de la touche
     * @return false
     */
    @Override
    public boolean keyDown(int i) {
        if (i == Keys.W) {
            keyList.add(Keys.W);
        }

        if (i == Keys.A) {
            keyList.add(Keys.A);
        }

        if (i == Keys.S) {
            keyList.add(Keys.S);
        }

        if (i == Keys.D) {
            keyList.add(Keys.D);
        }

        if (i == Keys.E) {
            keyList.add(Keys.E);
        }

        if (i == Keys.Q) {
            keyList.add(Keys.Q);
        }

        if (i == Keys.LEFT) {
            keyList.add(Keys.LEFT);
        }

        if (i == Keys.RIGHT) {
            keyList.add(Keys.RIGHT);
        }

        if (i == Keys.UP) {
            keyList.add(Keys.UP);
        }
        if (i == Keys.DOWN) {
            keyList.add(Keys.DOWN);
        }

        if (i == Keys.SPACE) {
            keyList.add(Keys.SPACE);
        }

        if (i == Keys.K) {
            keyList.add(Keys.K);
        }

        if (i == Keys.L) {
            keyList.add(Keys.L);
        }

        if (i == Keys.J) {
            keyList.add(Keys.J);
        }

        return false;
    }

    /**
     * On a arrêté d'appuyer sur une touche
     *
     * @param i la code de la touche
     * @return false
     */
    @Override
    public boolean keyUp(int i) {
        if (i == Keys.W) {
            keyList.remove((Integer) Keys.W);
        }

        if (i == Keys.A) {
            keyList.remove((Integer) Keys.A);
        }

        if (i == Keys.S) {
            keyList.remove((Integer) Keys.S);
        }

        if (i == Keys.D) {
            keyList.remove((Integer) Keys.D);
        }

        if (i == Keys.E) {
            keyList.remove((Integer) Keys.E);
        }

        if (i == Keys.Q) {
            keyList.remove((Integer) Keys.Q);
        }

        if (i == Keys.LEFT) {
            keyList.remove((Integer) Keys.LEFT);
        }

        if (i == Keys.RIGHT) {
            keyList.remove((Integer) Keys.RIGHT);
        }

        if (i == Keys.UP) {
            keyList.remove((Integer) Keys.UP);
        }
        if (i == Keys.DOWN) {
            keyList.remove((Integer) Keys.DOWN);
        }

        if (i == Keys.SPACE) {
            keyList.remove((Integer) Keys.SPACE);
        }

        if (i == Keys.K) {
            keyList.remove((Integer) Keys.K);
        }

        if (i == Keys.L) {
            keyList.remove((Integer) Keys.L);
        }

        if (i == Keys.J) {
            keyList.remove((Integer) Keys.J);
        }

        return false;
    }

    /**
     * Initialise les manettes de jeu et assigne leur boutons à des touches du
     * clavier
     */
    private void setUpControllers() {

        try {
            for (Controller controller : Controllers.getControllers()) {
                Gdx.app.log("yeet", controller.getName());
            }

            Controllers.getControllers().get(0).addListener(new ControllerAdapter() {

                @Override
                public boolean buttonDown(Controller controller, int buttonCode) {
                    if (buttonCode == 2) {
                        keyList.add(Keys.W);
                    }

                    if (buttonCode == 5) {
                        keyList.add(Keys.S);
                    }

                    if (buttonCode == 3) {
                        keyList.add(Keys.E);
                    }

                    if (buttonCode == 1) {
                        keyList.add(Keys.Q);
                    }

                    if (buttonCode == 0) {
                        keyList.add(Keys.SPACE);
                    }

                    return false;
                }

                @Override
                public boolean buttonUp(Controller controller, int buttonCode) {
                    if (buttonCode == 2) {
                        keyList.remove((Integer) Keys.W);
                    }

                    if (buttonCode == 5) {
                        keyList.remove((Integer) Keys.S);
                    }

                    if (buttonCode == 3) {
                        keyList.remove((Integer) Keys.E);
                    }

                    if (buttonCode == 1) {
                        keyList.remove((Integer) Keys.Q);
                    }

                    if (buttonCode == 0) {
                        keyList.remove((Integer) Keys.SPACE);
                    }

                    return false;
                }

                @Override
                public boolean povMoved(Controller controller, int povCode, PovDirection value) {
                    if (value == PovDirection.west) {
                        keyList.add(Keys.A);
                    } else {
                        keyList.remove((Integer) Keys.A);
                    }

                    if (value == PovDirection.east) {
                        keyList.add(Keys.D);
                    } else {
                        keyList.remove((Integer) Keys.D);
                    }

                    return false;
                }
            });

            Controllers.getControllers().get(1).addListener(new ControllerAdapter() {

                @Override
                public boolean buttonDown(Controller controller, int buttonCode) {
                    if (buttonCode == 2) {
                        keyList.add(Keys.UP);
                    }

                    if (buttonCode == 5) {
                        keyList.add(Keys.DOWN);
                    }

                    if (buttonCode == 3) {
                        keyList.add(Keys.NUMPAD_3);
                    }

                    if (buttonCode == 1) {
                        keyList.add(Keys.NUMPAD_2);
                    }

                    if (buttonCode == 0) {
                        keyList.add(Keys.CONTROL_RIGHT);
                    }

                    return false;
                }

                @Override
                public boolean buttonUp(Controller controller, int buttonCode) {
                    if (buttonCode == 2) {
                        keyList.remove((Integer) Keys.UP);
                    }

                    if (buttonCode == 5) {
                        keyList.remove((Integer) Keys.DOWN);
                    }

                    if (buttonCode == 3) {
                        keyList.remove((Integer) Keys.E);
                    }

                    if (buttonCode == 1) {
                        keyList.remove((Integer) Keys.Q);
                    }

                    if (buttonCode == 0) {
                        keyList.remove((Integer) Keys.CONTROL_RIGHT);
                    }

                    return false;
                }

                @Override
                public boolean povMoved(Controller controller, int povCode, PovDirection value) {
                    if (value == PovDirection.west) {
                        keyList.add(Keys.LEFT);
                    } else {
                        keyList.remove((Integer) Keys.LEFT);
                    }

                    if (value == PovDirection.east) {
                        keyList.add(Keys.RIGHT);
                    } else {
                        keyList.remove((Integer) Keys.RIGHT);
                    }

                    return false;
                }
            });
        } catch (IndexOutOfBoundsException e) {

        }
    }

    /**
     * Se débarasse des ojets inutiles
     */
    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        gameStage.dispose();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }

}
