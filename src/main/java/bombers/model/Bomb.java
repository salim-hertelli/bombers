package bombers.model;

import bombers.view.Tile;

public abstract class Bomb {
	private int timeToLive = 150;
	Tile tile;
	GameMap map;
	
	public Bomb(Tile tile, GameMap map) {
		this.tile = tile;
		this.map = map;
		tile.setBomb();
	}
	
	/*
	 * returns true if bomb exploded
	 */
	public boolean countDown() {
		if (timeToLive-- == 0) {
			tile.removeBomb();
			explode();
			return true;
		}
		return false;
	}
	
	abstract void explode();
}