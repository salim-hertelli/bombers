package bombers.model;

import bombers.view.Tile;

public class ProgressiveBomb extends Bomb{
	private int lengthOfImpact = 5;
	
	public ProgressiveBomb(Tile tile, GameMap map) {
		super(tile, map);
	}

	@Override
	void explode() {
		// TODO this solution is not smart, find something better
		Tile currentTile = tile;
		int traveledDistance = 0;
		do {
			currentTile = map.getBotNeighbor(currentTile);
			traveledDistance++;
			for (Player player : map.getPlayersAtTile(currentTile)) {
				player.kill();
			};
		} while (currentTile.isFree() && traveledDistance <= lengthOfImpact);
		
		if (!currentTile.isFree()) {
			currentTile.setTileType(TileType.FREE);
		}
		
		currentTile = tile;
		traveledDistance = 0;
		do {
			currentTile = map.getLeftNeighbor(currentTile);
			traveledDistance++;
			for (Player player : map.getPlayersAtTile(currentTile)) {
				player.kill();
			};
		} while (currentTile.isFree() && traveledDistance <= lengthOfImpact);
		
		if (!currentTile.isFree()) {
			currentTile.setTileType(TileType.FREE);
		}
		
		currentTile = tile;
		traveledDistance = 0;
		do {
			currentTile = map.getRightNeighbor(currentTile);
			traveledDistance++;
			for (Player player : map.getPlayersAtTile(currentTile)) {
				player.kill();
			};
		} while (currentTile.isFree() && traveledDistance <= lengthOfImpact);
		
		if (!currentTile.isFree()) {
			currentTile.setTileType(TileType.FREE);
		}
		
		currentTile = tile;
		traveledDistance = 0;
		do {
			currentTile = map.getTopNeighbor(currentTile);
			traveledDistance++;
			for (Player player : map.getPlayersAtTile(currentTile)) {
				player.kill();
			};
		} while (currentTile.isFree() && traveledDistance <= lengthOfImpact);
		
		if (!currentTile.isFree()) {
			currentTile.setTileType(TileType.FREE);
		}
	}
}
