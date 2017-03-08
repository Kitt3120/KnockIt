package de.spezipaul.knockit.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;

public class KittsManager {
	
	private ArrayList<Kitt> kitts = new ArrayList<>();
	private HashMap<Kitt, ArrayList<Player>> activePlayers = new HashMap<>();
	
	public KittsManager() {
		//TODO: register();
	}
	
	public void register(Kitt kitt){
		activePlayers.put(kitt, new ArrayList<>());
		kitts.add(kitt);
	}
	
	public ArrayList<Kitt> getKitts() {
		return kitts;
	}

	public Kitt getKitt(ItemStack item){
		for(Kitt kitt : kitts){
			if(kitt.getGUIItem().equals(item)){
				return kitt;
			}
		}
		return null;
	}
	
	public void setPlayer(Player p, Kitt k){
		ArrayList<Player> players = activePlayers.get(k);
		if(!players.contains(p)) players.add(p);
		activePlayers.put(k, players);
	}
	
	public void removePlayer(Player p, Kitt k){
		ArrayList<Player> players = activePlayers.get(k);
		if(players.contains(p)) players.remove(p);
		activePlayers.put(k, players);		
	}
	
	public ArrayList<Player> getPlayers(Kitt k){
		return activePlayers.get(k);
	}

	public void onFall(Player p) {
		p.getInventory().clear();
		p.setFallDistance(0);
		p.teleport(p.getLocation().getWorld().getSpawnLocation());
		Core.killstreakManager.reset(p);
	}

}
