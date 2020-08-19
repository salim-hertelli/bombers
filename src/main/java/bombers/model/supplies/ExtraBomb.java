package bombers.model.supplies;

import bombers.model.Player;
import bombers.view.Tile;

public class ExtraBomb extends Bonus {
	
	public ExtraBomb(Tile tile) {
		super(tile);
	}

	public void consume(Player player) {
		player.setBombsLimit(player.getBombsLimit() + 1);
		tile.removeBonus();
	}
}
