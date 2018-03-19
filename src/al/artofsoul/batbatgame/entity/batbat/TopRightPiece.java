package al.artofsoul.batbatgame.entity.batbat;

import java.awt.image.BufferedImage;

import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class TopRightPiece extends MapPiece {
	
	private BufferedImage[] sprites;
	
	public TopRightPiece(TileMap tm) {
		super(tm, "TopRightPiece");
	}
}
