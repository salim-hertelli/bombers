package bombers.model;

import java.io.IOException; 
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import bombers.control.GameBoard;
import bombers.view.Tile;

/*
 * This class uses files that serve as a description of a game map
 * The content of the file is parsed and the generated game map is
 * accessible via getters and setters
 */
public class GameMap {
	private final Dimensions teilDimensions = new Dimensions(40,40);
	private Tile[][] tiles;
	private Dimensions dimensions; // dimensions of the map
	private int xNumber; // number of tiles in a line
	private int yNumber; // number of tiles in a column
	private List<Player> players;
	private GameBoard gameBoard;
	
	public GameMap(String fileName, List<Player> players) {
		this(fileName, players, false);
	}
	
	public GameMap(String fileName, List<Player> players, boolean ignoreMapVerification) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		yNumber = lines.size();
		xNumber = lines.get(0).length();
		System.out.println(xNumber +" "+ yNumber);
		this.dimensions = new Dimensions(xNumber * getTileWidth(), yNumber * getTileHeight());

		generateTiles(lines);
		if (!ignoreMapVerification && !isValid()) {
			System.err.println("The map is invalid!");
			System.exit(0);
		}
	}
	
	private boolean isValid() {
		if (tiles.length < 2 || tiles[0].length < 2) {
			return true;
		}
		
		// this loop verifies that no 4 destructible Tiles are adjacent forming a bigger square
		for (int i = 0; i < tiles.length - 1; i++) {
			for (int j = 0; j < tiles[0].length - 1; j++) {
				TileType upperLeft = tiles[i][j].getTileType();
				TileType upperRight = tiles[i][j+1].getTileType();
				TileType lowerLeft = tiles[i+1][j].getTileType();
				TileType lowerRight = tiles[i+1][j+1].getTileType();
				if ((upperLeft.isDestructible() || upperLeft == TileType.FREE)
						&& (upperRight.isDestructible() || upperRight == TileType.FREE)
						&& (lowerRight.isDestructible() || lowerRight == TileType.FREE)
						&& (lowerLeft.isDestructible() || lowerLeft == TileType.FREE)) {
					return false;
				}
			}
		}
		
		
		// this loops count the total number of non destructible tiles in the map
		int totalNumber = 0;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				TileType currentType = tiles[i][j].getTileType();
				if (currentType.isDestructible() || currentType == TileType.FREE) {
					totalNumber++;
				}
			}
		}
		
		
		// this loop count the number of the reachable tiles from (0,0)
		Set<Tile> markedTiles = new HashSet<>();
		List<Tile> toCheck = new LinkedList<>();
		Tile firstTile = tiles[0][0];
		toCheck.add(firstTile);
		markedTiles.add(firstTile);
		int reachableNumber = (firstTile.getTileType().isReachable())? 1 : 0;
		while (toCheck.size() != 0) {
			Tile currentTile = toCheck.remove(0);
			Tile[] neighbors = {getTopNeighbor(currentTile, 1), getBotNeighbor(currentTile, 1), 
					getRightNeighbor(currentTile, 1), getLeftNeighbor(currentTile, 1)};
			for (Tile neighbor : neighbors) {
				if (neighbor != null && (neighbor.getTileType().isReachable()
						&& !markedTiles.contains(neighbor))) {
					reachableNumber++;
					markedTiles.add(neighbor);
					toCheck.add(neighbor);
				}
			}
		}
		
		return totalNumber == reachableNumber;
	}
	
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	private TileType interpretTileTypeFromChar(char c) {
		switch(c) {		
			case 'W': return TileType.WALL;
			case 'F': return TileType.FREE;
			case 'O': return TileType.OBSTACLE;
			default: throw new IllegalArgumentException("Character not allowed in the game map file");
		}
	}

	public int getTileHeight() {
		return teilDimensions.getHeight();
	}

	public int getTileWidth() {
		return teilDimensions.getWidth();
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
		System.out.println(lines.size());
		System.out.println(lines.get(0).length());
		System.out.println(xNumber);
		System.out.println(yNumber);
		tiles = new Tile[yNumber][xNumber];
		for (int j = 0; j < yNumber; j++) {
			String currentLine = lines.get(j);
			for (int i = 0; i < xNumber; i++) {
				Position tilePosition = new Position(i * getTileWidth(), j * getTileHeight());
				TileType tileType = interpretTileTypeFromChar(currentLine.charAt(i));
				Tile newTile = new Tile(tilePosition, new Position(i,j), getTileDimensions()
						, tileType, this);
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
	
	public Tile getRightNeighbor(Tile tile, int step) {
		if(tile.getGridPosition().getX() + step < tiles.length)
			return tiles[(int) (tile.getGridPosition().getX() + step)][(int) tile.getGridPosition().getY()];
		return null;
	}
	
	public Tile getTopNeighbor(Tile tile, int step) {
		if(tile.getGridPosition().getY() - step >= 0)
			return tiles[(int) tile.getGridPosition().getX()][(int) (tile.getGridPosition().getY() - step)];
		return null;
	}
	
	public Tile getBotNeighbor(Tile tile, int step) {
		if(tile.getGridPosition().getY() + step < tiles[0].length)
			return tiles[(int) tile.getGridPosition().getX()][(int) (tile.getGridPosition().getY() + step)];
		return null;
		}
	
	public Tile getLeftNeighbor(Tile tile, int step) {
		if(tile.getGridPosition().getX() - step >= 0)
			return tiles[(int) (tile.getGridPosition().getX() - step)][(int) tile.getGridPosition().getY()];
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
}
