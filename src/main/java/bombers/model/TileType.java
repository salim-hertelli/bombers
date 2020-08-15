package bombers.model;

public enum TileType {
	FREE(false, false), WALL(true, true), OBSTACLE(false, true);
	
	private boolean isDestructible;
	private boolean blocksBombPropagation;
	
	TileType(boolean isDestructible, boolean blocksBombPropagation) {
		this.isDestructible = isDestructible;
		this.blocksBombPropagation = blocksBombPropagation;
	}
	
	public boolean getIsDestructible() {
		return isDestructible;
	}
	
	public boolean blocksBombPropagation() {
		return blocksBombPropagation;
	}
}