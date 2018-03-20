package al.artofsoul.batbatgame.entity.enemies;

import java.awt.image.BufferedImage;

import al.artofsoul.batbatgame.entity.Enemy;
import al.artofsoul.batbatgame.handlers.Content;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class RedEnergy extends Enemy {
	
	private BufferedImage[] startSprites;
	private BufferedImage[] sprites;



	private boolean start;
	private boolean permanent;
	
	private int type = 0;
	public static final int VECTOR = 0;
	public static final int GRAVITY = 1;
	public static final int BOUNCE = 2;
	
	private int bounceCount = 0;
	
	public RedEnergy(TileMap tm) {
		super(tm);
		drawFlag = 1;

		health = maxHealth = 1;
		
		width = 20;
		height = 20;
		cwidth = 12;
		cheight = 12;
		
		damage = 1;
		moveSpeed = 5;
		
		startSprites = Content.RedEnergy[0];
		sprites = Content.RedEnergy[1];
		
		animation.setFrames(startSprites);
		animation.setDelay(2);
		
		start = true;
		flinching = true;
		permanent = false;
		
	}
	
	public void setType(int i) { type = i; }
	public void setPermanent(boolean b) { permanent = b; }
	
	public void updateAnimation(){
		animation.update();

		if(!permanent) {
			if(x < 0 || x > tileMap.getWidth() || y < 0 || y > tileMap.getHeight()) {
				remove = true;
			}
			if(bounceCount == 3) {
				remove = true;
			}
		}
	}

	@Override
	public void update() {
		
		if(start && animation.hasPlayedOnce()) {
				animation.setFrames(sprites);
				animation.setNumFrames(3);
				animation.setDelay(2);
				start = false;
		}
		
		if(type == VECTOR) {
			x += dx;
			y += dy;
		}
		else if(type == GRAVITY) {
			dy += 0.2;
			x += dx;
			y += dy;
		}
		else if(type == BOUNCE) {
			double dx2 = dx;
			double dy2 = dy;
			checkTileMapCollision();
			if(dx == 0) {
				dx = -dx2;
				bounceCount++;
			}
			if(dy == 0) {
				dy = -dy2;
				bounceCount++;
			}
			x += dx;
			y += dy;
		}

		updateAnimation();
		
	}


}
