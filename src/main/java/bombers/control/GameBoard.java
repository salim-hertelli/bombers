package bombers.control;

import bombers.view.ViewManager;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameBoard {
	
	
	public GameBoard(Stage primaryStage) {
        Scene scene = new ViewManager().getScene();
        primaryStage.setTitle("Bombers");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
}
