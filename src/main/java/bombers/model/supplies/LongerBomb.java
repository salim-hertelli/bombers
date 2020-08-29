package bombers.model.supplies;

import bombers.model.Player;
import bombers.view.Tile;

public class LongerBomb extends Bonus{
	
	public LongerBomb(Tile tile) {
		super(tile);
	}

	public void consume(Player player) {
		player.incrementLengthOfImpact();
		tile.removeBonus();
	}
}
