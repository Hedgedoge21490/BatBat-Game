package al.artofsoul.batbatgame.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.main.GamePanel;

/**
 * @author ArtOfSoul
 *
 */

public class AcidState extends GameState {
	
	private float hue;
	private Color color;
	
	private double angle;
	private BufferedImage image;
	
	public AcidState(GameStateManager gsm) {
		super(gsm);
		try {
			image = ImageIO.read(
			getClass().getResourceAsStream(
			"/Sprites/Player/PlayerSprites.gif"
			)).getSubimage(0, 0, 40, 40);
		}
		catch(Exception e) {}
	}
	
	public void init() {/*Leer weil vermutlich nicht benötigt. Aber muss da sein wegen abstract class undso.*/}
	
	public void update() {
		handleInput();
		color = Color.getHSBColor(hue, 1f, 1f);
		hue += 0.01;
		if(hue > 1) hue = 0;
		angle += 0.1;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		AffineTransform at = new AffineTransform();
		at.translate(GamePanel.WIDTH / 2.0, GamePanel.HEIGHT / 2.0);
		at.rotate(angle);
		g.drawImage(image, at, null);
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) gsm.setState(GameStateManager.MENUSTATE);
	}

}
