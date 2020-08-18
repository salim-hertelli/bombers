package bombers.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import bombers.view.Tile;

public class Player {
	private static double SPEED = 2.5; // numbers of pixels traveled in a single move
	private static final Dimensions dimensions = new Dimensions(25,25);
	private final int movementCorrection = 10; //%

	
	private String username;
	private GameMap map;
	private Direction previousDirection;
	private Direction direction;
	private List<Bomb> bombs;
	private int bombsLimit;
	private boolean isAlive;
	private Position position;
	private AtomicBoolean wantsToDrop = new AtomicBoolean(false);
	
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
		previousDirection = this.direction;
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
		adjustPosition();
		double newX = position.getX() + direction.getX() * SPEED;
		double newY = position.getY() + direction.getY() * SPEED;
		Position newPosition = new Position(newX, newY);
		if (isInScreen(newPosition) && noCollision(newPosition)) {
			bonus();
			this.position.update(newX, newY);
		}
		
		// update (and explode) bombs
		Bomb toRemove = null;
		for (Bomb bomb : bombs) {
			if (bomb.countDown().equals(BombState.EXPLODED)) {
				toRemove = bomb;
			};
		}	
		removeBomb(toRemove);
	}
	
	private void adjustPosition() {
		if(!previousDirection.equals(direction)) {
			Tile currentTile = map.getTileAtPosition(position);
			Tile bRTile = map.getTileAtPosition(getLowRightCornerPosition(position));
			if(direction.equals(Direction.RIGHT) || direction.equals(Direction.LEFT)){
				System.out.println(currentTile.getGridPosition().getY() + " " + bRTile.getGridPosition().getY() + " " + position.getY());

				if(Math.abs(bRTile.getPixelPosition().getY() - position.getY()) <= map.getTileDimensions().getHeight() / movementCorrection) {
					System.out.print("Changed from " + position.getY());
					this.position.update(position.getX(), bRTile.getPixelPosition().getY());
					System.out.println(" to" + bRTile.getPixelPosition().getY());
}
				if(Math.abs(currentTile.getPixelPosition().getY() - position.getY()) <= map.getTileDimensions().getHeight() / movementCorrection) { 
					System.out.print("Changed from " + position.getY());
					this.position.update(position.getX(), currentTile.getPixelPosition().getY());
					System.out.println( " to" + currentTile.getPixelPosition().getY());
}
			}else if(direction.equals(Direction.UP) || direction.equals(Direction.DOWN)) {
				System.out.println(currentTile.getPixelPosition().getX() + " " + bRTile.getPixelPosition().getX() + " " + position.getX());

				if(Math.abs(bRTile.getPixelPosition().getX() - position.getX()) <= map.getTileDimensions().getHeight() / movementCorrection) 
					this.position.update(bRTile.getPixelPosition().getX(), position.getY());
				if(Math.abs(currentTile.getPixelPosition().getX() - position.getX()) <= map.getTileDimensions().getHeight() / movementCorrection) 
					this.position.update(currentTile.getPixelPosition().getX(), position.getY());

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
