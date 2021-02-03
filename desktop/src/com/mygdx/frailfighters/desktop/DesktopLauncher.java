package com.mygdx.frailfighters.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.frailfighters.FrailFighters;

/**
 * La classe permettant de démarrer le jeu
 * @author Louis-Vincent
 */
public class DesktopLauncher {

    /**
     * Regroupe les images du jeu (pour les maps et les items) dans un TextureAtlas
     */
    static void pack() {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;
        settings.pot = true;
        TexturePacker.process(settings, "\\core\\assets\\png", "\\core\\assets\\packed", "game");
    }
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "FRAIL FIGHTERS";
        config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
        config.resizable = false;
        config.vSyncEnabled = true;
        new LwjglApplication(new FrailFighters(), config);
    }
}
