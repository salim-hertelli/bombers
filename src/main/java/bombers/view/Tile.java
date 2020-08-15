package bombers.view;

import java.io.FileInputStream;

import bombers.model.Dimensions;
import bombers.model.Position;
import bombers.model.TileType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile {
	private final static String BOMB_FILE_PATH = "src/main/java/bombers/view/bomb.png";
	private final static String FREE_TILE_FILE_PATH = "src/main/java/bombers/view/tile.png";
	private final static String WALL_TILE_FILE_PATH = "src/main/java/bombers/view/wall.png";
	
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
	private Position pixelPosition; // this is the usual position
	private Position gridPosition; // this is the coordinates of this tile in the map
	private TileType tileType;
	private boolean hasBomb;
	
	public Tile(Position pixelposition, Position gridPosition, Dimensions dimensions, TileType tileType) {
    	this.tileType = tileType;
    	this.dimensions = dimensions;
    	this.gridPosition = gridPosition;
    	this.pixelPosition = pixelposition;
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
	
	public Position getGridPosition() {
		return gridPosition;
	}
	
	public boolean isFree() {
		return tileType == TileType.FREE;
	}
	
	public void removeBomb() {
		hasBomb = false;
	}

	public void paint() {
		Image image = null;
		if (tileType == TileType.WALL) {
			image = wallImage;
		} else if (tileType == TileType.FREE) {
			image = freeImage;
		}
						
		gc.drawImage(image, pixelPosition.getX(), pixelPosition.getY());
		if (hasBomb) {
			gc.drawImage(bombImage, pixelPosition.getX(), pixelPosition.getY());
		}
	}
}
