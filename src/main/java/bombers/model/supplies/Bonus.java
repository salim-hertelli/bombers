package bombers.model.supplies;

import bombers.model.Player;
import bombers.model.Position;
import bombers.model.TileType;
import bombers.view.Tile;

public abstract class Bonus {
	Tile tile;
	int fps = 60;
	int secondsToLive = 45;
	long ttl = fps*secondsToLive;
	
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
	
	public abstract void consume(Player player, Position position);
	
	public void remove(Position position) {
		tile.setTileType(TileType.FREE);
	}
	
}
