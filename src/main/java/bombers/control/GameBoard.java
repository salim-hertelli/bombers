package bombers.control;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import bombers.model.Dimensions;
import bombers.model.Direction;
import bombers.model.GameMap;
import bombers.model.Player;
import bombers.model.Position;
import bombers.model.ProgressiveBomb;
import bombers.view.ViewManager;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameBoard {
	private final String mapFilePath = "src/main/java/bombers/control/map.txt";
	private final Dimensions dimensions = new Dimensions(750,750);
	private final int fps = 30;
	
	List<Player> players;
	Player mainPlayer;
	ViewManager viewManager;
    List<ProgressiveBomb> bombsInExplosion = new ArrayList();

	
	public GameBoard(Stage primaryStage) {
		GameMap map = setupGameMap();
		players = new LinkedList<>();
		mainPlayer = new Player("grnvs", map, new Position(100,100));
		players.add(mainPlayer);
		map.setPlayers(players);
		
		viewManager = new ViewManager(map, players);
		Scene scene = viewManager.getScene();

		setMainPlayerHandler(scene);
		
        primaryStage.setTitle("Bombers");
        primaryStage.setScene(scene);
        primaryStage.show();
        mainLoop();
	}

	private GameMap setupGameMap() {
		GameMap map = new GameMap(mapFilePath, dimensions, null);
		map.setGameBoard(this);
		return map;
	}
	
	private void setMainPlayerHandler(Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent> () {
			
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case UP: 
					mainPlayer.setDirection(Direction.UP);
					break;
				case DOWN: 				
					mainPlayer.setDirection(Direction.DOWN);
					break;
				case LEFT: 					
					mainPlayer.setDirection(Direction.LEFT);
					break;
				case RIGHT: 					
					mainPlayer.setDirection(Direction.RIGHT);
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
	}
	
	public void addBombToExplode(ProgressiveBomb pb) {
		bombsInExplosion.add(pb);
	}
	
	public void explosionTerminated(ProgressiveBomb pb) {
		bombsInExplosion.remove(pb);
	}
	
	private void mainLoop() {
		// TODO make the fps change dynamically with a topFPSLimit being the constant "MainloopIteration"ps used by the server
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000/fps), e-> {
			// each player updates his bombs during his move
			// thus the main loop does not explicitly update the bombs
			for (Player player : players) {
				player.move();
			}
			
			// now after all the bombs got updated check which players died
			for (Player player : players) {
				if (!player.isAlive()) {
					players.remove(player);
				}
			}
			
			viewManager.repaintAll();} ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
	}
}