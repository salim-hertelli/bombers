package bombers.view;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import bombers.model.Bomb;
import bombers.model.Dimensions;
import bombers.model.Player;
import bombers.model.Position;
import bombers.model.TileType;
import bombers.model.supplies.Bonus;
import bombers.model.supplies.ExtraBomb;
import bombers.model.supplies.Nitro;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Tile {
	private final static String BOMB_FILE_PATH = "src/main/java/bombers/view/bomb2.png";
	private final static String BONUS_FILE_PATH = "src/main/java/bombers/view/bonus.png";
	private final static String FREE_TILE_FILE_PATH = "src/main/java/bombers/view/tile2.png";
	private final static String WALL_TILE_FILE_PATH = "src/main/java/bombers/view/wall.png";
	private final static String OBSTACLE_TILE_FILE_PATH = "src/main/java/bombers/view/obstacle.png";
	
	private static Image freeImage;
	private static Image wallImage;
	private static Image bombImage;
	private static Image obstacleImage;
	private static Image bonusImage;
	
	static {
		try {
			freeImage = new Image(new FileInputStream(FREE_TILE_FILE_PATH));
			wallImage = new Image(new FileInputStream(WALL_TILE_FILE_PATH));
			bombImage = new Image(new FileInputStream(BOMB_FILE_PATH));
			bonusImage = new Image(new FileInputStream(BONUS_FILE_PATH));
			obstacleImage = new Image(new FileInputStream(OBSTACLE_TILE_FILE_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<Class<? extends Bonus>> supplies = Arrays.asList(bombers.model.supplies.ExtraBomb.class, 
			bombers.model.supplies.Nitro.class);
	private GraphicsContext gc;
	private Dimensions dimensions;
	private Position pixelPosition; // this is the usual position
	private Position gridPosition; // this is the coordinates of this tile in the map
	private TileType tileType;
	private boolean hasBomb;
	private Bomb bomb;
	private Bonus bonus;
	private int probability = 1; // probability of generating a bonus after destruction
	private boolean isExploding;
	
	public Tile(Position pixelposition, Position gridPosition, Dimensions dimensions, TileType tileType) {
    	this.tileType = tileType;
    	this.dimensions = dimensions;
    	this.gridPosition = gridPosition;
    	this.pixelPosition = pixelposition;
    	this.bomb = null;
    	this.isExploding = false;
	}
	
	/*
	 * use destroyTile() instead of setTileType() when destroying a tile
	 */
	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}
	
	public boolean hasBonus() {
		return bonus != null;
	}
	
	/*
	 * the tile is set to free and a bonus may spawn
	 */
	public void destroyTile() {
		TileType oldType = tileType;
		setTileType(TileType.FREE);
		if((int)(Math.random() * probability) == 0) 
			setBonus(new ExtraBomb(this));
	}
	
	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}
	
	
	public void setBomb(Bomb bomb) {
		this.bomb = bomb;
		hasBomb = true;
	}
	
	public Bomb getBomb() {
		return bomb;
	}
	
	public void removeBonus() {
		this.bonus = null;
	}
	
	public void setGraphicsContext(GraphicsContext gc) {
		this.gc = gc;
	}
	 
	public TileType getTileType() {
		return tileType;
	}
	
	/*
	 * returns the pixelPosition of the center of this tile
	 */
	public Position getCenterPosition() {
		return new Position(pixelPosition.getX() + (dimensions.getWidth() -1) / 2,
				pixelPosition.getY() + (dimensions.getHeight() - 1) / 2);
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
		return tileType == TileType.FREE;
	}
	
	public void removeBomb() {
		this.bomb = null;
	}
	
	//If a second bomb explodes the tile where there's an exploding bomb, the first bomb shouldn't explode a second time.
	public void initiateExplosion() {
		this.hasBomb = false;
	}
	
	public void setExploding(boolean exploding) {
		this.isExploding = exploding;
	}
	
	public Bonus getBonus() {
		return bonus;
	}

	public void paint() {
		Image image = null;
		if (tileType == TileType.WALL) {
			image = wallImage;
		} else if (tileType == TileType.FREE) {
			if(!isExploding)
				image = freeImage;
			else {
				image = obstacleImage; //TOCHANGE explosion
				setExploding(false);
			}
		} else if (tileType == TileType.OBSTACLE) {
			image = obstacleImage;
		}
						
		gc.drawImage(image, pixelPosition.getX(), pixelPosition.getY());
		if (hasBomb) {
			gc.drawImage(bombImage, pixelPosition.getX(), pixelPosition.getY());
		}
		if (hasBonus()) {
			gc.drawImage(bonusImage, pixelPosition.getX(), pixelPosition.getY());
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
