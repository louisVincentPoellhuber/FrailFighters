/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.frailfighters.vue.screens.resources;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import java.io.Serializable;

/**
 * Tuiles de la map
 *
 * @author Louis-Vincent
 */
public abstract class Tile extends Sprite implements Serializable {

    private int posX;
    private int posY;
    private Rectangle hitbox;
    private String region;
    private int sizeHitbox;

    public Tile(String region, int posX, int posY, int sizeHitbox) {
        this.posX = posX;
        this.posY = posY;
        this.region = region;
        this.sizeHitbox = sizeHitbox;
    }

    /**
     * Assigne l'image, la position et la hitbox de la tuile
     *
     * @param res ressources nécessaire pour trouver l'image
     */
    public void initialiser(Ressource res) {
        hitbox = new Rectangle(posX, posY, sizeHitbox, sizeHitbox);
        set(new Sprite(res.gameSprites.findRegion(region)));
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
