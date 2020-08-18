package bombers.model.supplies;

import bombers.model.GameMap;
import bombers.model.Player;
import bombers.model.Position;
import bombers.view.Tile;

public class ExtraBomb extends Bonus {
	
	public ExtraBomb(Tile tile) {
		super(tile);
	}

	public void consume(Player player, Position position) {
		player.setBombsLimit(player.getBombsLimit()+1);
		remove(position);
	}
}
