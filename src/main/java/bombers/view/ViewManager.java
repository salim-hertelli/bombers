package bombers.view;

import java.awt.SecondaryLoop;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ViewManager {
	private final static String FREE_TILE_FILE_PATH = "src\\main\\java\\bombers\\view\\tile.png";
	private final static String WALL_TILE_FILE_PATH = "src\\main\\java\\bombers\\view\\wall.png";
	private final static String BOMB_FILE_PATH = "src\\main\\java\\bombers\\view\\bomb.png";
	
    private int screenWidth = 750;
    private int screenHeight = 750;
    private int xNumber = 30;
    private int yNumber = 30;
	double gridWidth;
    double gridHeight;
    Scene scene;
    Tile[][] tiles;

	public ViewManager() {
		gridWidth = (double) screenWidth / xNumber;
		gridHeight = (double) screenHeight / yNumber;
		this.tiles = new Tile[xNumber][yNumber];

		this.scene = setupScene();
	}
	
	public ViewManager(int screenWidth, int screenHeight, int xNumber, int yNumber) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.xNumber = xNumber;
		this.yNumber = yNumber;
		gridWidth = (double) screenWidth / xNumber;
		gridHeight = (double) screenHeight / yNumber;
		this.tiles = new Tile[xNumber][yNumber];

		this.scene = setupScene();
	}
	
	public Scene getScene() {
		return this.scene;
	}
	
	private Scene setupScene() {
		Canvas canvas = new Canvas();
		canvas.setWidth(screenWidth);
		canvas.setHeight(screenHeight);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		for (int i = 0; i < xNumber; i++) {
            for (int j = 0; j < yNumber; j++) {
            	boolean iswall = (i == xNumber / 2)  || (j == yNumber / 2);
            	tiles[i][j] = new Tile(i * gridWidth, j * gridHeight, gridWidth, gridHeight, gc, iswall);
            	if (i % 3 == 0 || j % 5 == 0) {
            		tiles[i][j].setBomb();
            	}
            	tiles[i][j].paint();
            }
		}
		Pane root = new Pane();
		root.setPrefSize(screenWidth, screenHeight);
		root.getChildren().add(canvas);

		return new Scene(root, screenWidth, screenHeight);
	}
	
	
	
    public static class Tile {
    	// TODO class ki zeby, fix it
    	Rectangle rectangle; 
    	GraphicsContext gc;
    	double x;
    	double y;
    	double width;
    	double height;
    	boolean isWall;
    	boolean hasBomb;
    	
    	public Tile(double x, double y, double width, double height, GraphicsContext gc, boolean isWall) {
        	this.isWall = isWall;
        	this.gc = gc;
        	this.x = x;
        	this.y = y;
        	this.width = width;
        	this.height = height;
    	}
    	
    	public void setBomb() {
    		hasBomb = true;
    	}

    	public void paint() {
    		try {
				Image image = null;
				
				if (!isWall) {
					image = new Image(new FileInputStream(FREE_TILE_FILE_PATH));
				} else {
					image = new Image(new FileInputStream(WALL_TILE_FILE_PATH));
				}
								
    			gc.drawImage(image, x, y);
    			
    			if (hasBomb) {
    				gc.drawImage(new Image(new FileInputStream(BOMB_FILE_PATH)),x,y);
    			}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
    	}
    }
}
