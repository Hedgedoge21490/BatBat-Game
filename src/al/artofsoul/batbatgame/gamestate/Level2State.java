package al.artofsoul.batbatgame.gamestate;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.*;
import al.artofsoul.batbatgame.entity.enemies.XhelBat;
import al.artofsoul.batbatgame.entity.enemies.Zogu;
import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.main.GamePanel;


import java.awt.*;
import java.util.ArrayList;

/**
 * @author ArtOfSoul
 */

public class Level2State extends GameState {

    protected String lvl1 = "level1";

    public Level2State(GameStateManager gsm) {
        super(gsm, 2);
        init();
    }

    public void init() {

        populateEnemies();

        // init player
        player.init(enemies, energyParticles);

        // start event
        eventStart = true;
        tb = new ArrayList<Rectangle>();
        eventStart();

        //music
        JukeBox.load("/Music/level1.mp3", lvl1);
        JukeBox.loop(lvl1, 600, JukeBox.getFrames(lvl1) - 2200);

    }

    private void populateEnemies() {
        enemies.clear();

        XhelBat gp;
        Zogu g;

        gp = new XhelBat(tileMap, player);
        gp.setPosition(1300, 100);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(1320, 100);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(1340, 100);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(1660, 100);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(1680, 100);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(1700, 100);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(2177, 100);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(2960, 100);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(2980, 100);
        enemies.add(gp);
        gp = new XhelBat(tileMap, player);
        gp.setPosition(3000, 100);
        enemies.add(gp);

        g = new Zogu(tileMap);
        g.setPosition(2600, 100);
        enemies.add(g);
        g = new Zogu(tileMap);
        g.setPosition(3500, 100);
        enemies.add(g);
    }

    @Override
    public void update() {
        super.update();

        // play events
        if (eventDead)
            eventDead();

        // move backgrounds
        perendimi.setPosition(tileMap.getx(), tileMap.gety());
        mountains.setPosition(tileMap.getx(), tileMap.gety());
    }

    ///////////////////////////////////////////////////////
    //////////////////// EVENTS
    ///////////////////////////////////////////////////////

    // reset level
    private void reset() {
        player.reset();
        player.setPosition(300, 161);
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
}
