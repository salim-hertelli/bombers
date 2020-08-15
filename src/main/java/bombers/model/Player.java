package bombers.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import bombers.view.Tile;

public class Player {
	private static final int SPEED = 5; // numbers of pixels traveled in a single move
	private static final Dimensions dimensions = new Dimensions(25,25);
	
	private String username;
	private GameMap map;
	private Direction direction;
	private List<Bomb> bombs;
	private int bombsLimit;
	private boolean isAlive;
	private Position position;
	private AtomicBoolean wantsToDrop = new AtomicBoolean(false);
	
	public Player(String username, GameMap map, Position startPosition) {
		this.direction = Direction.REST;
		this.position = startPosition;
		this.username = username;
		this.map = map;
		this.bombsLimit = 20;
		this.isAlive = true;
		this.bombs = new LinkedList<>();
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public String getUsername() {
		return username;
	}
	
	// for the player to be drawn facing the right direction
	public Direction getDirection() {
		return direction;
	}
	
	public void move() {
		// If key was pressed player drops a bomb
		if (wantsToDrop.get()) {
			dropBomb();
			wantsToDrop.set(false);
		}
		
		// update position of the player according to the direction
		int newX = position.getX() + direction.getX() * SPEED;
		int newY = position.getY() + direction.getY() * SPEED;
		Position newPosition = new Position(newX, newY);
		if (isInScreen(newPosition) && noCollision(newPosition)) {			
			this.position.update(newX, newY);
		}
		
		// update (and explode) bombs
		Bomb toRemove = null;
		for (Bomb bomb : bombs) {
			if (bomb.countDown()) {
				toRemove = bomb;
			};
		}
		removeBomb(toRemove);
	}
	
	/*
	 * returns true if the position doesn't cause a collision with a non FREE tile
	 */
	private boolean noCollision(Position position) {
		Position topLeft = position;
		Position lowRight = getLowRightCornerPosition(position);
		Position topRight = new Position(lowRight.getX(), topLeft.getY());
		Position lowLeft = new Position(topLeft.getX(), lowRight.getY());
		
		if (!map.getTileAtPosition(topLeft).isFree()) {
			return false;
		}
		
		if (!map.getTileAtPosition(lowRight).isFree()) {
			return false;
		}
		
		if (!map.getTileAtPosition(topRight).isFree()) {
			return false;
		}
		
		if (!map.getTileAtPosition(lowLeft).isFree()) {
			return false;
		}
		
		return true;
	}
	
	/*
	 * returns true if the position doesn't cause the player to leave the screen
	 */
	private boolean isInScreen(Position position) {
		return position.getX() >= 0 && getLowRightCornerPosition(position).getX() < map.getDimensions().getWidth() &&
				position.getY() >= 0 && getLowRightCornerPosition(position).getY() < map.getDimensions().getHeight();
	}
	
	private Position getCenterPosition() {
		return new Position(position.getX() + (dimensions.getWidth() -1) / 2,
				position.getY() + (dimensions.getHeight() - 1) / 2);
	}
	
	public Position getLowRightCornerPosition() {
		return getLowRightCornerPosition(this.position);
	}
	
	public Position getLowRightCornerPosition(Position position) {
		return new Position(position.getX() + dimensions.getWidth() - 1,
				position.getY() + dimensions.getHeight() - 1);
	}
	
	public void setWantsToDrop() {
		wantsToDrop.set(true);
	}
	
	private void dropBomb() {
		if (bombs.size() < bombsLimit) {
			Tile dropTile = map.getTileAtPosition(getCenterPosition());
			if (!dropTile.hasBomb()) {
				Bomb newBomb = new ProgressiveBomb(dropTile, map);
				addBomb(newBomb);				
			}
		}
	}
	
	public void removeBomb(Bomb bomb) {
		bombs.remove(bomb);
	}
	
	public void addBomb(Bomb bomb) {
		bombs.add(bomb);
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void kill() {
		isAlive = false;
	}
}
