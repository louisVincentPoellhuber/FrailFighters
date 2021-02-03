/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.frailfighters.vue.screens.resources;

/**
 *  Tuile avec laquelle on ne peut pas interagir
 * @author Louis-Vincent
 */
public class TileBackground extends Tile{
    
    public TileBackground(String region, int posX, int posY) {
        super(region, posX, posY, -1);
    }
    
}
