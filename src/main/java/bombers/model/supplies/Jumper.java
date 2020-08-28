package bombers.model.supplies;

import bombers.model.Player;
import bombers.view.Tile;

//If player has Jumper, a bomb would remove it instead of killing the player.
public class Jumper extends Bonus{
	
	public Jumper(Tile tile) {
		super(tile);
	}
	
	public void consume(Player player) {
		player.setJump(true);
		tile.removeBonus();
	}
	
}
