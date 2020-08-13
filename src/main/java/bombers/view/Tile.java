package bombers.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import bombers.model.Dimensions;
import bombers.model.Position;
import bombers.model.TileType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile {
	private final static String BOMB_FILE_PATH = "src\\main\\java\\bombers\\view\\bomb.png";
	private final static String FREE_TILE_FILE_PATH = "src\\main\\java\\bombers\\view\\tile.png";
	private final static String WALL_TILE_FILE_PATH = "src\\main\\java\\bombers\\view\\wall.png";
	
	private static Image freeImage;
	private static Image wallImage;
	private static Image bombImage;
	
	static {
		try {
			freeImage = new Image(new FileInputStream(FREE_TILE_FILE_PATH));
			wallImage = new Image(new FileInputStream(WALL_TILE_FILE_PATH));
			bombImage = new Image(new FileInputStream(BOMB_FILE_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
	
	public boolean hasBomb() {
		return hasBomb;
	}
	
	public void removeBomb() {
		hasBomb = false;
	}

	public void paint() {
<<<<<<< HEAD
		Image image = null;
		if (tileType == TileType.WALL) {
			image = wallImage;
		} else if (tileType == TileType.FREE) {
			image = freeImage;
		}
						
		gc.drawImage(image, position.getX(), position.getY());
		
		if (hasBomb) {
			gc.drawImage(bombImage, position.getX(), position.getY());
=======
		try {
			Image image = new Image(new FileInputStream(tileType.getImagePath()));
							
			gc.drawImage(image, position.getX(), position.getY());
			
			if (hasBomb) {
				gc.drawImage(new Image(new FileInputStream(BOMB_FILE_PATH)), position.getX(), position.getY());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
>>>>>>> 3172e271c5b44fcb4cff0e028fb6d4c01445c1ed
		}
	}
}
