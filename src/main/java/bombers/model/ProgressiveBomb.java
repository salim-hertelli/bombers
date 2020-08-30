package bombers.model;

import java.util.ArrayList;
import java.util.List;

import bombers.view.Tile;

public class ProgressiveBomb extends Bomb{
	private int lengthOfImpact;
	private int step = 0;
	private List<Integer> indexes = new ArrayList<>(); 
	
	public ProgressiveBomb(Tile tile, GameMap map, int lenghtOfImpact) {
		super(tile, map);
		this.lengthOfImpact = lenghtOfImpact;
		for (int i=0; i < 4;)
	        indexes.add(i++);
	}
	
	@Override
	public BombState countDown() {
		if (step == lengthOfImpact){
			explode();
			tile.removeBomb();
			return BombState.EXPLODED;
		} else if(--timeToLive < 0){
			explode();
			return BombState.EXPLODING;
		} else if(timeToLive == 0) {
			tile.initiateExplosion();
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
				if (t.getTileType().isDestructible())
					t.destroyTile();
				if (t.hasBomb()) {
					if (t.getGridPosition().getX() == this.getTile().getGridPosition().getX())
						sameColumn(t);
					else
						sameLine(t);
				}
				
				t.setExploding(true);
			} else {
				indexes.set(indexes.indexOf(j), -1);
			}
		}
		step++;
	}
	
	public void sameColumn(Tile tile) {
		ProgressiveBomb bomb = (ProgressiveBomb) tile.getBomb();
		//The if else statement prevents that two consecutive bombs destroy 2 consecutive walls.
		if(tile.getGridPosition().getY() > this.getTile().getGridPosition().getY()) {
			bomb.setIndexes(3, -1);
			indexes.set(1, -1);
		} else {
			bomb.setIndexes(1, -1);
			indexes.set(3, -1);
		}
		bomb.setTTL(1);
	}
	
	public void sameLine(Tile tile) {
		ProgressiveBomb bomb = (ProgressiveBomb) tile.getBomb();
		//The if else statement prevents that two consecutive bombs destroy 2 consecutive walls.
		if(tile.getGridPosition().getX() > this.getTile().getGridPosition().getX()) {
			bomb.setIndexes(0, -1);
			indexes.set(2, -1);
		} else {
			bomb.setIndexes(2, -1);
			indexes.set(0, -1);
		}		
		bomb.setTTL(1);
	}
	
	public void setIndexes(int index, int value) {
		indexes.set(index, value);
	}
	
	public void setStep(int step) {
		this.step = step;
	}
}
