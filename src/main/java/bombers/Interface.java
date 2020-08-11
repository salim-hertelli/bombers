package bombers;

import java.awt.TextField;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.event.ActionEvent; 
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;



public class Interface extends Application {
	private double sceneWidth = 450;
    private double sceneHeight = 450;
	int n = 31, m = 31; //Ungerade
	double gridWidth = sceneWidth / n;
    double gridHeight = sceneHeight / m;
    
	public void start(final Stage primaryStage) {
	    MyNode[][] playfield = new MyNode[n][m];
	    Pane root = new Pane();
	    root.setPrefSize(sceneWidth, sceneHeight);
	    for( int i=0; i < n; i++) {
            for( int j=0; j < m; j++) {

                // create node
                MyNode node = new MyNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight);
                if(i%2 == 1 || j%2 == 1)
                	node.setColor(Color.GRAY);
                // add node to group
                root.getChildren().add(node);

                playfield[i][j] = node;

            }
        }
 
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
 
        primaryStage.setTitle("Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
    public static void main(String[] args) {
	        launch(args);
	}

    public static class MyNode extends StackPane {
    	Rectangle rectangle; 
    	public MyNode(double x, double y, double width, double height) {
        	rectangle = new Rectangle( width, height);
        	rectangle.setStroke(Color.BEIGE);
            rectangle.setFill(Color.BEIGE);
        	setTranslateX(x);
        	setTranslateY(y);
        	getChildren().addAll(rectangle);
    	}
    	public void setColor(Color c) {
    		rectangle.setStroke(c);
            rectangle.setFill(c);
    	}

}
}