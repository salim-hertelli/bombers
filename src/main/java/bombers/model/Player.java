package bombers.model;

import java.util.LinkedList;
import java.util.List;

public class Player {
	private static final int SPEED = 2; // numbers of pixels traveled in a single move
	
	private String username;
	private GameMap map;
	private Direction direction;
	private List<Bomb> bombs;
	private int bombsLimit;
	private boolean isAlive;
	
	public Player(String username, GameMap map) {
		this.direction = Direction.REST;
		this.username = username;
		this.map = map;
		this.bombsLimit = 1;
		this.isAlive = true;
		this.bombs = new LinkedList<>();
	}
	
	public void setDirection(Direction direction) {
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
	}
	
	public void dropBomb() {
		if (bombs.size() < bombsLimit) {
			// bomb must inform the player when it detonated so that the player decrements the bombsCount
			Bomb newBomb = new Bomb(this);
			
			bombs.add(newBomb);
		}
	}
	
	// bomb detonated
	public void removeBomb(Bomb bomb) {
		bombs.remove(bomb);
	}
	
	// for the gameboard to display bombs
	public List<Bomb> getBombs() {
		return bombs;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public void kill() {
		isAlive = false;
	}
}
