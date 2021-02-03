package com.mygdx.frailfighters.modele.objets;

import static com.mygdx.frailfighters.modele.objets.ModeleBougeable.ACCEL_GRAVITE;
import static com.mygdx.frailfighters.modele.objets.ModeleBougeable.COEFF_FROTTE;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Modèle des joueurs
 * @author Louis-Vincent
 */
public class ModelePlayer extends ModeleBougeable{

    public float vitesseSaut = 0;
    private float knockbackThreshold = 0;
    private float masseBase;
    private float pwrBase = 0;
    private float degatBase = 0;
    private float defenseBase = 0;

    private Armure armure;
    private Arme arme;

    private String persoSelection;

    private boolean knockedBack = false;

    private float pwrAttaque;
    private float degatAttaque;
    private float pwrCharge = 0;

    public ModelePlayer(Armure armure, Arme arme, String persoSelection) {
        super(0 + armure.getMasse() + arme.getMasse(), 100, armure.getDefense());
        this.armure = armure;
        this.arme = arme;
        this.persoSelection = persoSelection;
        
        initialiserPerso();

        defense = defenseBase + armure.getDefense();
        pwrAttaque = pwrBase + arme.getPuissance();
        degatAttaque = degatBase + arme.getDegats();
        defense = defenseBase + armure.getDefense();
        setMasse(masseBase);
    }

    /**
     * Calcule et met à jour l'accélération du joueur
     */
    @Override
    public void calculerAcceleration() {
        if (tombe) {
            accelY = ACCEL_GRAVITE;
        } else {
            accelY = 0;
        }

        if (knockedBack && !tombe && vitesseX != 0) {
            accelX = (float) COEFF_FROTTE * masse * ACCEL_GRAVITE * vitesseX / Math.abs(vitesseX);
        } else {
            accelX = 0;
        }
    }

    /**
     * Calcule et met à jour la vitesse du joueur
     */
    @Override
    public void calculerVitesse() {
        long nouveauTemps = System.currentTimeMillis();

        vitesseY += accelY * (nouveauTemps - temps);

        if ((vitesseX > 0 && vitesseX + accelX * (nouveauTemps - temps) > 0) || (vitesseX < 0 && vitesseX + accelX * (nouveauTemps - temps) < 0)) {
            vitesseX += accelX * (nouveauTemps - temps);
        } else {
            vitesseX = 0;
        }

        temps = nouveauTemps;

        if (Math.sqrt(Math.pow(vitesseX, 2) + Math.pow(vitesseY, 2)) < 0.1) {
            knockedBack = false;
            vitesseX = 0;
        }
    }

    /**
     * Le joueur saute
     */
    public void sauter() {
        vitesseY += vitesseSaut;
    }

    /**
     * Le joueur est affecté par une attaque
     *
     * @param pwrAtkX la composante x de l'attaque
     * @param pwrAtkY la composante y de l'attaque
     * @param degats les dégâts de l'attaque
     */
    @Override
    public void subirAttaque(float pwrAtkX, float pwrAtkY, float degats) {
        vitesseX = (pwrAtkX + masse * vitesseX) / masse;
        vitesseY = (pwrAtkY + masse * vitesseY) / masse;
        vie -= degats / (defense / 10);

        if (Math.sqrt(Math.pow(pwrAtkX, 2) + Math.pow(pwrAtkY, 2)) >= knockbackThreshold) {
            knockedBack = true;
        }
        majObserver();
    }

    /**
     * Initialise les attributs du personnages selon le personnage choisi dans le fichier de personnages
     */
    private void initialiserPerso() {
        try {
            FileInputStream fichier = new FileInputStream("core/assets/characters/" + persoSelection + "/stats.dat");
            ObjectInputStream ois = new ObjectInputStream(fichier);

            vitesseSaut = ois.readFloat();
            knockbackThreshold = ois.readInt();
            masseBase = ois.readInt();
            pwrBase = ois.readInt();
            degatBase = ois.readInt();
            defenseBase = ois.readInt();

        } catch (java.io.IOException e) {
            System.out.println(e);
        }
    }

    public boolean isKnockedBack() {
        return knockedBack;
    }

    public float getPwrCharge() {
        return pwrCharge;
    }

    public void setPwrCharge(float pwrCharge) {
        this.pwrCharge = pwrCharge;
    }

    public float getDegatAttaque() {
        return degatAttaque;
    }

    public float getPwrAttaque() {
        return pwrAttaque;
    }

    /**
     * Ajuste les attributs du personnage selon l'arme reçue
     * @param arme l'arme reçue
     */
    public void setArme(Arme arme) {
        this.arme = arme;
        masse = masseBase + armure.getMasse() + arme.getMasse();
        pwrAttaque = pwrBase + arme.getPuissance();
        degatAttaque = degatBase + arme.getDegats();
    }

     /**
     * Ajuste les attributs du personnage selon l'armure reçue
     * @param armure l'armure reçue
     */
    public void setArmure(Armure armure) {
        this.armure = armure;
        masse = masseBase + armure.getMasse() + arme.getMasse();
        defense = defenseBase + armure.getDefense();
    }

    public String getPersoSelection() {
        return persoSelection;
    }

    public float getVitesseSaut() {
        return vitesseSaut;
    }

}
