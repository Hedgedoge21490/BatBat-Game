package al.artofsoul.batbatgame.entity.batbat;

import java.awt.image.BufferedImage;

import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class BottomRightPiece extends MapPiece {
	
	private BufferedImage[] sprites;
	
	public BottomRightPiece(TileMap tm) {
		super(tm, "BottomRightPiece");
	}
}
