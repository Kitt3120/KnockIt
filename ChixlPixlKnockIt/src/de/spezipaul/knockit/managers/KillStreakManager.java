package de.spezipaul.knockit.managers;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.spezipaul.knockit.Core;

public class KillStreakManager implements Listener {
	
	private HashMap<Player, Integer> currentStreaks = new HashMap<>();
	
	public KillStreakManager() {}
	
	public void add(Player p, int amount){
		int currentAmount = get(p);
		currentAmount += amount;
		if(currentAmount < 0) currentAmount = 0;
		set(p, currentAmount);
		String message = Core.privateKillstreakMessage.replace("%NAME", p.getName()).replace("%COUNT", String.valueOf(currentAmount));
		p.sendMessage(Core.PLPrefix + message);
		if(currentAmount % Core.globalKillstreakDivider == 0){
			String globalMessage = Core.globalKillstreakMessage.replace("%NAME", p.getName()).replace("%COUNT", String.valueOf(currentAmount));
			Core.instance.getServer().broadcastMessage(Core.PLPrefix + globalMessage);
		}
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
		int current = get(p);
		if(current > 0){
			String message = Core.privateKillstreakEndMessage.replace("%NAME", p.getName()).replace("%COUNT", String.valueOf(current));
			p.sendMessage(Core.PLPrefix + message);	
			if(current >= Core.globalKillstreakDivider){
				String globalMessage = Core.globalKillstreakEndMessage.replace("%NAME", p.getName()).replace("%COUNT", String.valueOf(current));
				Core.instance.getServer().broadcastMessage(Core.PLPrefix + globalMessage);
			}		
		}
		set(p, 0);
	}
	
	public void set(Player p, int amount){
		currentStreaks.put(p, amount);
	}

}
