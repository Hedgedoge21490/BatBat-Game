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

public class MapPiece extends MapObject {

    private BufferedImage[] sprites;

    public MapPiece(TileMap tm, String type) {

        super(tm);
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/Other/ballBatBoss.gif")
            );
            sprites = new BufferedImage[1];
            width = height = 4;
            switch (type) {
                case "BottomLeftPiece":
                    sprites[0] = spritesheet.getSubimage(0, 10, 10, 10);
                    break;

                case "BottomRightPiece":
                    sprites[0] = spritesheet.getSubimage(10, 10, 10, 10);
                    break;

                case "TopLeftPiece":
                    sprites[0] = spritesheet.getSubimage(0, 0, 10, 10);
                    break;

                case "TopRightPiece":
                    sprites[0] = spritesheet.getSubimage(10, 0, 10, 10);
                    break;

                default:
                    sprites[0] = spritesheet.getSubimage(0, 10, 10, 10);
                    break;
            }
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
}
