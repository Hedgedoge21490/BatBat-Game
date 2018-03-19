package al.artofsoul.batbatgame.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class Player extends MapObject {
	
	// references
	private ArrayList<Enemy> enemies;
	
	// player stuff
	private int lives;
	private int health;
	private int maxHealth;
	private int damage;
	private int chargeDamage;
	private boolean knockback;
	private boolean flinching;
	private long flinchCount;
	private int score;
	private boolean doubleJump;
	private boolean alreadyDoubleJump;
	private double doubleJumpStart;
	private ArrayList<EnergyParticle> energyParticles;
	private long time;
	
	// actions
	private boolean dashing;
	private boolean attacking;
	private boolean upattacking;
	private boolean charging;
	private int chargingTick;
	private boolean teleporting;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private static final int[] numFrames = {
		1, 8, 5, 3, 3, 5, 3, 8, 2, 1, 3
	};
	private static final int[] frameWidths = {
		40, 40, 80, 40, 40, 40, 80, 40, 40, 40, 40
	};
	private static final int[] frameHights = {
		40, 40, 40, 40, 40, 80, 40, 40, 40, 40, 40
	};
	private static final int[] spriteDelays = {
		-1, 3, 2, 6, 5, 2, 2, 2, 1, -1, 1
	};
	
	private Rectangle ar;
	private Rectangle aur;
	private Rectangle cr;
	
	// animation actions
	private static final int ANIMATIONIDLE = 0;
	private static final int ANIMATIONWALKING = 1;
	private static final int ANIMATIONATTACKING = 2;
	private static final int ANIMATIONJUMPING = 3;
	private static final int ANIMATIONFALLING = 4;
	private static final int ANIMATIONUPATTACKING = 5;
	private static final int ANIMATIONCHARGING = 6;
	private static final int ANIMATIONDASHING = 7;
	private static final int ANIMATIONKNOCKBACK = 8;
	private static final int ANIMATIONDEAD = 9;
	private static final int ANIMATIONTELEPORTING = 10;
	
	// emotes
	private BufferedImage confused;
	private BufferedImage surprised;
	public static final int EMOTENONE = 0;
	public static final int EMOTECONFUSED = 1;
	public static final int EMOTESURPRISED = 2;
	private int emote = EMOTENONE;
	
	public Player(TileMap tm) {
		
		super(tm);
		
		ar = new Rectangle(0, 0, 0, 0);
		ar.width = 30;
		ar.height = 20;
		aur = new Rectangle((int)x - 15, (int)y - 45, 30, 30);
		cr = new Rectangle(0, 0, 0, 0);
		cr.width = 50;
		cr.height = 40;
		
		width = 30;
		height = 30;
		cwidth = 15;
		cheight = 38;
		
		moveSpeed = 1.6;
		maxSpeed = 1.6;
		stopSpeed = 1.6;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		doubleJumpStart = -3;
		
		damage = 2;
		chargeDamage = 1;
		
		facingRight = true;
		
		lives = 3;
		health = maxHealth = 5;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/BatterySpirtes.gif"
				)
			);
			
			int count = 0;
			sprites = new ArrayList<>();
			for(int i = 0; i < numFrames.length; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
					bi[j] = spritesheet.getSubimage(
						j * frameWidths[i],
						count,
						frameWidths[i],
						frameHights[i]
					);
				}
				sprites.add(bi);
				count += frameHights[i];
			}
			
			// emotes
			spritesheet = ImageIO.read(getClass().getResourceAsStream(
				"/HUD/Emotes.gif"
			));
			confused = spritesheet.getSubimage(
				0, 0, 14, 17
			);
			surprised = spritesheet.getSubimage(
				14, 0, 14, 17
			);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		energyParticles = new ArrayList<>();
		
		setAnimation(ANIMATIONIDLE);
		
		JukeBox.playerJump();
		JukeBox.load("/SFX/playerlands.mp3", "playerlands");
		JukeBox.playerAttack();
		JukeBox.load("/SFX/playerhit.mp3", "playerhit");
		JukeBox.load("/SFX/playercharge.mp3", "playercharge");
		
	}
	
	public void init(
		ArrayList<Enemy> enemies,
		ArrayList<EnergyParticle> energyParticles) {
		this.enemies = enemies;
		this.energyParticles = energyParticles;
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	
	public void setEmote(int i) {
		emote = i;
	}
	public void setTeleporting(boolean b) { teleporting = b; }

	@Override
	public void setJumping(boolean b) {
		if(knockback) return;
		if(b && !jumping && falling && !alreadyDoubleJump) {
			doubleJump = true;
		}
		jumping = b;
	}
	public void setAttacking() {
		if(knockback) return;
		if(charging) return;
		if(up && !attacking) upattacking = true;
		else attacking = true;
	}
	public void setCharging() {
		if(knockback) return;
		if(!attacking && !upattacking && !charging) {
			charging = true;
			JukeBox.play("playercharge");
			chargingTick = 0;
		}
	}

	public void setDashing(boolean b) {

		if(!b) dashing = false;

		else if(!falling) {
			dashing = true;
		}
	}

	public boolean isDashing() { return dashing; }
	
	public void setDead() {
		health = 0;
		stop();
	}
	
	public String getTimeToString() {
		int minutes = (int) (time / 3600);
		int seconds = (int) ((time % 3600) / 60);
		return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}
	public long getTime() { return time; }
	public void setTime(long t) { time = t; }
	public void setHealth(int i) { health = i; }
	public void setLives(int i) { lives = i; }
	public void gainLife() { lives++; }
	public void loseLife() { lives--; }
	public int getLives() { return lives; }
	
	public void increaseScore(int score) {
		this.score += score; 
	}
	
	public int getScore() { return score; }
	
	public void hit(int damage) {
		if(flinching) return;
		JukeBox.play("playerhit");
		stop();
		health -= damage;
		if(health < 0) health = 0;
		flinching = true;
		flinchCount = 0;
		if(facingRight) dx = -1;
		else dx = 1;
		dy = -3;
		knockback = true;
		falling = true;
		jumping = false;
	}
	
	public void reset() {
		health = maxHealth;
		facingRight = true;
		currentAction = -1;
		stop();
	}
	
	public void stop() {
		left = right = up = down = flinching = 
			dashing = jumping = attacking = upattacking = charging = false;
	}

	private void nextPositionNonMovement(){
		if(dx > 0) {
			dx -= stopSpeed;
			if(dx < 0) {
				dx = 0;
			}
		}
		else{
			dx += stopSpeed;
			if(dx > 0) {
				dx = 0;
			}
		}
	}
	private void nextPositionMovement(){

		double maxSpeed = this.maxSpeed;
		if(dashing) maxSpeed *= 1.75;

		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			nextPositionNonMovement();
		}
	}
	private void nextPositionJumping(){
		// jumping
		if(jumping && !falling) {
			dy = jumpStart;
			falling = true;
			JukeBox.playerJump();
		}

		if(doubleJump) {
			dy = doubleJumpStart;
			alreadyDoubleJump = true;
			doubleJump = false;
			JukeBox.playerJump();
			for(int i = 0; i < 6; i++) {
				energyParticles.add(
						new EnergyParticle(
								tileMap,
								x,
								y + cheight / 4.0,
								EnergyParticle.downdir));
			}
		}

		if(!falling) alreadyDoubleJump = false;
	}
	private void nextPositionCharging(){
		// charging
		if(charging) {
			chargingTick++;
			if(facingRight) dx = moveSpeed * (3 - chargingTick * 0.07);
			else dx = -moveSpeed * (3 - chargingTick * 0.07);
		}
	}
	private void cannotMoveWhileAttacking(){
		// cannot move while attacking, except in air
		if((attacking || upattacking || charging) &&
				!(jumping || falling)) {
			dx = 0;
		}
	}
	public void nextPositionKnockback(){
		if(knockback) {
			dy += fallSpeed * 2;
			if(!falling) knockback = false;
			return;
		}
	}
	public void nextPOsitionFalling(){
		// falling
		if(falling) {
			dy += fallSpeed;
			if(dy < 0 && !jumping) dy += stopJumpSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}

	private void getNextPosition() {

		nextPositionKnockback();

		nextPositionMovement();

		cannotMoveWhileAttacking();

		nextPositionCharging();

		nextPositionJumping();

		nextPOsitionFalling();
		
	}

	//Hier beginnt der Teil mit dem Update!

	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(spriteDelays[currentAction]);
		width = frameWidths[currentAction];
		height = frameHights[currentAction];
	}

	public void checkTeleporting(){
		// check teleporting
		if(teleporting) {
			energyParticles.add(
					new EnergyParticle(tileMap, x, y, EnergyParticle.updir)
			);
		}
	}
	public void updatePosition(){
		// update position
		boolean isFalling = falling;
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(isFalling && !falling) {
			JukeBox.play("playerlands");
		}
		if(dx == 0) x = (int)x;
	}
	public void checkDoneFlinching(){
		// check done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount > 120) {
				flinching = false;
			}
		}
	}
	public void updateEnergyParticles(){
		// energy particles
		for(int i = 0; i < energyParticles.size(); i++) {
			energyParticles.get(i).update();
			if(energyParticles.get(i).shouldRemove()) {
				energyParticles.remove(i);
				i--;
			}
		}
	}
	public void checkAttackFinished(){
		// check attack finished
		if(currentAction == ANIMATIONATTACKING ||
				currentAction == ANIMATIONUPATTACKING &&
						animation.hasPlayedOnce())
		{
			attacking = false;
			upattacking = false;
		}
		if(currentAction == ANIMATIONCHARGING) {
			if(animation.hasPlayed(5)) {
				charging = false;
			}
			cr.y = (int)y - 20;
			if(facingRight) cr.x = (int)x - 15;
			else cr.x = (int)x - 35;
			if(facingRight)
				energyParticles.add(
						new EnergyParticle(
								tileMap,
								x + 30,
								y,
								EnergyParticle.rightdir));
			else
				energyParticles.add(
						new EnergyParticle(
								tileMap,
								x - 30,
								y,
								EnergyParticle.leftdir));
		}
	}

	public void checkAttacks(Enemy e){

		// check attack
		if(currentAction == ANIMATIONATTACKING &&
				animation.getFrame() == 3 && animation.getCount() == 0 &&
					e.intersects(ar)) {
				e.hit(damage);
		}

		// check upward attack
		if(currentAction == ANIMATIONUPATTACKING &&
				animation.getFrame() == 3 && animation.getCount() == 0 &&
					e.intersects(aur)) {
				e.hit(damage);
		}

		// check charging attack
		if(currentAction == ANIMATIONCHARGING && animation.getCount() == 0 && e.intersects(cr)) {
			e.hit(chargeDamage);
		}
	}

	public void checkEnemyInteraction(){
		// check enemy interaction
		for(int i = 0; i < enemies.size(); i++) {

			Enemy e = enemies.get(i);

			checkAttacks(e);

			// collision with enemy
			if(!e.isDead() && intersects(e) && !charging) {
				hit(e.getDamage());
			}

			if(e.isDead()) {
				JukeBox.play("explode", 2000);
			}

		}
	}

	public void updateAnimationUpattacking(){
		if(currentAction != ANIMATIONUPATTACKING) {
			JukeBox.playerAttack();
			setAnimation(ANIMATIONUPATTACKING);
			aur.x = (int)x - 15;
			aur.y = (int)y - 50;
		}
		else {
			if(animation.getFrame() == 4 && animation.getCount() == 0) {
				for(int c = 0; c < 3; c++) {
					energyParticles.add(
							new EnergyParticle(
									tileMap,
									aur.x + aur.width / 2,
									aur.y + 5,
									EnergyParticle.updir));
				}
			}
		}
	}

	public void updateAnimationAttacking(){
		if(currentAction != ANIMATIONATTACKING) {
			JukeBox.playerAttack();
			setAnimation(ANIMATIONATTACKING);
			ar.y = (int)y - 6;
			if(facingRight) ar.x = (int)x + 10;
			else ar.x = (int)x - 40;
		}
		else {
			if(animation.getFrame() == 4 && animation.getCount() == 0) {
				for(int c = 0; c < 3; c++) {
					if(facingRight)
						energyParticles.add(
								new EnergyParticle(
										tileMap,
										ar.x + ar.width - 4,
										ar.y + ar.height / 2,
										EnergyParticle.rightdir));
					else
						energyParticles.add(
								new EnergyParticle(
										tileMap,
										ar.x + 4,
										ar.y + ar.height / 2,
										EnergyParticle.leftdir));
				}}
		}
	}

	public void updateAnimationTeleporting(){
		if(currentAction != ANIMATIONTELEPORTING) {
			setAnimation(ANIMATIONTELEPORTING);
		}
	}

	public void updateAnimationKnockback(){
		if(currentAction != ANIMATIONKNOCKBACK) {
			setAnimation(ANIMATIONKNOCKBACK);
		}
	}

	public void updateAnimationDead(){
		if(currentAction != ANIMATIONDEAD) {
			setAnimation(ANIMATIONDEAD);
		}
	}

	public void update() {
		
		time++;
		checkTeleporting();
		updatePosition();
		checkDoneFlinching();
		updateEnergyParticles();
		checkAttackFinished();
		checkEnemyInteraction();

		
		// set animation, ordered by priority
		if(teleporting) {
			updateAnimationTeleporting();
		}
		else if(knockback) {
			updateAnimationKnockback();
		}
		else if(health == 0) {
			updateAnimationDead();
		}
		else if(upattacking) {
			updateAnimationUpattacking();
		}
		else if(attacking) {
			updateAnimationAttacking();
		}
		else if(charging) {
			if(currentAction != ANIMATIONCHARGING) {
				setAnimation(ANIMATIONCHARGING);
			}
		}
		else if(dy < 0) {
			if(currentAction != ANIMATIONJUMPING) {
				setAnimation(ANIMATIONJUMPING);
			}
		}
		else if(dy > 0) {
			if(currentAction != ANIMATIONFALLING) {
				setAnimation(ANIMATIONFALLING);
			}
		}
		else if(dashing && (left || right)) {
			if(currentAction != ANIMATIONDASHING) {
				setAnimation(ANIMATIONDASHING);
			}
		}
		else if(left || right) {
			if(currentAction != ANIMATIONWALKING) {
				setAnimation(ANIMATIONWALKING);
			}
		}
		else if(currentAction != ANIMATIONIDLE) {
			setAnimation(ANIMATIONIDLE);
		}
		
		animation.update();
		
		// set direction
		if(!attacking && !upattacking && !charging && !knockback) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		// draw emote
		if(emote == EMOTECONFUSED) {
			g.drawImage(confused, (int)(x + xmap - cwidth / 2.0), (int)(y + ymap - 40), null);
		}
		else if(emote == EMOTESURPRISED) {
			g.drawImage(surprised, (int)(x + xmap - cwidth / 2.0), (int)(y + ymap - 40), null);
		}
		
		// draw energy particles
		for(int i = 0; i < energyParticles.size(); i++) {
			energyParticles.get(i).draw(g);
		}
		
		// flinch
		if(flinching && !knockback) {
			if(flinchCount % 10 < 5) return;
		}
		
		super.draw(g);
		
	}
	
}