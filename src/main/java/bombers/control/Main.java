package bombers.control;

import javafx.application.Application;
import javafx.stage.Stage;
 
public class Main extends Application{
	public void start(final Stage primaryStage) {
		new GameBoard(primaryStage);
    }
	
    public static void main(String[] args) {
	        launch(args);
	}
}
