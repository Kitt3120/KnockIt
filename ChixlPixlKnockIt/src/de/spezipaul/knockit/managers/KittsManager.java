package de.spezipaul.knockit.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.kitts.KittType;
import de.spezipaul.knockit.kitts.kitts.Schuetze;
import de.spezipaul.knockit.kitts.kitts.Teleporter;

public class KittsManager {
	
	private ArrayList<KittDescription> availableKitts = new ArrayList<>();
	private HashMap<Player, Kitt> activeKitts = new HashMap<>();
	
	public KittsManager() {
		register(new KittDescription(Teleporter.name, Teleporter.description, KittType.Teleporter));
		register(new KittDescription(Schuetze.name, Schuetze.description, KittType.Schuetze));
	}
	
	public void register(KittDescription desc){
		availableKitts.add(desc);
	}
	
	public ArrayList<KittDescription> getKitts() {
		return availableKitts;
	}

	public Kitt getKitt(Player player){
		return activeKitts.get(player);
	}
	
	public void setPlayer(Player player, Kitt kitt) {
		if(activeKitts.containsKey(player)) removePlayer(player);
		activeKitts.put(player, kitt);
	}
	
	public void removePlayer(Player player){
		getKitt(player).onRemove();
		activeKitts.remove(player);
	}
	
	public ArrayList<Player> getPlayers(Kitt kitt){
		ArrayList<Player> players = new ArrayList<>();
		for(Entry<Player, Kitt> ent : activeKitts.entrySet()){
			if(ent.getValue().getName().equalsIgnoreCase(kitt.getName())) players.add(ent.getKey());
		}
		return players;
	}
	
	public ItemStack getGUIItem(KittType type){
		
	}

	public void onFall(Player p) {
		p.getInventory().clear();
		p.setFallDistance(0);
		p.teleport(p.getLocation().getWorld().getSpawnLocation());
		Core.killstreakManager.reset(p);
	}

}
