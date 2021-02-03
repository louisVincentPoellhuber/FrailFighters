package com.mygdx.frailfighters.vue.screens.resources;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.frailfighters.controleur.Controleur;
import java.util.ArrayList;
import java.util.Observer;

/**
 * Classe des objets pouvant bouger
 *
 * @author Léo Brulotte
 */
public abstract class Bougeable extends Sprite implements Observer {

    protected float posX;
    protected float posY;
    protected Ressource res;
    protected Rectangle hitbox;
    protected Tile[][] tabTiles;
    protected ArrayList<Player> listeJoueurs;
    protected long temps;
    protected HealthBar healthBar;
    Controleur controleur;

    public Bougeable(float posX, float posY, Ressource res, int largeur, int hauteur,
            Tile[][] tabTiles, ArrayList<Player> listeJoueurs, Controleur controleur) {
        this.posX = posX;
        this.posY = posY;
        this.res = res;
        hitbox = new Rectangle(posX, posY, largeur, hauteur);
        this.tabTiles = tabTiles;
        this.listeJoueurs = listeJoueurs;
        this.controleur = controleur;
        temps = System.currentTimeMillis();

        TextureRegion regionHealthBar = res.gameSprites.findRegion("healthbar");
        healthBar = new HealthBar(regionHealthBar, 0, 0, this);
    }

    /**
     * Augmente ou diminue un déplacement de 0.5 pixels
     *
     * @param augmente true si le déplacement est positif, false s'il est
     * négatif
     * @param deplacement le déplacement déjà effectué
     * @return le déplacement incrémenté
     */
    protected float incrementerDeplacement(boolean augmente, float deplacement) {
        if (augmente) {
            return deplacement + (float) 0.5;
        } else {
            return deplacement - (float) 0.5;
        }
    }

    /**
     * Détecte les collisions entre la hitbox du bougeable et les tiles
     *
     * @return true s'il y a une collision, false s'il y en a pas
     */
    protected boolean detecterCollisions() {
        for (int i = 0; i < tabTiles.length; i++) {
            for (int j = 0; j < tabTiles[0].length; j++) {
                if (hitbox.overlaps(tabTiles[i][j].getHitbox()) && !(tabTiles[i][j] instanceof TileBackground)) {
                    return true;
                }
            }
        }
        return false;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(int fieldX) {
        this.posX = fieldX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(int fieldY) {
        this.posY = fieldY;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public abstract int getVie();

    /**
     * Met la position du bougeable à jour et affiche celui-ci
     *
     * @param batch obet qui affiche tout ce qu'il y a à afficher
     */
    public void draw(SpriteBatch batch) {
        setPosition(posX, posY);
        healthBar.setPosition(posX - (healthBar.getLargeur() - this.getWidth()) / 2, posY + this.getHeight() + 1);
        hitbox.setPosition(posX, posY);
        super.draw(batch);
        healthBar.draw(batch);
    }
}
