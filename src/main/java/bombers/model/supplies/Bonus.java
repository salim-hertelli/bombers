package bombers.model.supplies;

import bombers.control.GameBoard; 
import bombers.model.Player;
import bombers.view.Tile;

public abstract class Bonus {
	final static int bonuses = 2;

	Tile tile;
	int fps = GameBoard.FPS;
	int secondsToLive = 15;
	int ttl = fps * secondsToLive;
	
	public Bonus(Tile tile) {
		this.tile = tile;
	}
	
	public static Bonus generateBonus(Tile tile) {
		Bonus bonus = null;
		switch ((int)(Math.random() * bonuses)) {
		case 0:
			return new ExtraBomb(tile);
		case 1:
			return new LongerBomb(tile);
		}
		return null;
	}
	
	public boolean countDown() {
		if (ttl-- == 0) {
			tile.removeBonus();
			return false;
		}
		return true;
	}
	
	public abstract void consume(Player player);
}
