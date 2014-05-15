package hu.lilacode.hitnsync.game.view;

import hu.lilacode.hitnsync.game.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerStats {
	
	public List<Player> stats ;
	public PlayerStats(){
		stats =  new ArrayList<Player>(10);
		
		Player p = new Player();
		
		for (int i = 0; i < 10; i++){
			stats.set(i, p);
		}
		
	}

}
