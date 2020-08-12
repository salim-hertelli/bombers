package bombers.control;

import java.util.LinkedList;
import java.util.List;

import bombers.model.Dimensions;
import bombers.model.Direction;
import bombers.model.GameMap;
import bombers.model.Player;
import bombers.model.TileType;
import bombers.view.ViewManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameBoard {
	private final Dimensions dimensions = new Dimensions(600,600);
	private final int xNumber = 30;
	private final int yNumber = 30;
	private final int fps = 30;
	
	List<Player> players;
	Player mainPlayer;
	ViewManager viewManager;
	
	public GameBoard(Stage primaryStage) {
		GameMap map = setupGameMap(); //TODO
		players = new LinkedList<>();
		mainPlayer = new Player("grnvs", map);
		players.add(mainPlayer);
		
		viewManager = new ViewManager(map, players);
		Scene scene = viewManager.getScene();

		// TODO key released
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
					//
					mainPlayer.dropBomb();
					break;
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
	
	private void mainLoop() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000/fps), e-> {
			mainPlayer.move();
			viewManager.repaintAll();
		} ));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
	}
}