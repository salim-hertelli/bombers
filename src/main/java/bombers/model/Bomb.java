package bombers.model;

import bombers.view.Tile;

public class Bomb {
	private int timeToLive = 150;
	private Player player;
	private Tile tile;
	
	public Bomb(Player player, Tile tile) {
		if (tile.hasBomb()) {
			return;
		}
		this.player = player;
		this.tile = tile;
		tile.setBomb();
		player.addBomb(this);
	}
	
	public void countDown() {
		if (timeToLive-- == 0) {
			detonate();
		}
	}
	
	private void detonate() {
		tile.removeBomb();
		player.removeBomb(this);
		//TODO destroy
	}
}