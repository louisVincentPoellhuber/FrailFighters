/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.frailfighters.vue.screens.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.io.Serializable;

/**
 * Ressources visuelles pour les composantes du jeu (map et items)
 *
 * @author Louis-Vincent
 */
public class Ressource {

    TextureAtlas gameSprites;
    public BitmapFont gameFont;

    public static final int TILE_SIZE = 16;

    public Ressource() {
        gameSprites = new TextureAtlas(Gdx.files.internal("core/assets/packed/game.atlas"));
        gameFont = new BitmapFont(Gdx.files.internal("core/assets/fonts/gamefont.fnt"), Gdx.files.internal("core/assets/fonts/gamefont.png"), false);

    }

    /**
     * Se débarasse des objets inutiles
     */
    public void dispose() {
        gameSprites.dispose();
        gameFont.dispose();
    }

}
