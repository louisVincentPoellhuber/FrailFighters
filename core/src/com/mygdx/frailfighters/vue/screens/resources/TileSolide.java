/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.frailfighters.vue.screens.resources;

/**
 * Tuile avec laquelle on peut avoir une collision
 * @author Louis-Vincent
 */
public class TileSolide extends Tile{
    
    public TileSolide(String region, int posX, int posY) {
        super(region, posX, posY, Ressource.TILE_SIZE);
    }
    
}
