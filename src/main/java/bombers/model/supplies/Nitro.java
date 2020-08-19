package bombers.model.supplies;

import bombers.model.Player;
import bombers.model.Position;
import bombers.view.Tile;

public class Nitro extends Bonus {
	
	public Nitro(Tile tile) {
		super(tile);
	}

	private final int maxSpeed = 3;
	private final double boost = 0.5;

	public void consume(Player player, Position position) {
		double speed = player.getSpeed();
		speed += boost;
		if(speed <= maxSpeed)
			player.setSpeed(speed);
		remove(position);
	}
}
