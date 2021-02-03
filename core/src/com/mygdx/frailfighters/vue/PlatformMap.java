/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.frailfighters.vue;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.frailfighters.vue.screens.resources.Ressource;
import com.mygdx.frailfighters.vue.screens.resources.Tile;
import java.util.ArrayList;

/**
 * Permet l'affichage des map et les interactions avec celles-ci
 *
 * @author Louis-Vincent
 */
public class PlatformMap {

    private Tile[][] tabMap;
    private Ressource res;
    private ArrayList<int[]> spawnPositions;

    public PlatformMap(Tile[][] map, Ressource res, ArrayList<int[]> spawnPositions) {
        this.tabMap = map;
        this.res = res;
        this.spawnPositions = spawnPositions;
    }

    public Tile[][] getMap() {
        return tabMap;
    }

    /**
     * Affiche la map
     *
     * @param stage permet de dessiner à l'écran
     */
    public void draw(Stage stage) {
        for (int i = 0; i < tabMap.length; i++) {
            for (int j = 0; j < tabMap[0].length; j++) {
                stage.getBatch().draw(tabMap[i][j], tabMap[i][j].getPosX(), tabMap[i][j].getPosY());
            }
        }
    }

    public ArrayList<int[]> getSpawnPositions() {
        return spawnPositions;
    }

}
