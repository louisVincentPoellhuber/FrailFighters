package com.mygdx.frailfighters.vue.screens.resources;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe des barres de vies affichée au dessus des bougeables
 *
 * @author Léo Brulotte
 */
public class HealthBar implements Observer {

    private Texture texture;
    private float posX;
    private float posY;

    private int largeur;
    private int hauteur;
    private int srcX;
    private int srcY;

    private Bougeable bougeable;
    private int pourcentageVie = 100;

    public HealthBar(TextureRegion region, float posX, float posY, Bougeable bougeable) {
        texture = region.getTexture();
        this.posX = posX;
        this.posY = posY;
        this.bougeable = bougeable;
        srcX = region.getRegionX();
        srcY = region.getRegionY();
        largeur = region.getRegionWidth();
        hauteur = region.getRegionHeight();
    }

    public void setPosition(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public float getLargeur() {
        return largeur;
    }

    public float getHauteur() {
        return hauteur;
    }

    /**
     * Met la position du bougeable à jour et affiche celui-ci
     *
     * @param sp obet qui affiche tout ce qu'il y a à afficher
     */
    public void draw(SpriteBatch sp) {
        int largeurFinale = (int) (pourcentageVie / 100f * largeur);

        sp.draw(texture, posX, posY, 0, 0, largeurFinale, hauteur, 1, 1, 0,
                srcX, srcY, largeurFinale, hauteur, false, false);
    }

    @Override
    public void update(Observable o, Object o1) {
        pourcentageVie = bougeable.getVie();
    }
}
