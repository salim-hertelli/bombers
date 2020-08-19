package bombers.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import bombers.view.Tile;

public class Player {
	private static double SPEED = 2.5; // numbers of pixels traveled in a single move
	private static final Dimensions dimensions = new Dimensions(25,25);
	private final int movementCorrection = 55; //%

	private String username;
	private GameMap map;
	private Direction previousDirection;
	private Direction direction;
	private List<Bomb> bombs;
	private int bombsLimit;
	private boolean isAlive;
	private Position position;
	private AtomicBoolean wantsToDrop = new AtomicBoolean(false);
	private Tile lockDestination = null;
	private Direction lockDirection = null;
	private Direction lastDirectionRequest = null;
	
	public Player(String username, GameMap map, Position startPosition) {
		this.direction = Direction.REST;
		this.previousDirection = Direction.REST;
		this.position = startPosition;
		this.username = username;
		this.map = map;
		this.bombsLimit = 1;
		this.isAlive = true;
		this.bombs = new LinkedList<>();
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setDirection(Direction direction) {
		if (direction != lockDirection) {
			freeLock();
			previousDirection = this.direction;
			this.direction = direction;			
		}
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
		
		// update (and explode) bombs
		Bomb toRemove = null;
		for (Bomb bomb : bombs) {
			if (bomb.countDown().equals(BombState.EXPLODED)) {
				toRemove = bomb;
			};
		}	
		removeBomb(toRemove);

		// update position of the player according to the direction
		adjustPosition();
		if (isLocked()) {
			followLock();
		}

		double newX = position.getX() + direction.getX() * SPEED;
		double newY = position.getY() + direction.getY() * SPEED;
		Position newPosition = new Position(newX, newY);
		if (isInScreen(newPosition) && noCollision(newPosition)) {
			bonus();
			this.position.update(newX, newY);
		}
		
	}
	
	private void lockDestination(Tile destination, Direction lockDirection) {
		lockDestination = destination;
		this.lockDirection = lockDirection;
	}
	
	private void freeLock() {
		lockDestination = null;
		lockDirection = null;
	}
	
	public boolean isLocked() {
		return lockDestination != null;
	}
	
	private void followLock() {
		// Bro, you know that feeling when you write code totally sure it won't work but it does? xD
		if (direction == Direction.RIGHT || direction == Direction.LEFT) {
			if (lockDestination.getPixelPosition().getY() > position.getY()) {
				this.direction = Direction.DOWN;
			} else if (lockDestination.getPixelPosition().getY() < position.getY()) {
				this.direction = Direction.UP;
			} else {
				freeLock();
			}
		} else {
			if (lockDestination.getPixelPosition().getX() > position.getX()) {
				this.direction = Direction.RIGHT;
			} else if (lockDestination.getPixelPosition().getX() < position.getX()) {
				this.direction = Direction.LEFT;
			} else {
				freeLock();
			}
		}
	}
	
	private void adjustPosition() {
		// Bro, you know that feeling when you write code totally sure it won't work but it does? xD
		if(!isLocked() && !previousDirection.equals(direction) && direction != Direction.REST) {
			freeLock();
			
			Tile currentTile = map.getTileAtPosition(getCenterPosition());
			Tile nextTile = null;
			
			switch(direction) {
				case RIGHT: nextTile = map.getRightNeighbor(currentTile, 1); break;
				case DOWN: nextTile = map.getBotNeighbor(currentTile, 1); break;
				case LEFT: nextTile = map.getLeftNeighbor(currentTile, 1); break;
				case UP: nextTile = map.getTopNeighbor(currentTile, 1); break;
			}
			
			if (nextTile != null && nextTile.isFree()) {
				lockDestination(nextTile, direction);
			}
		}
	}
	

	private boolean bonus() {
		Position topLeft = position;
		Position lowRight = getLowRightCornerPosition(position);
		Position topRight = new Position(lowRight.getX(), topLeft.getY());
		Position lowLeft = new Position(topLeft.getX(), lowRight.getY());
		boolean consumed = false;
		
		if (map.getTileAtPosition(topLeft).getTileType().equals(TileType.BONUS)) {
			map.getTileAtPosition(topLeft).getBonus().consume(this, topLeft);
			consumed = true;
		}
		if (map.getTileAtPosition(lowRight).getTileType().equals(TileType.BONUS)) {
			map.getTileAtPosition(lowRight).getBonus().consume(this, lowRight);
			consumed = true;
		}
		if (map.getTileAtPosition(topRight).getTileType().equals(TileType.BONUS)) {
			map.getTileAtPosition(topRight).getBonus().consume(this, topRight);
			consumed = true;
		}
		if (map.getTileAtPosition(lowLeft).getTileType().equals(TileType.BONUS)) {
			map.getTileAtPosition(lowLeft).getBonus().consume(this, lowLeft);
			consumed = true;
		}
		//For specific bonuses maybe idk
		return consumed;
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
	public int getBombsLimit() {
		return bombsLimit;
	}
	
	public double getSpeed() {
		return SPEED;
	}
	
	public void setSpeed(double speed) {
		SPEED = speed;
	}
	
	public void setBombsLimit(int bombsLimit) {
		this.bombsLimit = bombsLimit;
	}
	
	public void kill() {
		isAlive = false;
	}
}
