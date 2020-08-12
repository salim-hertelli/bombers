package bombers.model;

public class Position {
	double x;
	double y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void update(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
 