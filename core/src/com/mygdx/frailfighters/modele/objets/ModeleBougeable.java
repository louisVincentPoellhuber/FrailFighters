package com.mygdx.frailfighters.modele.objets;

import java.util.Observable;

/**
 * Modèle pour les objets qui peuvent bouger
 * @author Léo Brulotte
 */
public abstract class ModeleBougeable extends Observable {

    public static final float ACCEL_GRAVITE = (float) -0.0015;
    public static final float COEFF_FROTTE = (float) 0.05;

    protected float masse;
    protected float vitesseX = 0;
    protected float vitesseY = 0;
    protected float accelY = 0;
    protected float accelX = 0;
    protected boolean tombe = true;
    protected long temps;
    protected int vie;
    protected float defense;

    public ModeleBougeable(float masse, int vie, float defense) {
        this.masse = masse;
        this.vie = vie;
        this.defense = defense;
        temps = System.currentTimeMillis();
    }

    /**
     * Calcule et met à jour l'accélération du bougeable
     */
    public void calculerAcceleration() {
        if (tombe) {
            accelY = ACCEL_GRAVITE;
        } else {
            accelY = 0;
        }

        if (!tombe && vitesseX != 0) {
            accelX = (float) COEFF_FROTTE * ACCEL_GRAVITE * vitesseX / Math.abs(vitesseX);
        } else {
            accelX = 0;
        }
    }

    /**
     * Calcule et met à jour la vitesse du bougeable
     */
    public void calculerVitesse() {
        long nouveauTemps = System.currentTimeMillis();

        vitesseY += accelY * (nouveauTemps - temps);

        if ((vitesseX > 0 && vitesseX + accelX * (nouveauTemps - temps) > 0) || (vitesseX < 0 && vitesseX + accelX * (nouveauTemps - temps) < 0)) {
            vitesseX += accelX * (nouveauTemps - temps);
        } else {
            vitesseX = 0;
        }

        temps = nouveauTemps;
    }

    /**
     * Le bougable est affecté par une attaque
     *
     * @param pwrAtkX la composante x de la puissance d'attaque
     * @param pwrAtkY la composante
     * @param degats les dégâts de l'attaque
     */
    public void subirAttaque(float pwrAtkX, float pwrAtkY, float degats) {
        vitesseX = (pwrAtkX + masse * vitesseX) / masse;
        vitesseY = (pwrAtkY + masse * vitesseY) / masse;
        vie -= degats / (defense / 10);
        majObserver();
    }

    /**
     * Avertis les obsersvers que le modèle a changé
     */
    public void majObserver() {
        setChanged();
        notifyObservers();
    }

    public float getVitesseX() {
        return vitesseX;
    }

    public float getVitesseY() {
        return vitesseY;
    }

    public void setTombe(boolean tombe) {
        this.tombe = tombe;
        calculerAcceleration();
    }

    public boolean isTombe() {
        return tombe;
    }

    public void setVitesseX(float vitesseX) {
        this.vitesseX = vitesseX;
    }

    public void setVitesseY(float vitesseY) {
        this.vitesseY = vitesseY;
    }

    public float getMasse() {
        return masse;
    }

    public int getVie() {
        return vie;
    }

    public void setMasse(float masse) {
        this.masse = masse;
    }

}
