package bombers.model;

public class Position {
	double x;
	double y;
	
	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void update(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
		return x == other.x && y == other.y;
	}
	
	/*
	 * a and b must have either the same x or the same y value which is the case 
	 * if we follow the rule that a player can't stand on 4 tiles at the same time
	 */
	public static double measureDistance(Position a, Position b) {
		return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
	}
}
 