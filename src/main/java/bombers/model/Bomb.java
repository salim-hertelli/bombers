package bombers.model;

import bombers.view.Tile;

public class Bomb {
	private int timeToLive = 150;
	private Tile tile;
	
	public Bomb(Tile tile) {
		this.tile = tile;
		tile.setBomb();
	}
	
	/*
	 * returns true if bomb explodes
	 */
	public boolean countDown() {
		if (timeToLive-- == 0) {
			System.out.println("tile is " + tile);
			tile.removeBomb();
			return true;
		}
		return false;
	}
}