package bombers.view;

import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;



public class GUI extends Application {
	public void start(final Stage primaryStage) {
 
        Scene scene = new GameScreenManager().getScene();
 
        primaryStage.setTitle("Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
    public static void main(String[] args) {
	        launch(args);
	}
}