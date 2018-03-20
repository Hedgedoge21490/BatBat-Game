package al.artofsoul.batbatgame.gamestate;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.*;
import al.artofsoul.batbatgame.entity.enemies.Ufo;
import al.artofsoul.batbatgame.entity.enemies.XhelBat;
import al.artofsoul.batbatgame.entity.enemies.Zogu;
import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.main.GamePanel;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author ArtOfSoul
 */

public class Level3State extends GameState {
    // events
    private boolean eventQuake;

    protected String lvl2 = "level2";

    public Level3State(GameStateManager gsm) {
        super(gsm, 3);
        init();
    }

    public void init() {

        populateEnemies();
        player.init(enemies, energyParticles);

        // start event
        eventStart = true;
        tb = new ArrayList<Rectangle>();
        eventStart();

        // music
        JukeBox.load("/Music/level1v2.mp3", lvl2);
        JukeBox.loop(lvl2, 600, JukeBox.getFrames(lvl2) - 2200);

    }

    private void populateEnemies() {
        enemies.clear();
        XhelBat gp;
        Zogu g;
        Ufo t;

        gp = new XhelBat(tileMap, player);
        gp.setPosition(750, 100);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(900, 150);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(1320, 250);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(1570, 160);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(1590, 160);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(2600, 370);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(2620, 370);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(2640, 370);
        enemies.add(gp);

        g = new Zogu(tileMap);
        g.setPosition(904, 130);
        enemies.add(g);
        g = new Zogu(tileMap);
        g.setPosition(1080, 270);
        enemies.add(g);
        g = new Zogu(tileMap);
        g.setPosition(1200, 270);
        enemies.add(g);
        g = new Zogu(tileMap);
        g.setPosition(1704, 300);
        enemies.add(g);

        t = new Ufo(tileMap, player, enemies);
        t.setPosition(1900, 580);
        enemies.add(t);
        t = new Ufo(tileMap, player, enemies);
        t.setPosition(2330, 550);
        enemies.add(t);
        t = new Ufo(tileMap, player, enemies);
        t.setPosition(2400, 490);
        enemies.add(t);
        t = new Ufo(tileMap, player, enemies);
        t.setPosition(2457, 430);
        enemies.add(t);

    }

    @Override
    public void update() {
        super.update();

        // play events
        if (eventDead) eventDead();
        if (eventQuake) eventQuake();

        // check if quake event should start
        if (player.getx() > 2175 && !tileMap.isShaking()) {
            eventQuake = blockInput = true;
        }

        // move title and subtitle
        if (title != null) {
            title.update();
            if (title.shouldRemove()) title = null;
        }
        if (subtitle != null) {
            subtitle.update();
            if (subtitle.shouldRemove()) subtitle = null;
        }

        // move backgrounds
        temple.setPosition(tileMap.getx(), tileMap.gety());



    }

    public void draw(Graphics2D g) {

        // draw background
        temple.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        // draw enemy projectiles
        for (int i = 0; i < eprojectiles.size(); i++) {
            eprojectiles.get(i).draw(g);
        }

        // draw explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).draw(g);
        }

        // draw player
        player.draw(g);

        // draw teleport
        teleport.draw(g);

        // draw hud
        hud.draw(g);

        // draw title
        if (title != null) title.draw(g);
        if (subtitle != null) subtitle.draw(g);

        // draw transition boxes
        g.setColor(java.awt.Color.BLACK);
        for (int i = 0; i < tb.size(); i++) {
            g.fill(tb.get(i));
        }

    }

    public void handleInput() {
        if (Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(true);
        if (blockInput || player.getHealth() == 0) return;
        player.setUp(Keys.keyState[Keys.UP]);
        player.setLeft(Keys.keyState[Keys.LEFT]);
        player.setDown(Keys.keyState[Keys.DOWN]);
        player.setRight(Keys.keyState[Keys.RIGHT]);
        player.setJumping(Keys.keyState[Keys.BUTTON1]);
        player.setDashing(Keys.keyState[Keys.BUTTON2]);
        if (Keys.isPressed(Keys.BUTTON3)) player.setAttacking();
        if (Keys.isPressed(Keys.BUTTON4)) player.setCharging();
    }

///////////////////////////////////////////////////////
//////////////////// EVENTS
///////////////////////////////////////////////////////

    // reset level
    private void reset() {
        player.loseLife();
        player.reset();
        player.setPosition(300, 131);
        populateEnemies();
        blockInput = true;
        eventCount = 0;
        tileMap.setShaking(false, 0);
        eventStart = true;
        eventStart();
        title = new Title(batBatStart.getSubimage(0, 0, 178, 20));
        title.sety(60);
        subtitle = new Title(batBatStart.getSubimage(0, 33, 91, 13));
        subtitle.sety(85);
    }



    // player has died
    private void eventDead() {
        eventCount++;
        if (eventCount == 1) player.setDead();
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
                reset();
            }
        }
    }

    // earthquake
    private void eventQuake() {
        eventCount++;
        if (eventCount == 1) {
            player.stop();
            player.setPosition(2175, player.gety());
        }
        if (eventCount == 60) {
            player.setEmote(Player.EMOTECONFUSED);
        }
        if (eventCount == 120) player.setEmote(Player.EMOTENONE);
        if (eventCount == 150) tileMap.setShaking(true, 10);
        if (eventCount == 180) player.setEmote(Player.EMOTESURPRISED);
        if (eventCount == 300) {
            player.setEmote(Player.EMOTENONE);
            eventQuake = blockInput = false;
            eventCount = 0;
        }
    }


}
