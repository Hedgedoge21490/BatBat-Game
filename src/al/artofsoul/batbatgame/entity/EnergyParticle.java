package al.artofsoul.batbatgame.entity;

import java.awt.image.BufferedImage;

import al.artofsoul.batbatgame.handlers.Content;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class EnergyParticle extends MapObject {
	
	private int count;
	private boolean remove;
	
	private BufferedImage[] sprites;
	
	public static final int UPDIR = 0;
	public static final int LEFTDIR = 1;
	public static final int DOWNDIR = 2;
	public static final int RIGHTDIR = 3;
	
	public EnergyParticle(TileMap tm, double x, double y, int dir) {
		super(tm);
		this.x = x;
		this.y = y;
		double d1 = Math.random() * 2.5 - 1.25;
		double d2 = -Math.random() - 0.8; 
		if(dir == UPDIR) {
			dx = d1;
			dy = d2;
		}
		else if(dir == LEFTDIR) {
			dx = d2;
			dy = d1;
		}
		else if(dir == DOWNDIR) {
			dx = d1;
			dy = -d2;
		}
		else {
			dx = -d2;
			dy = d1;
		}
		
		count = 0;
		sprites = Content.EnergyParticle[0];
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	public void update() {
		x += dx;
		y += dy;
		count++;
		if(count == 60) remove = true;
	}
	
	public boolean shouldRemove() { return remove; }
	
	//Draw-Methode wird geerbt.
	
}
