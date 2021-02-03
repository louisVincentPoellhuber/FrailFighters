package com.mygdx.frailfighters;

import com.mygdx.frailfighters.vue.screens.resources.Ressource;
import com.badlogic.gdx.Game;
import com.mygdx.frailfighters.vue.screens.MainMenuScreen;

/**
 * Classe principale du jeu
 *
 * @author Louis-Vincent Poellhuber
 */
public class FrailFighters extends Game {

    public Ressource res;

    /**
     * Crée le menu
     */
    @Override
    public void create() {
        res = new Ressource();
        setScreen(new MainMenuScreen(this));
    }

    /**
     * Se débarasse des ojets inutiles
     */
    @Override
    public void dispose() {
        res.dispose();
    }
}
