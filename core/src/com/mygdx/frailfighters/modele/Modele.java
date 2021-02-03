package com.mygdx.frailfighters.modele;

import com.mygdx.frailfighters.vue.screens.GameScreen;
import com.mygdx.frailfighters.FrailFighters;
import com.mygdx.frailfighters.modele.objets.Arme;
import com.mygdx.frailfighters.modele.objets.Armure;
import com.mygdx.frailfighters.modele.objets.ModeleItem;
import com.mygdx.frailfighters.modele.objets.ModelePlayer;
import java.util.ArrayList;

/**
 * Classe du modèle
 *
 * @author Louis-Vincent
 */
public class Modele {

    public static final int MAX_BASE_X = GameScreen.SCREEN_W;
    public static final int MAX_BASE_Y = GameScreen.SCREEN_H;
    public static final int NBR_MAX_JOUEURS = 2;

    private ArrayList<ModelePlayer> listeJoueurs = new ArrayList<ModelePlayer>();
    private ModelePlayer player;
    private FrailFighters game;
    private boolean partieFinie = false;
    private ModelePlayer gagnant;

    private ArrayList<ModeleItem> itemTemplates = new ArrayList<ModeleItem>();

    public Modele(FrailFighters game, ArrayList<String> persoSelection) {
        this.game = game;
        listeJoueurs.add(new ModelePlayer(new Armure(0, 0), new Arme(0, 0, 0), persoSelection.get(0)));
        listeJoueurs.add(new ModelePlayer(new Armure(0, 0), new Arme(0, 0, 0), persoSelection.get(1)));
        setUpItemTemplates();
    }

    /**
     * Initialise les modèles des items
     */
    private void setUpItemTemplates() {
        itemTemplates.add(new Arme(1, 1, 1));
        itemTemplates.add(new Arme(2, 2, 2));
        itemTemplates.add(new Arme(3, 3, 3));
        itemTemplates.add(new Arme(4, 4, 4));
        itemTemplates.add(new Armure(1, 1));
        itemTemplates.add(new Armure(2, 2));
        itemTemplates.add(new Armure(3, 3));
        itemTemplates.add(new Armure(4, 4));
    }

    /**
     * Vérifie si un joueur a perdu, mettant fin à la partie
     */
    public boolean isPartieFinie() {
        for (ModelePlayer joueur : listeJoueurs) {
            if (joueur.getVie() <= 0) {
                partieFinie = true;
            } else {
                gagnant = joueur;
            }
        }

        return partieFinie;
    }

    public ArrayList<ModelePlayer> getListeJoueurs() {
        return listeJoueurs;
    }

    public ModelePlayer getGagnant() {
        return gagnant;
    }

    public ArrayList<ModeleItem> getItemTemplates() {
        return itemTemplates;
    }

}
