package al.artofsoul.batbatgame.entity;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.tilemap.TileMap;

import java.awt.*;

/**
 * @author ArtOfSoul
 *
 */

public class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected boolean remove;
	
	protected boolean flinching;
	protected long flinchCount;
	
	public Enemy(TileMap tm) {
		super(tm);
		remove = false;
	}
	
	public boolean isDead() { return dead; }
	public boolean shouldRemove() { return remove; }
	
	public int getDamage() { return damage; }
	
	public void hit(int damage) {
		if(dead || flinching) return;
		JukeBox.play("enemyhit");
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		if(dead) remove = true;
		flinching = true;
		flinchCount = 0;
	}

	public void getNextPosition() {
		if(left) dx = -moveSpeed;
		else if(right) dx = moveSpeed;
		else dx = 0;
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		if(jumping && !falling) {
			dy = jumpStart;
		}
	}

	public void draw(Graphics2D g) {
		if(flinching && (flinchCount == 0 || flinchCount == 2)) {
			return;
		}
		super.draw(g);
	}

	public void update() {//Method empty, still gets invoked in Level4State.java
	}
}














