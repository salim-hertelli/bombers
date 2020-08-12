package bombers.model;

public enum TileType {
	FREE("src\\main\\java\\bombers\\view\\tile.png"), WALL("src\\main\\java\\bombers\\view\\wall.png");
	String imagePath; 
	
	TileType(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public String getImagePath() {
		return this.imagePath;
	}
}