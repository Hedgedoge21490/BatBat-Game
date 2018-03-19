package al.artofsoul.batbatgame.entity.batbat;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import al.artofsoul.batbatgame.entity.MapObject;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class BottomRightPiece extends MapObject {
	
private BufferedImage[] sprites;
	
	public BottomRightPiece(TileMap tm) {
		super(tm);
		try {	
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/Other/ballBatBoss.gif")
			);
			sprites = new BufferedImage[1];
			width = height = 4;
			sprites[0] = spritesheet.getSubimage(10, 10, 10, 10);
			animation.setFrames(sprites);
			animation.setDelay(-1);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		x += dx;
		y += dy;
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}

}
