package com.mygdx.frailfighters.controleur;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.frailfighters.FrailFighters;
import com.mygdx.frailfighters.modele.Modele;
import com.mygdx.frailfighters.modele.objets.ModeleBougeable;
import com.mygdx.frailfighters.modele.objets.ModelePlayer;
import com.mygdx.frailfighters.vue.PlatformMap;
import com.mygdx.frailfighters.vue.screens.GameScreen;
import java.util.ArrayList;

/**
 * Classe du contrôleur
 *
 * @author Léo Brulotte
 */
public class Controleur {

    Modele modele;

    public Controleur(FrailFighters game, ArrayList<String> persoSelection, PlatformMap mapSelection) {
        modele = new Modele(game, persoSelection);
        ((Game) Gdx.app.getApplicationListener()).setScreen(
                new GameScreen((FrailFighters) game, mapSelection, persoSelection, this, modele));
    }

    /**
     * Un bougeable subit une attaque
     *
     * @param bougeable le bougeable subissant l'attaque
     * @param pwrX la composante x de la puissance de l'attaque
     * @param pwrY la composante y de la puissance de l'attaque
     * @param degats les dégâts de l'attaque
     */
    public void subirAttaque(ModeleBougeable bougeable, float pwrX, float pwrY, float degats) {
        bougeable.subirAttaque(pwrX, pwrY, degats);
    }

    /**
     * Calcule et met à jour l'accélération d'un bougeable
     *
     * @param bougeable le bougeable dont l'accélération est calculée
     */
    public void calculerAcceleration(ModeleBougeable bougeable) {
        bougeable.calculerAcceleration();
    }

    /**
     * Calcule et met à jour la vitesse d'un bougeable
     *
     * @param bougeable le bougeable dont la vitesse est calculée
     */
    public void calculerVitesse(ModeleBougeable bougeable) {
        bougeable.calculerVitesse();
    }

    /**
     * Un joueur saute
     *
     * @param player le joueur qui saute
     */
    public void joueurSaute(ModelePlayer player) {
        player.sauter();
    }
}
