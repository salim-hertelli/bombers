package bombers.model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
		//TODO 
		// move has to update the ttl of the bombs
		if (wantsToDrop.get()) {
			dropBomb();
			wantsToDrop.set(false);
		}
		System.out.print("I was " + position + ", i have direction " + direction + ", new Position ");
		this.position.update(position.getX() + direction.getX() * SPEED, position.getY() + direction.getY() * SPEED);
		System.out.println(position);
		for (Bomb bomb : bombs) {
			bomb.countDown();
		}
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
			new Bomb(this, map.getTileAtPosition(getCenterPosition()));
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
