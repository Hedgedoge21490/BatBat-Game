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

public class RedEnergy extends Enemy {
	
	private BufferedImage[] startSprites;
	private BufferedImage[] sprites;
	
	private boolean start;
	private boolean permanent;
	
	private int type = 0;
	public static int vector = 0;
	public static int gravity = 1;
	public static int bounce = 2;
	
	private int bounceCount = 0;
	
	public RedEnergy(TileMap tm) {
		
		super(tm);
		
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
		
		if(type == vector) {
			x += dx;
			y += dy;
		}
		else if(type == gravity) {
			dy += 0.2;
			x += dx;
			y += dy;
		}
		else if(type == bounce) {
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

	@Override
	public void draw(java.awt.Graphics2D g) {
		setMapPosition();
		if(facingRight) {
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2.0),
					(int)(y + ymap - height / 2.0),
					null
			);
		}
		else {
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2.0 + width),
					(int)(y + ymap - height / 2.0),
					-width,
					height,
					null
			);
		}
	}

}
