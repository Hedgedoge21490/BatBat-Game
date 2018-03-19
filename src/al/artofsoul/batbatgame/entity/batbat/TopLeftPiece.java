package al.artofsoul.batbatgame.entity.batbat;

import java.awt.image.BufferedImage;

import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class TopLeftPiece extends MapPiece{
	
	private BufferedImage[] sprites;
	
	public TopLeftPiece(TileMap tm) {
		super(tm, "TopLeftPiece");
	}
}
