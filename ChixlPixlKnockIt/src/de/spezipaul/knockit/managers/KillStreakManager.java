package de.spezipaul.knockit.managers;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class KillStreakManager implements Listener {
	
	private HashMap<Player, Integer> currentStreaks = new HashMap<>();
	
	public KillStreakManager() {}
	
	public void add(Player p, int amount){
		int currentAmount = get(p);
		currentAmount += amount;
		if(currentAmount < 0) currentAmount = 0;
		set(p, currentAmount);
	}
	
	public void remove(Player p, int amount){
		int currentAmount = get(p);
		currentAmount -= amount;
		if(currentAmount < 0) currentAmount = 0;
		set(p, currentAmount);
	}
	
	public int get(Player p){
		int amount = 0;
		if(currentStreaks.containsKey(p)) amount = currentStreaks.get(p);
		return amount;
	}
	
	public void reset(Player p){
		set(p, 0);
	}
	
	public void set(Player p, int amount){
		currentStreaks.put(p, amount);
	}

}
