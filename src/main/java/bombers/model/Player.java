package bombers.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import bombers.view.Tile;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Player {
	private static double SPEED = 2.5; // numbers of pixels traveled in a single move
	private static final Dimensions dimensions = new Dimensions(40, 40);
	private final double movementCorrectionRate = 30 / 100.0;
	private Timeline animation;
	
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
	private List<Tile> allowedBombTiles;
	private boolean hasJump;
	private Position blockJump;
	private int bombLengthOfImpact;
	
	public Player(String username, GameMap map, Position startPosition) {
		this.bombLengthOfImpact = 1;
		this.direction = Direction.REST;
		this.previousDirection = Direction.REST;
		this.position = startPosition;
		this.username = username;
		this.map = map;
		this.bombsLimit = 1;
		this.isAlive = true;
		this.bombs = new LinkedList<>();
		this.allowedBombTiles = new LinkedList<>();
		this.hasJump = false;
		this.blockJump = null;
	}
	
	/*
	 * increments the length of impact of the bombs by 1
	 */
	public void incrementLengthOfImpact() {
		bombLengthOfImpact++;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setDirection(Direction direction) {
		if (direction != lockDirection) {
			freeLock();
			previousDirection = this.direction;
			if(blockJump == null)
				this.direction = direction;
			if (direction == Direction.REST) {
				adjustPosition();
			}
		}
	}
	
	private void adjustPosition() {
		Position myCenter = getCenterPosition();
		Tile tile = map.getTileAtPosition(myCenter);
		Position tileCenter = tile.getCenterPosition();
		
		// In the condition we assume that tiles are all squares: height = width
		if (Position.measureDistance(myCenter, tileCenter) < map.getTileHeight() * movementCorrectionRate) {
			this.position.update(tile.getPixelPosition().getX(), tile.getPixelPosition().getY());
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	// for the player to be drawn facing the right direction
	public Direction getDirection() {
		return direction;
	}
	
	public Direction getLockDirection() {
		return lockDirection;
	}
	
	public void move() {
		// If key was pressed player drops a bomb
		if (wantsToDrop.get()) {
			dropBomb();
		}
		
		// update (and explode) bombs
		Bomb toRemove = null;
		for (Bomb bomb : bombs) {
			if (bomb.countDown().equals(BombState.EXPLODED)) {
				toRemove = bomb;
			};
		}	
		removeBomb(toRemove);

		
		if (blockJump == null && !isLocked()) {
			checkDestination();
		}
		
		if (isLocked()) {
			updateLock();
		}
		
		// update position of the player according to the direction
		Direction direction = this.direction;
		double newX = position.getX() + direction.getX() * SPEED;
		double newY = position.getY() + direction.getY() * SPEED;
		Position newPosition = new Position(newX, newY);
		if (isInScreen(newPosition) && noCollision(newPosition) && bombTolerant(newPosition)) {
			bonus();
			this.position.update(newX, newY);
			if(blockJump != null) {
				blockJump = null;
				this.setDirection(Direction.REST);
			}	
		}else if(blockJump != null || (hasJump() && nextTileAvailable(this.getPosition()))) {
			this.position.update(newX, newY);
		}
		Tile tileToRemoveTile = null;
		for (Tile bombedTile : allowedBombTiles) {
			if (bombedTile != map.getTileAtPosition(getLowRightCornerPosition()) && 
					bombedTile != map.getTileAtPosition(getPosition())) {
				tileToRemoveTile = bombedTile;
			};
		}
		if (tileToRemoveTile != null) {
			allowedBombTiles.remove(tileToRemoveTile);
		}
	}

	private boolean nextTileAvailable(Position position) {
		Position nextPosition = null;
		switch (direction) {
		case UP:
			nextPosition = new Position(position.getX(), position.getY() - 2*map.getTileHeight());
			break;
		case DOWN:
			nextPosition = new Position(position.getX(), position.getY() + 2*map.getTileHeight());
			break;
		case RIGHT:
			nextPosition = new Position(position.getX() + 2*map.getTileWidth(), position.getY());
			break;
		case LEFT:
			nextPosition = new Position(position.getX() - 2*map.getTileWidth(), position.getY());
			break;
		}
		if(isInScreen(nextPosition) && noCollision(nextPosition) && bombTolerant(nextPosition)) {
			blockJump = nextPosition;
			return true;
		}
		return false;
	}
	
	private boolean bombFree(Position position) {
		Position lowRight = getLowRightCornerPosition(position);
	
		Tile lowRightTile = map.getTileAtPosition(lowRight);
		Tile positionTile = map.getTileAtPosition(position);
		return !(positionTile.hasBomb() || lowRightTile.hasBomb());
	}
	
	private boolean bombTolerant(Position position) {
		Position lowRight = getLowRightCornerPosition(position);
		
		Tile lowRightTile = map.getTileAtPosition(lowRight);
		Tile positionTile = map.getTileAtPosition(position);
		if (positionTile.hasBomb()) {
			if (!allowedBombTiles.contains(positionTile)) {
				return false;
			}
		}
		if (lowRightTile.hasBomb()) {
			if (!allowedBombTiles.contains(lowRightTile)) {
				return false;
			}
		}
		return true;
	}
	
	private void lockDestination(Tile lockDestination, Direction lockDirection) {
		this.lockDestination = lockDestination;
		this.lockDirection = lockDirection;
	}
	
	private void freeLock() {
		lockDestination = null;
		lockDirection = null;
	}
	
	public boolean isLocked() {
		return lockDestination != null;
	}
	
	/*
	 * this method is used when a destination is already locked
	 * It checks whether destination is reached, if so lock is freed and direction is set back to lockDirection
	 */
	private void updateLock() { 
		Direction direction = lockDirection;
		if (direction == Direction.RIGHT || direction == Direction.LEFT) {
			if (Math.abs(lockDestination.getPixelPosition().getY() - position.getY()) < SPEED) {
				position.update(position.getX(), lockDestination.getPixelPosition().getY());
				this.direction = lockDirection;
				freeLock();
			}
		} else {
			if (Math.abs(lockDestination.getPixelPosition().getX() - position.getX()) < SPEED) {
				position.update(lockDestination.getPixelPosition().getX(), position.getY());
				this.direction = lockDirection;
				freeLock();
			}
		}
	}
	
	/*
	 * this method is used when no destination is locked
	 * It's purpose is to look for the destination and lock it when needed
	 */
	private void checkDestination() {
		Direction direction = this.direction;
		if (!previousDirection.equals(direction) && direction != Direction.REST) {
			Tile currentTile = map.getTileAtPosition(getCenterPosition());
			Tile nextTile = null;
			
			switch(direction) {
				case RIGHT: nextTile = map.getRightNeighbor(currentTile, 1); break;
				case DOWN: nextTile = map.getBotNeighbor(currentTile, 1); break;
				case LEFT: nextTile = map.getLeftNeighbor(currentTile, 1); break;
				case UP: nextTile = map.getTopNeighbor(currentTile, 1); break;
			}
			
			if (nextTile != null && nextTile.isFree()) {
				if (direction == Direction.RIGHT || direction == Direction.LEFT) {
					// last step of the adjustment
					if (Math.abs(nextTile.getPixelPosition().getY() - position.getY()) < SPEED) {
						position.update(position.getX(), nextTile.getPixelPosition().getY());
						return;
					}
					
					if (nextTile.getPixelPosition().getY() > position.getY()) {
						this.direction = Direction.DOWN;
					} else if (nextTile.getPixelPosition().getY() < position.getY()) {
						this.direction = Direction.UP;
					}
				} else {
					if (Math.abs(nextTile.getPixelPosition().getX() - position.getX()) < SPEED) {
						position.update(nextTile.getPixelPosition().getX(), position.getY());
						return;
					}
					
					if (nextTile.getPixelPosition().getX() > position.getX()) {
						this.direction = Direction.RIGHT;
					} else if (nextTile.getPixelPosition().getX() < position.getX()) {
						this.direction = Direction.LEFT;
					}
				}

				lockDestination(nextTile, direction);
			}
		}
	}
	
	/*
	 * if there is a reachable bonus, the player consumes it
	 */
	private boolean bonus() {
		Position topLeft = position;
		Position lowRight = getLowRightCornerPosition(position);
		boolean consumed = false;
		
		if (map.getTileAtPosition(topLeft).hasBonus()) {
			map.getTileAtPosition(topLeft).getBonus().consume(this);
			consumed = true;
		}
		if (map.getTileAtPosition(lowRight).hasBonus()) {
			map.getTileAtPosition(lowRight).getBonus().consume(this);
			consumed = true;
		}

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
	
	public void setDoesntWantToDrop() {
		wantsToDrop.set(false);
	}

	public void addAllowedBombedTile(Tile tile) {
		if (!allowedBombTiles.contains(tile)) {
			allowedBombTiles.add(tile);
		}
	}
	
	private void dropBomb() {
		if (bombs.size() < bombsLimit) {
			Tile dropTile = map.getTileAtPosition(getCenterPosition());
			if (!dropTile.hasBomb()) {
				Bomb newBomb = new ProgressiveBomb(dropTile, map, bombLengthOfImpact);
				addBomb(newBomb);
				allowedBombTiles.add(dropTile);
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
	
	public boolean hasJump() {
		return hasJump;
	}
	
	public void setJump(boolean jump) {
		hasJump = jump;
	}
	
	public Timeline getAnimation() {
		return animation;
	}
	
	public void setAnimation(Timeline animation) {
		this.animation = animation;
	}
	
	public void kill() {
		for (Bomb bomb : bombs) {
			bomb.remove();
		}
		if(hasJump())
			setJump(false);
		else
			isAlive = false;
	}
	
	
}
