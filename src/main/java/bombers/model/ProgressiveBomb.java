package bombers.model;

import java.util.ArrayList; 
import java.util.List;

import bombers.model.supplies.Bonus;
import bombers.model.supplies.ExtraBomb;
import bombers.view.Tile;

public class ProgressiveBomb extends Bomb{
	private int lengthOfImpact = 20;
	private int step = 0;
	private int wahrscheinlichkeit = 1;
	private List<Integer> indexes = new ArrayList<>(); 
	
	public ProgressiveBomb(Tile tile, GameMap map) {
		super(tile, map);
		for (int i=0; i < 4;)
	        indexes.add(i++);
	}
	
	@Override
	public BombState countDown() {
		if (timeToLive-- == -lengthOfImpact){
			explode();
			return BombState.EXPLODED;
		} else if(timeToLive < 0){
			explode();
			return BombState.EXPLODING;
		} else if(timeToLive == 0) {
			tile.removeBomb();
			return BombState.EXPLODING;
		}
		return BombState.DROPPED;
	}

	@Override
	public void explode() {
		Tile[] toDestroy = {map.getLeftNeighbor(tile, step), map.getBotNeighbor(tile, step), 
				map.getRightNeighbor(tile, step), map.getTopNeighbor(tile, step)};
		for (int j : indexes) {
			if (j >= 0 && toDestroy[j] != null) {
				Tile t = toDestroy[j];
				for (Player player : map.getPlayersAtTile(t)) 
					player.kill();
				if (t.getTileType().blocksBombPropagation())
					indexes.set(indexes.indexOf(j), -1);
				if (t.getTileType().isDestructible()) {
					t.destroyTile();
				}
			} else {
				indexes.set(indexes.indexOf(j), -1);
			}
		}
		step++;
	}
}
