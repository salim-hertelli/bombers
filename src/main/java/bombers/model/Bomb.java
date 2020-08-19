package bombers.model;

import bombers.view.Tile;

public abstract class Bomb {
	protected int timeToLive = 150;
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
	public BombState countDown() {
		if (timeToLive-- == 0) {
			tile.removeBomb();
			explode();
			return BombState.EXPLODED;
		}
		return BombState.DROPPED;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	/*
	 * this method remove the bomb even if it hasn't exploded yet
	 */
	public void remove() {
		tile.removeBomb();
	}
	
	abstract void explode();
}