package de.spezipaul.knockit.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.exceptions.NoKittDescriptionException;
import de.spezipaul.knockit.exceptions.NoKittException;
import de.spezipaul.knockit.exceptions.NoLastDamagerException;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.kitts.KittType;
import de.spezipaul.knockit.kitts.kitts.schütze.Schütze;
import de.spezipaul.knockit.kitts.kitts.teleporter.Teleporter;

public class KittsManager {
	
	private ArrayList<KittDescription> availableKitts = new ArrayList<>();
	private HashMap<Player, Kitt> activeKitts = new HashMap<>();
	
	private HashMap<Player, Player> lastDamagers = new HashMap<>();
	
	public KittsManager() {
		availableKitts.add(new KittDescription(KittType.Teleporter, "Teleporter", new String[]{"Kann sich zurück teleportieren"}, Material.BLAZE_ROD));
		availableKitts.add(new KittDescription(KittType.Schütze, "Schütze", new String[]{"Einen starken Bogen und 4 Pfeile"}, Material.BOW));
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
		case Schütze:
			Schütze schütze = new Schütze(owner, desc);
			setPlayer(owner, schütze);
			return schütze;
		default:
			return null;
		}
	}
	
	
	
	
	
	public ArrayList<KittDescription> getKittDescriptions() {
		return availableKitts;
	}
	
	public KittDescription getKitt(KittType type) throws NoKittDescriptionException {
		for(KittDescription desc : getKittDescriptions()){
			if(desc.getKittType().equals(type)) return desc;
		}
		throw new NoKittDescriptionException();
	}

	public Kitt getKitt(Player player) throws NoKittException {
		if(!activeKitts.containsKey(player)) throw new NoKittException();
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
		try {
			getKitt(player).stopSchedulers();
			getKitt(player).setEnabled(false);			
		} catch (NoKittException e) {}
		player.getInventory().clear();
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
		p.setFallDistance(0);
		p.teleport(p.getLocation().getWorld().getSpawnLocation());
		p.setHealth(p.getMaxHealth());
		try {
			Player lastDamager = getLastDamager(p);
			Core.killstreakManager.add(lastDamager, 1);
			removeLastDamager(p);
		} catch (NoLastDamagerException e) {}
		Core.killstreakManager.reset(p);
	}
	
	public void onDeath(Player p){
		Core.killstreakManager.reset(p);
		try {
			Core.kittsManager.getKitt(p).stopSchedulers();
			Core.kittsManager.getKitt(p).setEnabled(false);
		} catch (NoKittException e) {}
	}
	
	public void onDeath(Player p, Player killer){
		Core.killstreakManager.add(killer, 1);
		Core.killstreakManager.reset(p);
		try {
			Core.kittsManager.getKitt(p).stopSchedulers();
			Core.kittsManager.getKitt(p).setEnabled(false);
		} catch (NoKittException e) {}
	}
	
	public void setLastDamager(Player target, Player damager){
		lastDamagers.put(target, damager);
	}
	
	public Player getLastDamager(Player target) throws NoLastDamagerException {
		if(!lastDamagers.containsKey(target)) throw new NoLastDamagerException();
		return lastDamagers.get(target);
	}
	
	public void removeLastDamager(Player target){
		lastDamagers.remove(target);
	}

}
