package bombers.model;

import java.util.ArrayList;
import java.util.List;

import bombers.view.Tile;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ProgressiveBomb extends Bomb{
	//private Timeline bombTimeline;
	private int lengthOfImpact = 20;
	private int step = 0;
	private List<Integer> indexes = new ArrayList<>(); 
	
	public ProgressiveBomb(Tile tile, GameMap map) {
		super(tile, map);
		for (int i=0; i < 4;)
	        indexes.add(i++);
	}
	
	public BombState countDown() {
		if(timeToLive-- == -lengthOfImpact){
			explode();
			return BombState.EXPLODED;
		}else if(timeToLive < 0){
			explode();
			return BombState.EXPLODING;
		}else if (timeToLive == 0) {
			tile.removeBomb();
			return BombState.EXPLODING;
		}
		return BombState.DROPPED;
	}
	
	public void explode() {
		Tile[] toDestroy = { map.getLeftNeighbor(tile, step), map.getBotNeighbor(tile, step), map.getRightNeighbor(tile, step), map.getTopNeighbor(tile, step)};
		for(int j:indexes) {
			if(j >= 0 && toDestroy[j] != null && toDestroy[j].getTileType().isDestructible()) {
				Tile t = toDestroy[j];
				for (Player player : map.getPlayersAtTile(t)) 
					player.kill();
				if (!t.isFree()) { 
					t.setTileType(TileType.FREE);
					indexes.set(indexes.indexOf(j), -1);
				}
			}else {
				indexes.set(indexes.indexOf(j), -1);
			}
		}
		step++;
		if(step == lengthOfImpact) 
			map.removeBombToExplode(this);
	}
}
