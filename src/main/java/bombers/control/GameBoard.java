package bombers.control;

import java.util.LinkedList;
import java.util.List;

import bombers.model.Dimensions;
import bombers.model.Direction;
import bombers.model.GameMap;
import bombers.model.Player;
import bombers.model.Position;
import bombers.model.TileType;
import bombers.view.ViewManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameBoard {
	private final Dimensions dimensions = new Dimensions(750,750);
	private final int xNumber = 30;
	private final int yNumber = 30;
	private final int fps = 30;
	
	List<Player> players;
	Player mainPlayer;
	ViewManager viewManager;
	
	public GameBoard(Stage primaryStage) {
		GameMap map = setupGameMap(); //TODO
		players = new LinkedList<>();
		mainPlayer = new Player("grnvs", map, new Position(50,50));
		players.add(mainPlayer);
		
		viewManager = new ViewManager(map, players);
		Scene scene = viewManager.getScene();

		scene.setOnKeyPressed(new EventHandler<KeyEvent> () {
			Direction previous;
			
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP: 
					previous = Direction.UP;
					mainPlayer.setDirection(previous);
					break;
				case DOWN: 				
					previous = Direction.DOWN;
					mainPlayer.setDirection(previous);
					break;
				case LEFT: 					
					previous = Direction.LEFT;
					mainPlayer.setDirection(previous);
					break;
				case RIGHT: 					
					previous = Direction.RIGHT;
					mainPlayer.setDirection(previous);
					break;
				case SPACE:
					mainPlayer.setWantsToDrop();
					break;
				}
			}
        });
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent> () {
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.DOWN && mainPlayer.getDirection() == Direction.DOWN ||
						event.getCode() == KeyCode.UP && mainPlayer.getDirection() == Direction.UP ||
						event.getCode() == KeyCode.RIGHT && mainPlayer.getDirection() == Direction.RIGHT ||
						event.getCode() == KeyCode.LEFT && mainPlayer.getDirection() == Direction.LEFT) {
					mainPlayer.setDirection(Direction.REST);
				}
			}
        });
		
        primaryStage.setTitle("Bombers");
        primaryStage.setScene(scene);
        primaryStage.show();
        mainLoop();
	}
	
	private GameMap setupGameMap() {
		GameMap map = new GameMap(dimensions, xNumber, yNumber);
		map.setTileType(2,2,TileType.WALL);
		return map;
	}
	
	class MainLoop implements Runnable {

		Player mainPlayer;
		ViewManager manager;
		
		public MainLoop(Player mainPlayer, ViewManager manager) {
			this.mainPlayer = mainPlayer;
			this.manager = manager;
		}
		
		@Override
		public void run() {
			long start;
			long toSleep;
			while (true) {
				System.out.println("im running");
				start = System.currentTimeMillis();
				mainPlayer.move();
				viewManager.repaintAll();
				System.out.println(System.currentTimeMillis() - start);
				toSleep = 1000/fps - (System.currentTimeMillis() - start);
				try {
					Thread.sleep(toSleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}
		}
		
	}
	
	private void mainLoop() {
		MainLoop loop = new MainLoop(mainPlayer, viewManager);
		new Thread(loop).start();
	}
}