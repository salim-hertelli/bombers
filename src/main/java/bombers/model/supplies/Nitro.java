package bombers.model.supplies;

import bombers.model.Player;
import bombers.view.Tile;

public class Nitro extends Bonus {
	
	private final int maxSpeed = 3;
	private final double boost = 0.5;
	
	public Nitro(Tile tile) {
		super(tile);
	}

	public void consume(Player player) {
		double speed = player.getSpeed();
		speed += boost;
		if (speed <= maxSpeed) {			
			player.setSpeed(speed);
		}
		tile.removeBonus();
	}
}
