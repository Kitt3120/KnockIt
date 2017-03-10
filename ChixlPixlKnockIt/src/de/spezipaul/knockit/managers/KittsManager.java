package de.spezipaul.knockit.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.kitts.KittType;
import de.spezipaul.knockit.kitts.kitts.Sch�tze;
import de.spezipaul.knockit.kitts.kitts.Teleporter;

public class KittsManager {
	
	private ArrayList<KittDescription> availableKitts = new ArrayList<>();
	private HashMap<Player, Kitt> activeKitts = new HashMap<>();
	
	public KittsManager() {
		new KittDescription(KittType.Teleporter, "Teleporter", new String[]{"Kann sich zur�ck teleportieren"}, Material.BLAZE_ROD);
		new KittDescription(KittType.Sch�tze, "Sch�tze", new String[]{"Einen starken Bogen und 4 Pfeile"}, Material.BOW);
	}
	
	public void register(KittDescription desc){
		availableKitts.add(desc);
	}
	/*
	 * Important !!! Add ALL Kitts here
	 */
	public Kitt create(Player owner, KittDescription desc){ 
		if(hasKitt(owner)) removePlayer(owner);
		switch (desc.getKittType()) {
		case Teleporter:
			Teleporter teleporter = new Teleporter(owner, desc);
			setPlayer(owner, teleporter);
			return teleporter;
		case Sch�tze:
			Sch�tze sch�tze = new Sch�tze(owner, desc);
			setPlayer(owner, sch�tze);
			return sch�tze;
		default:
			return null;
		}
	}
	
	public ArrayList<KittDescription> getKittDescriptions() {
		return availableKitts;
	}
	
	public KittDescription getKitt(KittType type){
		for(KittDescription desc : getKittDescriptions()){
			if(desc.getKittType().equals(type)) return desc;
		}
		return null;
	}

	public Kitt getKitt(Player player){
		return activeKitts.get(player);
	}
	
	public boolean hasKitt(Player player){
		return activeKitts.containsKey(player);
	}
	
	public void setPlayer(Player player, Kitt kitt) {
		if(activeKitts.containsKey(player)) removePlayer(player);
		activeKitts.put(player, kitt);
	}
	
	public void removePlayer(Player player){
		getKitt(player).stopSchedulers();
		activeKitts.remove(player);
	}
	
	public ArrayList<Player> getPlayers(Kitt kitt){
		ArrayList<Player> players = new ArrayList<>();
		for(Entry<Player, Kitt> ent : activeKitts.entrySet()){
			if(ent.getValue().getDescription().getKittType().equals(kitt.getDescription().getKittType())) players.add(ent.getKey());
		}
		return players;
	}

	public void onFall(Player p) {
		p.getInventory().clear();
		p.setFallDistance(0);
		p.teleport(p.getLocation().getWorld().getSpawnLocation());
		Core.killstreakManager.reset(p);
	}

}
