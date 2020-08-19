package bombers.view;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import bombers.model.Dimensions;
import bombers.model.Position;
import bombers.model.TileType;
import bombers.model.supplies.Bonus;
import bombers.model.supplies.ExtraBomb;
import bombers.model.supplies.Nitro;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile {
	private final static String BOMB_FILE_PATH = "src/main/java/bombers/view/bomb.png";
	private final static String FREE_TILE_FILE_PATH = "src/main/java/bombers/view/tile.png";
	private final static String WALL_TILE_FILE_PATH = "src/main/java/bombers/view/wall.png";
	private final static String OBSTACLE_TILE_FILE_PATH = "src/main/java/bombers/view/wall.png";
	
	private static Image freeImage;
	private static Image wallImage;
	private static Image bombImage;
	private static Image obstacleImage;

	static {
		try {
			freeImage = new Image(new FileInputStream(FREE_TILE_FILE_PATH));
			wallImage = new Image(new FileInputStream(WALL_TILE_FILE_PATH));
			bombImage = new Image(new FileInputStream(BOMB_FILE_PATH));
			obstacleImage = new Image(new FileInputStream(OBSTACLE_TILE_FILE_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<Class<? extends Bonus>> supplies = Arrays.asList(bombers.model.supplies.ExtraBomb.class, bombers.model.supplies.Nitro.class);
	private GraphicsContext gc;
	private Dimensions dimensions;
	private Position pixelPosition; // this is the usual position
	private Position gridPosition; // this is the coordinates of this tile in the map
	private TileType tileType;
	private boolean hasBomb;
	private boolean hasBonus;
	private Bonus bonus;
	
	public Tile(Position pixelposition, Position gridPosition, Dimensions dimensions, TileType tileType) {
    	this.tileType = tileType;
    	this.dimensions = dimensions;
    	this.gridPosition = gridPosition;
    	this.pixelPosition = pixelposition;
	}
	
	public void setTileType(TileType tileType) {
		this.tileType = tileType;
		if(tileType.equals(TileType.BONUS)) {
			hasBonus = true;
			try {
			    bonus = new Nitro(this);
			    //I used this to pick a bonus randomly, but since I added an argument to the constructor it doesnt work anymore.
			    //supplies.get(new Random().nextInt(supplies.size())).newInstance();    
			}catch(Exception e){
				System.err.println("Can't add bonus in " + gridPosition);
			}
		}	
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
	
	public Position getPixelPosition() {
		return pixelPosition;
	}
	
	public boolean isFree() {
		return tileType == TileType.FREE || tileType == TileType.BONUS;
	}
	
	public void removeBomb() {
		hasBomb = false;
	}
	
	public Bonus getBonus() {
		return bonus;
	}

	public void paint() {
		Image image = null;
		if (tileType == TileType.WALL) {
			image = wallImage;
		} else if (tileType == TileType.FREE || tileType == TileType.BONUS) {
			image = freeImage;
		} else if (tileType == TileType.OBSTACLE) {
			image = obstacleImage;
		}
						
		gc.drawImage(image, pixelPosition.getX(), pixelPosition.getY());
		if (hasBomb) {
			gc.drawImage(bombImage, pixelPosition.getX(), pixelPosition.getY());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tile)) {
			return false;
		}
		Tile other = (Tile) obj;
		return gridPosition.equals(other.getGridPosition());
	}
	
}
