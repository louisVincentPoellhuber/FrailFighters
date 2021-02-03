package com.mygdx.frailfighters.modele.objets;

/**
 * Classe de l'arme
 *
 * @author Léo Brulotte
 */
public class Arme extends ModeleItem {

    private float degats;
    private float puissance;

    public Arme(float degats, float puissance, float masse) {
        super(masse);
        this.degats = degats;
        this.puissance = puissance;
    }

    public float getDegats() {
        return degats;
    }

    public float getPuissance() {
        return puissance;
    }
}
