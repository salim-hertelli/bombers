package bombers.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import bombers.control.GameBoard;
import bombers.view.Tile;

/*
 * This class uses files that serve as a description of a game map
 * The content of the file is parsed and the generated game map is
 * accessible via getters and setters
 */
public class GameMap {
	private Tile[][] tiles;
	private Dimensions dimensions; // dimensions of the map
	private int xNumber; // number of tiles in a line
	private int yNumber; // number of tiles in a column
	private List<Player> players;
	private GameBoard gameBoard;
	
	public GameMap(String fileName, Dimensions dimensions, List<Player> players) {
		this.dimensions = dimensions;
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		yNumber = lines.size();
		xNumber = lines.get(0).length();
		
		generateTiles(lines);
	}
	
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	private TileType interpretTileTypeFromChar(char c) {
		if (c == 'W') {
			return TileType.WALL;
		} else if (c == 'F') {
			return TileType.FREE;
		} else {
			throw new IllegalArgumentException("Character not allowed in the game map file");
		}
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
	
	private void generateTiles(List<String> lines) {
		tiles = new Tile[yNumber][xNumber];
		for (int j = 0; j < yNumber; j++) {
			String currentLine = lines.get(j);
			for (int i = 0; i < xNumber; i++) {
				Position tilePosition = new Position(i * getTileWidth(), j * getTileHeight());
				TileType tileType = interpretTileTypeFromChar(currentLine.charAt(i));
				Tile newTile = new Tile(tilePosition, new Position(i,j), getTileDimensions(), tileType);
				tiles[i][j] = newTile;
			}
		}
	}
	
	public void setTileType(int x, int y, TileType type) {
		tiles[x][y].setTileType(type);
	}
	
	/*
	 * returns the tile whose index is (x,y)
	 */
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public Tile getRightNeighbor(Tile tile, int n) {
		if(tile.getGridPosition().getX() + n < tiles.length)
			return tiles[(int) (tile.getGridPosition().getX() + n)][(int) tile.getGridPosition().getY()];
		return null;
	}
	
	public Tile getTopNeighbor(Tile tile, int n) {
		if(tile.getGridPosition().getY() - n >= 0)
			return tiles[(int) tile.getGridPosition().getX()][(int) (tile.getGridPosition().getY() - n)];
		return null;
	}
	
	public Tile getBotNeighbor(Tile tile, int n) {
		if(tile.getGridPosition().getY() + n < tiles[0].length)
			return tiles[(int) tile.getGridPosition().getX()][(int) (tile.getGridPosition().getY() + n)];
		return null;
		}
	
	public Tile getLeftNeighbor(Tile tile, int n) {
		if(tile.getGridPosition().getX() - n >= 0)
			return tiles[(int) (tile.getGridPosition().getX() - n)][(int) tile.getGridPosition().getY()];
		return null;
	}
	
	/*
	 * returns the tile to which the specified pixel belongs
	 */
	public Tile getTileAtPosition(Position position) {
		int x = (int) (position.getX() / getTileWidth());
		int y = (int) (position.getY() / getTileHeight());
		return tiles[x][y];
	}
	
	public List<Player> getPlayersAtTile(Tile tile) {
		List<Player> result = new LinkedList<>();
		for (Player player : players) {
			Position topLeft = player.getPosition();
			Position lowRight = player.getLowRightCornerPosition();
			Position topRight = new Position(lowRight.getX(), topLeft.getY());
			Position lowLeft = new Position(topLeft.getX(), lowRight.getY());
			if (getTileAtPosition(topLeft) == tile ||
					getTileAtPosition(lowRight) == tile ||
					getTileAtPosition(topRight) == tile ||
					getTileAtPosition(lowLeft) == tile) {
				result.add(player);
			}
		}
		return result;
	}
	
	public void setGameBoard(GameBoard gB) {
		gameBoard = gB;
	}
	
	public void addBombToExplode(ProgressiveBomb pb) {
		gameBoard.addBombToExplode(pb);
	}
	
	public void removeBombToExplode(ProgressiveBomb pb) {
		gameBoard.explosionTerminated(pb);
	}
}
