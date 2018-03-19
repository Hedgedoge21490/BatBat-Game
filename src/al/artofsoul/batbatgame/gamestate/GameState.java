package al.artofsoul.batbatgame.gamestate;

import java.awt.*;
import java.util.ArrayList;


/**
 * @author ArtOfSoul
 *
 */

public abstract class GameState {
	
	protected GameStateManager gsm;

	// events
	private boolean blockInput = false;
	private int eventCount = 0;
	private boolean eventStart;
	private ArrayList<Rectangle> tb;

	
	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void handleInput();


}
