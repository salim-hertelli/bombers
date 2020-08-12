package bombers.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameScreenManager {
	private final static String GRID_TILE_FILE_PATH = "src\\main\\java\\bombers\\view\\7x7.png"; 
	
    private int screenWidth = 450;
    private int screenHeight = 450;
    private int xNumber = 31;
    private int yNumber = 31;
	double gridWidth = (double) screenWidth / xNumber;
    double gridHeight = (double) screenHeight / yNumber;
    Scene scene;
    MyNode[][] grid;
    
	public GameScreenManager() {
		this.scene = generateScene();
	}
	
	public GameScreenManager(int screenWidth, int screenHeight, int xNumber, int yNumber) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.xNumber = xNumber;
		this.yNumber = yNumber;
		this.scene = generateScene();
	}
	
	public Scene getScene() {
		return this.scene;
	}
	
	private Scene generateScene() {
		ImageView imageView = null;
		
	    grid = new MyNode[xNumber][yNumber];
	    Pane root = new Pane();
	    root.setPrefSize(screenWidth, screenHeight);
	    for (int i = 0; i < xNumber; i++) {
            for (int j = 0; j < yNumber; j++) {
            	try {
            		imageView = new ImageView(new Image(new FileInputStream(GRID_TILE_FILE_PATH)));
            	} catch (FileNotFoundException e1) {
            		// TODO Auto-generated catch block
            		e1.printStackTrace();
            	}

                // create node
                MyNode node = new MyNode(i * gridWidth, j * gridHeight, gridWidth, gridHeight);
                node.getChildren().add(imageView);
                node.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                	Object source = e.getSource();
                	if (source instanceof MyNode) {
                		((MyNode) source).setColor(Color.ALICEBLUE);
                	}
                });
                if(i % 2 == 1 || j % 2 == 1)
                	node.setColor(Color.GRAY);
                // add node to group
                root.getChildren().add(node);

                grid[i][j] = node;

            }
        }
		
		return new Scene(root, screenWidth, screenHeight);
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
