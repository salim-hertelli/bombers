package bombers.model.supplies;

import bombers.control.GameBoard; 
import bombers.model.Player;
import bombers.view.Tile;

public abstract class Bonus {
	Tile tile;
	int fps = GameBoard.FPS;
	int secondsToLive = 45;
	long ttl = fps * secondsToLive;
	
	public Bonus(Tile tile) {
		this.tile = tile;
	}
	
	public boolean countDown() {
		if (ttl-- == 0) {
			//remove
			return false;
		}
		return true;
	}
	
	public abstract void consume(Player player);
}
