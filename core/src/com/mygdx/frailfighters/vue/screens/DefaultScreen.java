/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.frailfighters.vue.screens;

import com.badlogic.gdx.Screen;
import com.mygdx.frailfighters.FrailFighters;

/**
 * Classe mère des écrans
 *
 * @author Louis-Vincent
 */
public abstract class DefaultScreen implements Screen {

    public FrailFighters game;

    protected DefaultScreen(FrailFighters game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    /**
     * Appelée lorsque l'écran devient l'écran actuel
     */
    @Override
    public void render(float f) {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    /**
     * Se débarasse des ojets inutiles
     */
    @Override
    public void dispose() {

    }

}
