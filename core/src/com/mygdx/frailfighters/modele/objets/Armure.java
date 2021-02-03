package com.mygdx.frailfighters.modele.objets;

public class Armure extends ModeleItem {

    private int defense;

    /**
     * Classe de l'armure
     *
     * @author Léo Brulotte
     */
    public Armure(int armure, float masse) {
        super(masse);
        this.defense = armure;
    }

    public int getDefense() {
        return defense;
    }
}
