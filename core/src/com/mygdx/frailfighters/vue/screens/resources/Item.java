package com.mygdx.frailfighters.vue.screens.resources;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.frailfighters.controleur.Controleur;
import com.mygdx.frailfighters.modele.objets.ModeleItem;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Classe des items
 *
 * @author Léo Brulotte
 */
public class Item extends Bougeable {

    private ModeleItem item;

    public Item(ModeleItem item, float posX, float posY, Ressource res,
            Tile[][] tabTiles, ArrayList<Player> listeJoueurs, String region,
            Controleur controleur) {
        super(posX, posY, res, 16, 16, tabTiles, listeJoueurs, controleur);
        set(new Sprite(res.gameSprites.findRegion(region)));
        this.item = item;
        this.item.addObserver(this);

    }

    /**
     * L'item bouge selon l'environnement
     */
    public void bouger() {
        long nouveauTemps = System.currentTimeMillis();
        boolean continuerX = true;
        boolean continuerY = true;
        float deplacementX = 0;
        float deplacementY = 0;
        boolean augmenterX = true;
        boolean augmenterY = true;
        float posXInitial = posX;
        float posYInitial = posY;

        item.calculerAcceleration();
        item.calculerVitesse();

        float dx = item.getVitesseX() * (nouveauTemps - temps);
        float dy = item.getVitesseY() * (nouveauTemps - temps);

        if (dx < 0) {
            augmenterX = false;
        }

        if (dy < 0) {
            augmenterY = false;
        }

        verifierSiTombe();
        
        while (continuerX || continuerY) {

            if (continuerX && Math.abs(deplacementX) < Math.abs(dx)) {
                deplacementX = incrementerDeplacement(augmenterX, deplacementX);

                hitbox.setX(posXInitial + deplacementX);
                if (detecterCollisions()) {
                    hitbox.setX(posX);
                    continuerX = false;

                    item.setVitesseX((float) -0.75 * item.getVitesseX());
                    item.setVitesseY((float) 0.75 * item.getVitesseY());

                    if (Math.sqrt(Math.pow(item.getVitesseX(), 2) + Math.pow(item.getVitesseY(), 2)) < 0.1) {
                        item.setVitesseX(0);
                    }

                } else {
                    posX = hitbox.getX();
                }
            } else {
                continuerX = false;
            }

            if (continuerY && Math.abs(deplacementY) < Math.abs(dy)) {
                deplacementY = incrementerDeplacement(augmenterY, deplacementY);

                hitbox.setY(posYInitial + deplacementY);
                if (detecterCollisions()) {
                    hitbox.setY(posY);
                    continuerY = false;

                    item.setVitesseX((float) 0.75 * item.getVitesseX());
                    item.setVitesseY((float) -0.75 * item.getVitesseY());

                    if (Math.sqrt(Math.pow(item.getVitesseX(), 2) + Math.pow(item.getVitesseY(), 2)) < 0.1) {
                        item.setVitesseY(0);
                    }

                    if (!augmenterY) {
                        item.setTombe(false);
                    }

                } else {
                    posY = hitbox.getY();
                    item.setTombe(true);
                }
            } else {
                continuerY = false;
            }

            if (Math.sqrt(Math.pow(item.getVitesseX(), 2) + Math.pow(item.getVitesseY(), 1)) > 0.5) {
                collisioner(posXInitial, posYInitial);
            }
        }

        temps = nouveauTemps;
    }

    private void verifierSiTombe() {
        hitbox.setY(posY - 0.5f);
        if (detecterCollisions()) {
            item.setTombe(false);
        } else {
            item.setTombe(true);
        }
        hitbox.setY(posY);
    }
    
    /**
     * L'item est attaqué
     * Appelle la méthode subirAttaque du modèle
     * @param pwrX la force de l'attaque en X
     * @param pwrY la force de l'attaque en Y
     * @param degats les dégâts infligés
     */
    public void subirAttaque(float pwrX, float pwrY, float degats) {
        item.subirAttaque(pwrX, pwrY, degats);
    }

    /**
     * L'item rentre en collision avec un joueur
     *
     * @param posXInitial composante x initiale de la position de l'item
     * @param posYInitial composante y initiale de la position de l'item
     */
    private void collisioner(float posXInitial, float posYInitial) {
        Rectangle hitboxInitiale = new Rectangle(posXInitial, posYInitial, hitbox.getWidth(), hitbox.getHeight());

        for (Player p : listeJoueurs) {
            if (hitbox.overlaps(p.getHitbox()) && !hitboxInitiale.overlaps(p.getHitbox())) {
                p.subirAttaque(item.getVitesseX() * item.getMasse() / 10, item.getVitesseY() * item.getMasse() / 10, item.getMasse() / 10);
                item.setVitesseX(item.getVitesseX() * 9 / 10);
                item.setVitesseY(item.getVitesseY() * 9 / 10);
            }
        }
    }

    public ModeleItem getItem() {
        return item;
    }

    @Override
    public int getVie() {
        return item.getVie();
    }

    /**
     * Met la position du bougeable à jour et affiche celui-ci
     *
     * @param batch objet qui affiche tout ce qu'il y a à afficher
     */
    public void draw(SpriteBatch batch) {
        setPosition(posX, posY);
        hitbox.setPosition(posX, posY);
        super.draw(batch);
    }

    /**
     * Ajuste la vie de l'item
     *
     * @param o paramètre requis par Java
     * @param o1 paramètre requis par Java
     */
    @Override
    public void update(Observable o, Object o1) {
        healthBar.update(o, o1);
    }

}
