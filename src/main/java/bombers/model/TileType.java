package bombers.model;

import javafx.scene.image.Image;

public enum TileType {
	FREE, WALL;
	private boolean isDestructible = true;
	
	public void setDestructible(boolean isDestructible) {
		this.isDestructible = isDestructible;
	}
	
	public boolean isDestructible() {
		return isDestructible;
	}
}