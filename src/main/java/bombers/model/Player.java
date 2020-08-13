package bombers.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import bombers.view.Tile;

public class Player {
	private static final int SPEED = 5; // numbers of pixels traveled in a single move
	private static Dimensions dimensions = new Dimensions(25,25); //TODO set dimensions of the player
	
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
		System.out.println("Direction changed, new direction: " + direction);
		this.direction = direction;
	}
	
	public String getUsername() {
		return username;
	}
	
	// for the gameboard to print the player in the correct direction
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
		this.position.update(position.getX() + direction.getX() * SPEED, position.getY() + direction.getY() * SPEED);
		
		// update (and explode) bombs
		Bomb toRemove = null;
		for (Bomb bomb : bombs) {
			if (bomb.countDown()) {
				toRemove = bomb;
			};
		}
		removeBomb(toRemove);
	}
	
	private Position getCenterPosition() {
		return new Position(position.getX() + dimensions.getWidth() / 2,
				position.getY() + dimensions.getHeight() / 2);
	}
	
	public void setWantsToDrop() {
		wantsToDrop.set(true);
	}
	
	private void dropBomb() {
		if (bombs.size() < bombsLimit) {
			Tile dropTile = map.getTileAtPosition(getCenterPosition());
			if (!dropTile.hasBomb()) {
				Bomb newBomb = new Bomb(dropTile);
				addBomb(newBomb);				
			}
		}
	}
	
	// bomb detonated
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
