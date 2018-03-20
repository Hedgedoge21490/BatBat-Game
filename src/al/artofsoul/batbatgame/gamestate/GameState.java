package al.artofsoul.batbatgame.gamestate;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.*;
import al.artofsoul.batbatgame.main.GamePanel;
import al.artofsoul.batbatgame.tilemap.Background;
import al.artofsoul.batbatgame.tilemap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * @author ArtOfSoul
 *
 */

public abstract class GameState {
	
	protected GameStateManager gsm;

	protected String tileType = "/Tilesets/ruinstileset.gif";
	protected String tp = "teleport";

	//For Levelstates
	protected int level;

	//All Backgrounds
	protected Background sky;
	protected Background clouds;
	protected Background mountains;
	protected Background mountains2;
	protected Background perendimi;
	protected Background temple;

	//Titel und Untertitel Positionen für Level 1-3
	protected static final int[][] TITLEPOSITIONS = {
			{0, 0, 178, 19},
			{0, 0, 178, 20},
			{0, 0, 178, 20}
	};
	protected static final int[][] SUBTITLEPOSITIONS = {
			{0, 20, 82, 13},
			{0, 33, 91, 13},
			{0, 33, 91, 13}
	};

	protected Player player;
	protected TileMap tileMap;
	protected ArrayList<Enemy> enemies;
	protected ArrayList<EnemyProjectile> eprojectiles;
	protected ArrayList<EnergyParticle> energyParticles;
	protected ArrayList<Explosion> explosions;

	protected HUD hud;
	protected BufferedImage batBatStart;
	protected Title title;
	protected Title subtitle;
	protected Teleport teleport;

	//Level 4 specific
	protected Portal portal;
	protected boolean eventPortal;

	// events
	protected boolean blockInput = false;
	protected int eventCount = 0;
	protected boolean eventStart;
	protected ArrayList<Rectangle> tb;
	protected boolean eventFinish;
	protected boolean eventDead;

	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	//Überladener Konstruktor spezifisch für Level
	public GameState(GameStateManager gsm, int level){
		this.gsm = gsm;
		this.level = level;
		init(level);
	}

	
	public void init(int level){

		//backgrounds
		sky = new Background("/Backgrounds/qielli.gif", 0);
		clouds = new Background("/Backgrounds/rete.gif", 0.1);
		mountains = new Background("/Backgrounds/mali.gif", 0.2);
		perendimi = new Background("/Backgrounds/perendimi.gif", 0.5, 0);
		mountains2 = new Background("/Backgrounds/mali2.gif", 0.2);
		temple = new Background("/Backgrounds/temple.gif", 0.5, 0);

		//TileMap Init Levelspezifisch

		//Player Init Levelspezifisch

		switch(level){
			case 1:
				tileMap = new TileMap(30, tileType, "/Maps/level1.map",0,120,1);
				tileMap.setBounds(
						tileMap.getWidth() - 1 * tileMap.getTileSize(),
						tileMap.getHeight() - 2 * tileMap.getTileSize(),
						0, 0
				);
				player = new Player(tileMap,140,191);
				teleport = new Teleport(tileMap);
				teleport.setPosition(3700, 131);
				break;
			case 2:
				tileMap = new TileMap(30, tileType, "/Maps/level2.map", 140,0,1);
				tileMap.setBounds(tileMap.getWidth() - 1 * tileMap.getTileSize(),
						tileMap.getHeight() - 2 * tileMap.getTileSize(), 0, 0);
				player = new Player(tileMap, 300,161);
				teleport = new Teleport(tileMap);
				teleport.setPosition(3700, 131);
				break;
			case 3:
				tileMap = new TileMap(30, tileType,"/Maps/level3.map", 140, 0 ,1);
				player = new Player(tileMap, 300, 131);
				teleport = new Teleport(tileMap);
				teleport.setPosition(2850, 371);
				break;
			case 4:
				tileMap = new TileMap(30, tileType, "/Maps/level4.map", 140,0,1);
				player = new Player(tileMap, 50, 190);

				// portal
				portal = new Portal(tileMap);
				portal.setPosition(160, 154);
				break;
			default:
				break;
		}


		enemies = new ArrayList<Enemy>();
		eprojectiles = new ArrayList<EnemyProjectile>();
		//populate Enemies jeveils in LevelStates

		energyParticles = new ArrayList<EnergyParticle>();

		//init.player benötigt populierte enemies?

		// explosions
		explosions = new ArrayList<Explosion>();

		//hud
		hud = new HUD(player);

		//title + subtitle levelspezifisch
		if(level !=4){
			try {
				batBatStart = ImageIO.read(
						getClass().getResourceAsStream("/HUD/batbat.gif")
				);

				title = new Title(batBatStart.getSubimage(TITLEPOSITIONS[level-1][0], TITLEPOSITIONS[level-1][1],
						TITLEPOSITIONS[level-1][2],TITLEPOSITIONS[level-1][3]));
				title.sety(60);

				subtitle = new Title(batBatStart.getSubimage(SUBTITLEPOSITIONS[level-1][0], SUBTITLEPOSITIONS[level-1][1],
						SUBTITLEPOSITIONS[level-1][2], SUBTITLEPOSITIONS[level-1][3]));
				subtitle.sety(85);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//Load Specialeffect Sounds
		JukeBox.load("/SFX/teleport.mp3", tp);
		JukeBox.load("/SFX/explode.mp3", "explode");
		JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");
	}

	public void updateEnemyStuff(){
		// update enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if (e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(
						new Explosion(tileMap, e.getx(), e.gety()));
			}
		}

		// update enemy projectiles
		for (int i = 0; i < eprojectiles.size(); i++) {
			EnemyProjectile ep = eprojectiles.get(i);
			ep.update();
			if (ep.shouldRemove()) {
				eprojectiles.remove(i);
				i--;
			}
		}

		// update explosions
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if (explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
	}


	public void update(){
		// check keys
		handleInput();

		// check if end of level event should start
		if (teleport != null && teleport.contains(player)) {
			eventFinish = blockInput = true;
		}
		// check if player dead event should start
		if (player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}

		//play Envents
		//Kommt später hier hi wenn die Events da sind.
		if (eventStart) eventStart();
		if (eventFinish) eventFinish();


		// move title and subtitle
		if (title != null) {
			title.update();
			if (title.shouldRemove()) title = null;
		}
		if (subtitle != null) {
			subtitle.update();
			if (subtitle.shouldRemove()) subtitle = null;
		}

		// update tilemap
		tileMap.setPosition(
				GamePanel.WIDTH / 2.0 - player.getx(),
				GamePanel.HEIGHT / 2.0 - player.gety()
		);
		tileMap.update();
		tileMap.fixBounds();

		updateEnemyStuff();

		// update player
		player.update();

		if(teleport != null){
			// update teleport
			teleport.update();
		}
	};

	public abstract void draw(Graphics2D g);

	public abstract void handleInput();

	///////////////////
	//STANDARD EVENTS//
	///////////////////

	public void eventStart() {
		eventCount++;
		if (eventCount == 1) {
			tb.clear();
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			if (portal != null && !portal.isOpened()) tileMap.setShaking(true, 10);
			JukeBox.stop("level1");
		}
		if (eventCount > 1 && eventCount < 60) {
			tb.get(0).height -= 4;
			tb.get(1).width -= 6;
			tb.get(2).y += 4;
			tb.get(3).x += 6;
		}
		if (eventCount == 30 && title != null) title.begin();
		if (eventCount == 60) {
			eventStart = blockInput = false;
			eventCount = 0;
			if (level == 4)
				eventPortal = blockInput = true;

			if (subtitle != null)
				subtitle.begin();

			tb.clear();
		}
	}

	public void eventFinish(){
		eventCount++;
		if (eventCount == 1) {
			JukeBox.play(tp);
			player.setTeleporting(true);
			player.stop();
		} else if (eventCount == 120) {
			tb.clear();
			tb.add(new Rectangle(
					GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		} else if (eventCount > 120) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
			JukeBox.stop(tp);
		}
		if (eventCount == 180) {
			PlayerSave.setHealth(player.getHealth());
			PlayerSave.setLives(player.getLives());
			PlayerSave.setTime(player.getTime());

			switch(level){
				case 1:
					gsm.setState(GameStateManager.LEVEL3STATE);
					break;

				case 2:
					gsm.setState(GameStateManager.LEVEL4STATE);
					break;

				case 3:
					gsm.setState(GameStateManager.LEVEL2STATE);
					break;

				case 4:
					gsm.setState(GameStateManager.ACIDSTATE);
					break;

				default:
					break;

			}
		}
	}
}
