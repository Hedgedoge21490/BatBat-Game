package al.artofsoul.batbatgame.entity.batbat;

import java.awt.image.BufferedImage;

import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class BottomLeftPiece extends MapPiece {
	
	private BufferedImage[] sprites;
	
	public BottomLeftPiece(TileMap tm) {
		super(tm, "BottomLEftPiece");
	}
}
