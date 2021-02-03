package com.mygdx.frailfighters.vue.screens.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.frailfighters.controleur.Controleur;
import com.mygdx.frailfighters.modele.objets.Arme;
import com.mygdx.frailfighters.modele.objets.Armure;
import com.mygdx.frailfighters.modele.objets.ModelePlayer;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Classe des joueurs
 *
 * @author Louis-Vincent
 */
public class Player extends Bougeable {

    private enum FormeAttaque {
        HORIZONTALE,
        VERTICALE,
        AUCUNE;
    };

    private enum EtatJoueur {
        IDLE,
        WALKING,
        JUMPING,
        FALLING,
        CHARGING_SIDE,
        ATTACK_SIDE,
        CHARGING_UP,
        ATTACK_UP,
        CHARGIN_DOWN,
        ATTACK_DOWN,
        KNOCKBACK,
    };

    private final float VITESSE_DEPLACEMENT;
    private static final int ATTACK_WIDTH = 14;
    private static final int ATTACK_HEIGHT = 8;

    private float pwrX = 0, pwrY = 0, stateTime, animationStart;
    private ModelePlayer player;
    private int keyUp, keyLeft, keyRight, keyAttackSide, keyAttackUp, keyAttackDown,
            keyPickUp, directionX, directionY;
    private String persoSelection;
    private boolean isAttacking = false;

    private Rectangle tmp;
    private TextureRegion currentFrame;

    private ArrayList<Item> listeItems;
    private Tile[][] tabTiles;
    private ArrayList<Animation> listeAnimations = new ArrayList();

    private FormeAttaque formeAttaqueActuelle = FormeAttaque.AUCUNE;
    private EtatJoueur prochainEtat = EtatJoueur.IDLE, etatActuel = prochainEtat;

    public Player(Controleur controleur, int posX, int posY, int keyUp, int keyLeft,
            int keyRight, int keyAttackSide, int keyAttackUp, int keyAttackDown, int keyPickUp,
            Ressource res, ModelePlayer player, Tile[][] tabTiles, ArrayList<Player> listeJoueurs, ArrayList<Item> listeItems,
            String persoSelection) {
        super(posX, posY, res, 16, 16, tabTiles, listeJoueurs, controleur);
        this.posX = posX;
        this.posY = posY;
        this.tabTiles = tabTiles;
        this.listeItems = listeItems;

        this.keyUp = keyUp;
        this.keyLeft = keyLeft;
        this.keyRight = keyRight;
        this.keyAttackSide = keyAttackSide;
        this.keyAttackUp = keyAttackUp;
        this.keyAttackDown = keyAttackDown;
        this.keyPickUp = keyPickUp;

        this.res = res;
        this.player = player;
        this.player.addObserver(this);
        this.persoSelection = persoSelection;

        VITESSE_DEPLACEMENT = player.getVitesseSaut() * 3;

        initialiserAnimations();
    }

    /**
     * Le joueur bouge selon l'environnement et les touches appuyées
     *
     * @param keyList les touches appuyées
     */
    public void bouger(ArrayList<Integer> keyList) {
        long nouveauTemps = System.currentTimeMillis();
        float dx = 0;
        float dy = 0;
        boolean augmenterX = true;
        boolean augmenterY = true;

        controleur.calculerAcceleration(player);
        controleur.calculerVitesse(player);

        if (!player.isKnockedBack()) {
            if (keyList.contains(keyLeft)) {
                prochainEtat = EtatJoueur.WALKING;
                if (!isAttacking) {
                    dx -= VITESSE_DEPLACEMENT;
                } else {
                    dx -= VITESSE_DEPLACEMENT / 3;
                }
                directionX = -1;
            }
            if (keyList.contains(keyRight)) {
                prochainEtat = EtatJoueur.WALKING;
                if (!isAttacking) {
                    dx += VITESSE_DEPLACEMENT;
                } else {
                    dx += VITESSE_DEPLACEMENT / 3;
                }
                directionX = 1;

            }
            if (keyList.contains(keyUp) && !player.isTombe()) {
                prochainEtat = EtatJoueur.JUMPING;
                controleur.joueurSaute(player);
                keyList.remove((Integer) keyUp);
            }
            if (keyList.contains(keyAttackSide)) {
                formeAttaqueActuelle = FormeAttaque.HORIZONTALE;
                prochainEtat = EtatJoueur.CHARGING_SIDE;
                attaquer();
            }
            if (keyList.contains(keyAttackUp)) {
                directionY = 1;
                prochainEtat = EtatJoueur.CHARGING_UP;
                formeAttaqueActuelle = FormeAttaque.VERTICALE;
                attaquer();
            }
            if (keyList.contains(keyAttackDown)) {
                directionY = -1;
                prochainEtat = EtatJoueur.CHARGIN_DOWN;
                formeAttaqueActuelle = FormeAttaque.VERTICALE;
                attaquer();
            }
            if (!(keyList.contains(keyAttackSide) || keyList.contains(keyAttackUp)
                    || keyList.contains(keyAttackDown))) {
                terminerAttaque();
            }
            if (keyList.contains(keyPickUp)) {
                pickUpItem();
                keyList.remove((Integer) keyPickUp);
            }
        } else {
            prochainEtat = EtatJoueur.KNOCKBACK;
        }

        if (player.getVitesseY() < 0 && !player.isKnockedBack()) {
            prochainEtat = EtatJoueur.FALLING;
        }

        if (keyList.isEmpty() && player.getVitesseY() == 0) {
            prochainEtat = EtatJoueur.IDLE;
        }

        dx += dx + player.getVitesseX() * (nouveauTemps - temps);
        dy += player.getVitesseY() * (nouveauTemps - temps);

        if (dx < 0) {
            augmenterX = false;
        }

        if (dy < 0) {
            augmenterY = false;
        }

        effectuerMouvement(dx, dy, augmenterX, augmenterY);

        if (stateTime - animationStart >= listeAnimations.get(positionEtat()).getAnimationDuration()) {
            etatActuel = prochainEtat;
        }

        temps = nouveauTemps;
    }

    /**
     * Le joueur bouge selon des paramètres
     *
     * @param dx la composante x du mouvement du joueur
     * @param dy la composante y du mouvement du joueur
     * @param augmenterX true si la composante x est positive, flase le cas
     * échéant
     * @param augmenterY true si la composante y est positive, flase le cas
     * échéant
     */
    private void effectuerMouvement(float dx, float dy, boolean augmenterX, boolean augmenterY) {
        boolean continuerX = true;
        boolean continuerY = true;
        float deplacementX = 0;
        float deplacementY = 0;
        float posXInitial = posX;
        float posYInitial = posY;

        verifierSiTombe();
        while (continuerX || continuerY) {

            if (continuerX && Math.abs(deplacementX) < Math.abs(dx)) {
                deplacementX = incrementerDeplacement(augmenterX, deplacementX);

                hitbox.setX(posXInitial + deplacementX);
                if (detecterCollisions()) {
                    hitbox.setX(posX);
                    continuerX = false;

                    if (player.isKnockedBack()) {
                        player.setVitesseX((float) -0.75 * player.getVitesseX());
                        player.setVitesseY((float) 0.75 * player.getVitesseY());
                    } else {
                        player.setVitesseX(0);
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

                    if (player.isKnockedBack()) {
                        player.setVitesseX((float) 0.75 * player.getVitesseX());
                        player.setVitesseY((float) -0.75 * player.getVitesseY());

                    } else {
                        player.setVitesseY(0);
                    }

                    if (!augmenterY) {
                        player.setTombe(false);
                    }

                } else {
                    posY = hitbox.getY();
                    player.setTombe(true);
                }
            } else {
                continuerY = false;
            }
        }
    }

    /**
     * Vérifie si le joueur tombe
     */
    private void verifierSiTombe() {
        hitbox.setY(posY - 0.5f);
        if (detecterCollisions()) {
            player.setTombe(false);
        } else {
            player.setTombe(true);
        }
        hitbox.setY(posY);
    }

    /**
     * Le joueur ramasse un item
     *
     * @return true si un item est ramassé, false le cas échant
     */
    private boolean pickUpItem() {
        for (Item item : listeItems) {
            if (hitbox.overlaps(item.getHitbox())) {
                if (item.getItem() instanceof Arme) {
                    player.setArme((Arme) item.getItem());
                } else if (item.getItem() instanceof Armure) {
                    player.setArmure((Armure) item.getItem());
                }
                listeItems.remove(item);
                return true;
            }
        }
        return false;
    }

    /**
     * Le joueur charge une attaque
     */
    public void attaquer() {
        isAttacking = true;

        if (player.getPwrCharge() < 35) {
            player.setPwrCharge(player.getPwrCharge() + (float) 0.2);
        }
    }

    /**
     * Le joueur attaque sur un de ses côtés
     *
     * @return la hitbox de l'attaque du joueur
     */
    private Rectangle attaquerHorizontal() {
        etatActuel = EtatJoueur.ATTACK_SIDE;
        animationStart = stateTime;
        return new Rectangle(posCentreJoueurX() + directionX
                * (Ressource.TILE_SIZE / 2 + centreAttaqueX(ATTACK_WIDTH))
                - centreAttaqueX(ATTACK_WIDTH), posCentreJoueurY() - centreAttaqueY(ATTACK_HEIGHT),
                ATTACK_WIDTH, ATTACK_HEIGHT);
    }

    /**
     * Le joueur attaque vers le haut ou le bas
     *
     * @return la hitbox de l'attaque du joueur
     */
    private Rectangle attaquerVertical() {
        if (directionY > 0) {
            etatActuel = EtatJoueur.ATTACK_UP;
        } else {
            etatActuel = EtatJoueur.ATTACK_DOWN;
        }
        animationStart = stateTime;

        return new Rectangle(posCentreJoueurX() + directionX
                * (Ressource.TILE_SIZE / 2 + centreAttaqueX(ATTACK_HEIGHT))
                - centreAttaqueX(ATTACK_HEIGHT), posCentreJoueurY()
                + directionY * (Ressource.TILE_SIZE / 2 + centreAttaqueY(ATTACK_WIDTH) / 2)
                - centreAttaqueY(ATTACK_WIDTH), ATTACK_HEIGHT, ATTACK_WIDTH);
    }

    /**
     * Le joueur finit son attaque. Celle-ci frappe les bougeables en collision
     * avec sa hitbox
     */
    private void terminerAttaque() {
        Rectangle attaqueHitbox = new Rectangle();

        switch (formeAttaqueActuelle) {
            case HORIZONTALE:
                attaqueHitbox = attaquerHorizontal();
                pwrY = 0;
                pwrX = directionX * player.getPwrCharge() * player.getPwrAttaque();
                break;
            case VERTICALE:
                attaqueHitbox = attaquerVertical();
                pwrY = directionY * (float) 1.3 * player.getPwrCharge() * player.getPwrAttaque();
                pwrX = directionX * player.getPwrCharge() * player.getPwrAttaque();
                break;
        }

        for (Player p : listeJoueurs) {
            if (p != this && attaqueHitbox.overlaps(p.hitbox)) {
                p.subirAttaque(pwrX, pwrY, player.getDegatAttaque() * player.getPwrCharge());
            }
        }
        for (Item item : listeItems) {
            if (attaqueHitbox.overlaps(item.hitbox)) {
                item.subirAttaque(pwrX, pwrY, player.getDegatAttaque() * player.getPwrCharge());
            }
        }

        isAttacking = false;
        player.setPwrCharge(0);
        formeAttaqueActuelle = FormeAttaque.AUCUNE;
    }

    /**
     * Extrait les sprites des fichiers pour chaque personnage et leur associe
     * les bons attributs
     */
    private void initialiserAnimations() {
        ArrayList<String> listeRegions = new ArrayList();
        TextureRegion[] animationFrame;
        int spriteWidth = 1;
        int spriteHeight = 1;

        try {
            FileInputStream fichier = new FileInputStream("core/assets/characters/" + persoSelection + "/sprites.dat");
            ObjectInputStream ois = new ObjectInputStream(fichier);

            spriteWidth = ois.readInt();
            spriteHeight = ois.readInt();
            int index = ois.readInt();

            for (int i = 0; i < index; i++) {
                String s = ois.readUTF();
                listeRegions.add(s);
            }

        } catch (java.io.IOException e) {
            System.out.println(e);
        }

        for (String s : listeRegions) {
            Texture animationSheet = new Texture(Gdx.files.internal("core/assets/characters/" + persoSelection + "/" + s));
            TextureRegion[][] tmp = new TextureRegion[0][0];
            switch (listeRegions.indexOf(s)) {
                case 5:
                    tmp = TextureRegion.split(animationSheet, spriteWidth + ATTACK_WIDTH, spriteHeight);
                    break;
                case 7:
                case 9:
                    tmp = TextureRegion.split(animationSheet, spriteWidth + ATTACK_HEIGHT, spriteHeight);
                    break;
                default:
                    tmp = TextureRegion.split(animationSheet, spriteWidth, spriteHeight);
                    break;
            }

            animationFrame = new TextureRegion[tmp[0].length];
            for (int i = 0; i < tmp[0].length; i++) {
                animationFrame[i] = tmp[0][i];
            }
            listeAnimations.add(new Animation<TextureRegion>(0.1f, animationFrame));
        }

        stateTime = 0f;
    }

    /**
     * Met la position du bougeable à jour et affiche celui-ci
     *
     * @param batch obet qui affiche tout ce qu'il y a à afficher
     */
    @Override
    public void draw(SpriteBatch batch) {
        float posX = 0;
        stateTime += Gdx.graphics.getDeltaTime();

        updateAnimation();
        posX = findDrawPositionX(this.posX);

        setPosition(posX, posY);
        hitbox.setPosition(this.posX, posY);
        batch.draw(currentFrame, posX, posY);
        healthBar.setPosition(this.posX - healthBar.getLargeur() / 5, posY + currentFrame.getTexture().getHeight() + 1);
        healthBar.draw(batch);
    }

    /**
     * Ajuste la vie du joueur
     *
     * @param o paramètre requis par Java
     * @param o1 paramètre requis par Java
     */
    @Override
    public void update(Observable o, Object o1) {
        healthBar.update(o, o1);
    }

    /**
     * Ajuste la direcion de l'animation selon la direction du joueur
     */
    private void updateAnimation() {
        currentFrame = (TextureRegion) listeAnimations.get(positionEtat()).getKeyFrame(stateTime, true);
        if (directionX < 0 && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (directionX > 0 && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
    }

    /**
     * Ajuste la position du joueur selon l'animation effectuée
     *
     * @param absolutePos position initiale du joueur
     * @return position adaptée à l'animation
     */
    private float findDrawPositionX(float absolutePos) {
        if (etatActuel == EtatJoueur.ATTACK_SIDE && directionX < 0) {
            return absolutePos - ATTACK_WIDTH;
        }
        if ((etatActuel == EtatJoueur.ATTACK_UP
                || etatActuel == EtatJoueur.ATTACK_DOWN)
                && directionX < 0) {
            return absolutePos - ATTACK_HEIGHT;
        }
        return absolutePos;
    }

    /**
     * Trouve la position de l'état actuel dans la liste d'états
     *
     * @return la position de l'état actuel dans la liste d'états
     */
    private int positionEtat() {
        EtatJoueur[] tab = EtatJoueur.values();
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] == etatActuel) {
                return i;
            }
        }
        return -1;
    }

    private float posCentreJoueurX() {
        return posX + Ressource.TILE_SIZE / 2;
    }

    private float posCentreJoueurY() {
        return posY + Ressource.TILE_SIZE / 2;
    }

    private int centreAttaqueX(int width) {
        return width / 2;
    }

    private int centreAttaqueY(int height) {
        return height / 2;
    }

    /**
     * Le joueur subit une attaque
     *
     * @param pwrX la composante x de la puissance de l'attaque
     * @param pwrY la composante y de la puissance de l'attaque
     * @param degats les dégats de l'attaque
     */
    public void subirAttaque(float pwrX, float pwrY, float degats) {
        controleur.subirAttaque(player, pwrX, pwrY, degats);
    }

    @Override
    public int getVie() {
        return player.getVie();
    }

}
