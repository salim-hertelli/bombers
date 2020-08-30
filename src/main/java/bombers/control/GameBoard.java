package bombers.control;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import bombers.model.Dimensions;
import bombers.model.Direction;
import bombers.model.GameMap;
import bombers.model.Player;
import bombers.model.Position;
import bombers.model.ProgressiveBomb;
import bombers.view.ViewManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameBoard {
	private final String mapFilePath = "src/main/java/bombers/control/map.txt";
	public final static int FPS = 60;
	
	private Stage primaryStage;
	private Dimensions dimensions;
	private Button replay;
	private Button quit;
	List<Player> players;
	Player mainPlayer;
	ViewManager viewManager;
    List<ProgressiveBomb> bombsInExplosion = new ArrayList<>();

	
	public GameBoard(Stage primaryStage) {
		GameMap map = setupGameMap();
		this.dimensions = map.getDimensions();
		this.primaryStage = primaryStage;
		
		players = new LinkedList<>();
		mainPlayer = new Player("grnvs", map, new Position(0, 0));
		Player secondPlayer = new Player("second", map, new Position(map.getDimensions().getWidth() 
				- map.getTileWidth(), 0));
		players.add(mainPlayer);
		players.add(secondPlayer);
		map.setPlayers(players);
		
		viewManager = new ViewManager(map, players);
		Scene scene = viewManager.getScene();

		setPlayersHandler(mainPlayer, secondPlayer, scene);
		
        primaryStage.setTitle("Bombers");
        primaryStage.setScene(scene);
        primaryStage.show();
        mainLoop();
	}

	private GameMap setupGameMap() {
		GameMap map = new GameMap(mapFilePath, null);
		map.setGameBoard(this);
		return map;
	}
	
	private void setPlayersHandler(Player mainPlayer, Player secondPlayer, Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent> () {
			
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
					case UP: 
						if (Direction.UP != mainPlayer.getDirection()) {
							mainPlayer.setDirection(Direction.UP);
						}
						break;
					case DOWN: 				
						if (Direction.DOWN != mainPlayer.getDirection()) {
							mainPlayer.setDirection(Direction.DOWN);
						}
						break;
					case LEFT: 					
						if (Direction.LEFT != mainPlayer.getDirection()) {
							mainPlayer.setDirection(Direction.LEFT);
						}
						break;
					case RIGHT: 					
						if (Direction.RIGHT != mainPlayer.getDirection()) {
							mainPlayer.setDirection(Direction.RIGHT);
						}
						break;
					case SPACE:
						mainPlayer.setWantsToDrop();
						break;
					case Z: 
						if (Direction.UP != secondPlayer.getDirection()) {
							secondPlayer.setDirection(Direction.UP);
						}
						break;
					case S: 				
						if (Direction.DOWN != secondPlayer.getDirection()) {
							secondPlayer.setDirection(Direction.DOWN);
						}
						break;
					case Q: 					
						if (Direction.LEFT != secondPlayer.getDirection()) {
							secondPlayer.setDirection(Direction.LEFT);
						}
						break;
					case D: 					
						if (Direction.RIGHT != secondPlayer.getDirection()) {
							secondPlayer.setDirection(Direction.RIGHT);
						}
						break;
					case P:
						secondPlayer.setWantsToDrop();
						break;
				}
			}
        });
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent> () {
			public void handle(KeyEvent event) {
				System.out.println(event.getCode());
				boolean playerLocked = mainPlayer.isLocked();
				Direction direction = (playerLocked) ? mainPlayer.getLockDirection() : mainPlayer.getDirection();
				if (event.getCode() == KeyCode.DOWN && direction == Direction.DOWN ||
						event.getCode() == KeyCode.UP && direction == Direction.UP ||
						event.getCode() == KeyCode.RIGHT && direction == Direction.RIGHT ||
						event.getCode() == KeyCode.LEFT && direction == Direction.LEFT) {
					mainPlayer.setDirection(Direction.REST);
				}
				
				boolean secondPlayerLocked = secondPlayer.isLocked();
				Direction secondDirection = (secondPlayerLocked) ? 
						secondPlayer.getLockDirection() : secondPlayer.getDirection();
				if (event.getCode() == KeyCode.S && secondDirection == Direction.DOWN ||
						event.getCode() == KeyCode.Z && secondDirection == Direction.UP ||
						event.getCode() == KeyCode.D && secondDirection == Direction.RIGHT ||
						event.getCode() == KeyCode.Q && secondDirection == Direction.LEFT) {
					secondPlayer.setDirection(Direction.REST);
				}
				
				if (event.getCode() == KeyCode.SPACE) {
					mainPlayer.setDoesntWantToDrop();
				}
				if (event.getCode() == KeyCode.P) {
					secondPlayer.setDoesntWantToDrop();
				}
			}
        });
	}
	
	private void mainLoop() {
		// TODO make the fps change dynamically with a topFPSLimit being the constant 
		// "MainloopIteration"ps used by the server
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000 / FPS), e-> {
			boolean allDead = true;
			List<Player> playersToKill = new LinkedList<>();
			for (Player player : players) {
				// each player updates his bombs during his move
				// thus the main loop does not explicitly update the bombs
				player.move();
				// TODO: I think this verification should erst be made after all players have been moved
				if (!player.isAlive()) {					
					playersToKill.add(player);
				}
				else {					
					allDead = false;
				}
			}
			for (Player player : playersToKill) {
				player.kill();
				players.remove(player);
			}
			if(allDead) {
				gameOver();
				mainPlayer.getAnimation().stop();
			}
			viewManager.repaintAll();
		}));
		mainPlayer.setAnimation(timeline);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
	}
	
	private void gameOver() {
		replay = new Button();
	    replay.setText("Replay");
	    EventHandler<ActionEvent> replayEvent = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) { 
            	new GameBoard(primaryStage);
            	((Stage)replay.getScene().getWindow()).close();
            } 
        };
	    replay.setOnAction(replayEvent);
	    
	    quit = new Button();
	    quit.setText("Quit");

	    EventHandler<ActionEvent> quitEvent = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) { 
            	((Stage)quit.getScene().getWindow()).close();
            	primaryStage.close();
            } 
        };
	    quit.setOnAction(quitEvent);
	    
        FlowPane secondaryLayout = new FlowPane();
        secondaryLayout.getChildren().addAll(replay, quit);
        
        Scene secondScene = new Scene(secondaryLayout, 230, 100);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Game over");
        newWindow.setScene(secondScene);

        newWindow.initModality(Modality.WINDOW_MODAL);

        newWindow.initOwner(primaryStage);

        newWindow.setX(primaryStage.getX() + primaryStage.getWidth()/2 - secondScene.getWidth()/2);
        newWindow.setY(primaryStage.getY() + primaryStage.getHeight()/2 - secondScene.getHeight()/2);

        newWindow.show();
	}
}