/**
 * 
 */
package bombers.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import bombers.model.Dimensions;
import bombers.model.Position;
import bombers.model.TileType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * @author DELL
 *
 */
public class Tile {
	private final static String BOMB_FILE_PATH = "src\\main\\java\\bombers\\view\\bomb.png";
	
	private GraphicsContext gc;
	private Dimensions dimensions;
	private Position position;
	private TileType tileType;
	private boolean hasBomb;
	
	public Tile(Position position, Dimensions dimensions, TileType tileType) {
    	this.tileType = tileType;
    	this.dimensions = dimensions;
    	this.position = position;
	}
	
	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}
	
	public void setBomb() {
		hasBomb = true;
	}
	
	public void setGraphicsContext(GraphicsContext gc) {
		this.gc = gc;
	}
	 
	public TileType getTileType() {
		return tileType;
	}
	
	public void removeBomb() {
		hasBomb = false;
	}

	public void paint() {
		try {
			System.out.println("I painted " + position.getX() + " " + position.getY());
			Image image = new Image(new FileInputStream(tileType.getImagePath()));
							
			gc.drawImage(image, position.getX(), position.getY());
			
			if (hasBomb) {
				gc.drawImage(new Image(new FileInputStream(BOMB_FILE_PATH)), position.getX(), position.getY());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
