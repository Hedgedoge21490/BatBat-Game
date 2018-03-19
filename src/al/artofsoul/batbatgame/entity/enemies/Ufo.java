package al.artofsoul.batbatgame.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import al.artofsoul.batbatgame.entity.Enemy;
import al.artofsoul.batbatgame.entity.Player;
import al.artofsoul.batbatgame.handlers.Content;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class Ufo extends Enemy {
	
	private Player player;
	private ArrayList<Enemy> enemies;
	
	private BufferedImage[] idleSprites;
	private BufferedImage[] jumpSprites;
	private BufferedImage[] attackSprites;
	
	private boolean isJumping;
	
	private static final int IDLE = 0;
	private static final int JUMPING = 1;
	private static final int ATTACKING = 2;
	
	private int attackTick;
	private int attackDelay = 30;
	private int step;
	
	public Ufo(TileMap tm, Player p, ArrayList<Enemy> en) {
		
		super(tm);
		player = p;
		enemies = en;
		
		health = maxHealth = 4;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 26;
		
		damage = 1;
		moveSpeed = 1.5;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5;
		
		idleSprites = Content.Ufo[0];
		jumpSprites = Content.Ufo[1];
		attackSprites = Content.Ufo[2];
		
		animation.setFrames(idleSprites);
		animation.setDelay(-1);
		
		attackTick = 0;
		
	}


	private void getNextPosition() {
		if(left) dx = -moveSpeed;
		else if(right) dx = moveSpeed;
		else dx = 0;
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		if(isJumping && !falling) {
			dy = jumpStart;
		}
	}



	public void checkIfDoneFlinching(){
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
	}

	public void idleUfo(){
		if(step == 0) {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(idleSprites);
				animation.setDelay(-1);
			}
			attackTick++;
			if(attackTick >= attackDelay && Math.abs(player.getx() - x) < 60) {
				step++;
				attackTick = 0;
			}
		}
	}

	public void jumpAwayUfo(){
		if(step == 1) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(jumpSprites);
				animation.setDelay(-1);
			}
			isJumping = true;
			if(facingRight) left = true;
			else right = true;
			if(falling) {
				step++;
			}
		}
	}

	public void attackUfo(){

		if(step == 2) {
			if(dy > 0 && currentAction != ATTACKING) {
				currentAction = ATTACKING;
				animation.setFrames(attackSprites);
				animation.setDelay(3);
				RedEnergy de = new RedEnergy(tileMap);
				de.setPosition(x, y);
				if(facingRight) de.setVector(3, 3);
				else de.setVector(-3, 3);
				enemies.add(de);
			}
			if(currentAction == ATTACKING && animation.hasPlayedOnce()) {
				step++;
				currentAction = JUMPING;
				animation.setFrames(jumpSprites);
				animation.setDelay(-1);
			}
		}
	}

	@Override
	public void update() {

		checkIfDoneFlinching();
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
		
		if(player.getx() < x) facingRight = false;
		else facingRight = true;

		idleUfo();
		jumpAwayUfo();
		attackUfo();

		// done attacking
		if(step == 3 && dy == 0) {
			step++;
		}
		// land
		if(step == 4) {
			step = 0;
			left = right = isJumping = false;
		}
		
	}


	@Override
	public void draw(Graphics2D g) {
		if(flinching && (flinchCount == 0 || flinchCount == 2)) {
			return;
		}
		super.draw(g);
	}

}
