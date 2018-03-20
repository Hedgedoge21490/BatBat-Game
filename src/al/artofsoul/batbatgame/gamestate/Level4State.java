package al.artofsoul.batbatgame.gamestate;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.*;
import al.artofsoul.batbatgame.entity.batbat.BottomLeftPiece;
import al.artofsoul.batbatgame.entity.batbat.BottomRightPiece;
import al.artofsoul.batbatgame.entity.batbat.TopLeftPiece;
import al.artofsoul.batbatgame.entity.batbat.TopRightPiece;
import al.artofsoul.batbatgame.entity.enemies.RedEnergy;
import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.main.GamePanel;


import java.awt.*;
import java.util.ArrayList;

/**
 * @author ArtOfSoul
 */

public class Level4State extends GameState {

    private String lvlboss = "level1boss";

    private Spirit spirit;

    // events
    private boolean flash;
    private boolean eventBossDead;

    public Level4State(GameStateManager gsm) {
        super(gsm,4);
        init();
    }

    public void init() {

        populateEnemies();

        player.init(enemies, energyParticles);

        // angelspop
        tlp = new TopLeftPiece(tileMap);
        trp = new TopRightPiece(tileMap);
        blp = new BottomLeftPiece(tileMap);
        brp = new BottomRightPiece(tileMap);
        tlp.setPosition(152, 102);
        trp.setPosition(162, 102);
        blp.setPosition(152, 112);
        brp.setPosition(162, 112);

        // start event
        eventStart = blockInput = true;
        tb = new ArrayList<Rectangle>();
        eventStart();

        // music
        JukeBox.load("/Music/level1boss.mp3", lvlboss);
    }

    private void populateEnemies() {
        enemies.clear();
        spirit = new Spirit(tileMap, player, enemies, explosions);
        spirit.setPosition(-9000, 9000);
        enemies.add(spirit);
    }

    @Override
    public void update() {
        super.update();

        // check if boss dead event should start
        if (!eventFinish && spirit.isDead()) {
            eventBossDead = blockInput = true;
        }

        // play events
        if (eventDead) eventDead();
        if (eventPortal) eventPortal();
        if (eventBossDead) eventBossDead();

        // move backgrounds
        temple.setPosition(tileMap.getx(), tileMap.gety());

        // update portal
        portal.update();
        // update Portal Thingi
        tlp.update();
        trp.update();
        blp.update();
        brp.update();
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        // flash
        if (flash) {
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        }
    }


///////////////////////////////////////////////////////
//////////////////// EVENTS
///////////////////////////////////////////////////////

    // reset level
    private void reset() {
        player.reset();
        player.setPosition(50, 190);
        populateEnemies();
        eventStart = blockInput = true;
        eventCount = 0;
        super.eventStart();
    }

    // player has died
    private void eventDead() {
        eventCount++;
        if (eventCount == 1) {
            player.setDead();
            player.stop();
        }
        if (eventCount == 60) {
            tb.clear();
            tb.add(new Rectangle(
                    GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
        } else if (eventCount > 60) {
            tb.get(0).x -= 6;
            tb.get(0).y -= 4;
            tb.get(0).width += 12;
            tb.get(0).height += 8;
        }
        if (eventCount >= 120) {
            if (player.getLives() == 0) {
                gsm.setState(GameStateManager.MENUSTATE);
            } else {
                eventDead = blockInput = false;
                eventCount = 0;
                player.loseLife();
                reset();
            }
        }
    }

    private void eventPortal() {
        eventCount++;
        if (eventCount == 1 && portal.isOpened()) {
                eventCount = 360;
        }
        if (eventCount > 60 && eventCount < 180) {
            energyParticles.add(
                    new EnergyParticle(tileMap, 157, 107, (int) (Math.random() * 4)));
        }
        if (eventCount >= 160 && eventCount <= 180) {
            flash = eventCount % 4 == 0 || eventCount % 4 == 1;
        }
        if (eventCount == 181) {
            tileMap.setShaking(false, 0);
            flash = false;
            tlp.setVector(-0.3, -0.3);
            trp.setVector(0.3, -0.3);
            blp.setVector(-0.3, 0.3);
            brp.setVector(0.3, 0.3);
            player.setEmote(Player.EMOTESURPRISED);
        }
        if (eventCount == 240) {
            tlp.setVector(0, -5);
            trp.setVector(0, -5);
            blp.setVector(0, -5);
            brp.setVector(0, -5);
        }
        if (eventCount == 300) {
            player.setEmote(Player.EMOTENONE);
            portal.setOpening();
        }
        if (eventCount == 360) {
            flash = true;
            spirit.setPosition(160, 160);
            RedEnergy de;
            for (int i = 0; i < 20; i++) {
                de = new RedEnergy(tileMap);
                de.setPosition(160, 160);
                de.setVector(Math.random() * 10 - 5, Math.random() * -2 - 3);
                enemies.add(de);
            }
        }
        if (eventCount == 362) {
            flash = false;
            JukeBox.loop(
                    lvlboss,
                    0,
                    60000,
                    JukeBox.getFrames(lvlboss) - 4000
            );
        }
        if (eventCount == 420) {
            eventPortal = blockInput = false;
            eventCount = 0;
            spirit.setActive();
        }

    }

    public void eventBossDead() {
        eventCount++;
        if (eventCount == 1) {
            player.stop();
            JukeBox.stop(lvlboss);
            enemies.clear();
        }
        if (eventCount <= 120 && eventCount % 15 == 0) {
            explosions.add(new Explosion(tileMap, spirit.getx(), spirit.gety()));
            JukeBox.play("explode");
        }
        if (eventCount == 180) {
            JukeBox.play("fanfare");
        }
        if (eventCount == 390) {
            eventBossDead = false;
            eventCount = 0;
            eventFinish = true;
        }
    }
}