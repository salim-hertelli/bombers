package bombers.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import bombers.view.Tile;

public class GameMap {
	Tile[][] tiles;
	private Dimensions dimensions;
	private int xNumber;
	private int yNumber;
	
	/*
	 * initializes all the tiles as FREE
	 */
	public GameMap(Dimensions dimensions, int xNumber, int yNumber) {
		this.dimensions = dimensions;
		this.xNumber = xNumber;
		this.yNumber = yNumber;
		
		generateTiles();
	}
	
	public int getTileHeight() {
		return dimensions.getHeight() / yNumber;
	}
	
	public int getTileWidth() {
		return dimensions.getWidth() / xNumber;
	}
	
	public Dimensions getTileDimensions() {
		return new Dimensions(getTileWidth(), getTileHeight());
	}
	
	public Dimensions getDimensions() {
		return dimensions;
	}
	
	public Collection<Tile> getTiles() {
		List<Tile> list = new LinkedList<>();
		for (Tile[] first : tiles) {
			for (Tile second : first) {
				list.add(second);
			}
		}
		
		return list;
	}
	
	private void generateTiles() {
		tiles = new Tile[xNumber][yNumber];
		for (int i = 0; i < xNumber; i++) {
			for (int j = 0; j < yNumber; j++) {
				Dimensions dimensions = getTileDimensions();
				Position position = new Position(i * getTileWidth(), j * getTileHeight());
				tiles[i][j] = new Tile(position, dimensions, TileType.FREE);
			}
		}
	}
	
	public void setTileType(int x, int y, TileType type) {
		tiles[x][y].setTileType(type);
	}
	
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}
}
