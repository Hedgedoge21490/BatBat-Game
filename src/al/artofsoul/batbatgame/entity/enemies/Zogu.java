package al.artofsoul.batbatgame.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import al.artofsoul.batbatgame.entity.Enemy;
import al.artofsoul.batbatgame.handlers.Content;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class Zogu extends Enemy {
	
	private BufferedImage[] idleSprites;
	
	private int tick;
	private double a;
	private double b;
	
	public Zogu(TileMap tm) {
		
		super(tm);
		
		health = maxHealth = 2;
		
		width = 39;
		height = 20;
		cwidth = 25;
		cheight = 15;
		
		damage = 1;
		moveSpeed = 5;
		
		idleSprites = Content.Zogu[0];
		
		animation.setFrames(idleSprites);
		animation.setDelay(4);
		
		tick = 0;
		a = Math.random() * 0.06 + 0.07;
		b = Math.random() * 0.06 + 0.07;
		
	}

	@Override
	public void update() {
		
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
		
		tick++;
		x = Math.sin(a * tick) + x;
		y = Math.sin(b * tick) + y;
		
		// update animation
		animation.update();
		
	}

}
