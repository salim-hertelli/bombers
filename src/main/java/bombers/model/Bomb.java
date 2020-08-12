package bombers.model;

public class Bomb {
	private int timeToLive = 999999;
	private Player player;
	
	public Bomb(Player player) {
		this.player = player;
	}
	
	public void countDown() {
		if (timeToLive-- == 0) {
			detonate();
		}
	}
	
	private void detonate() {
		player.removeBomb(this);
	}
}
