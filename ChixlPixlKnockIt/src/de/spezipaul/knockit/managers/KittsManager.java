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
import de.spezipaul.knockit.kitts.kitts.chemiker.Chemiker;
import de.spezipaul.knockit.kitts.kitts.creeper.Creeper;
import de.spezipaul.knockit.kitts.kitts.schütze.Schütze;
import de.spezipaul.knockit.kitts.kitts.sniper.Sniper;
import de.spezipaul.knockit.kitts.kitts.specials.kitt3120.Kitt3120;
import de.spezipaul.knockit.kitts.kitts.teleporter.Teleporter;

public class KittsManager {
	
	private ArrayList<KittDescription> availableKitts = new ArrayList<>();
	private HashMap<Player, Kitt> activeKitts = new HashMap<>();
	
	private HashMap<Player, Player> lastDamagers = new HashMap<>();
	
	public KittsManager() {
		availableKitts.add(new KittDescription(KittType.Teleporter, "Teleporter", new String[]{"Kann sich zurück teleportieren"}, Material.BLAZE_ROD));
		availableKitts.add(new KittDescription(KittType.Schütze, "Schütze", new String[]{"Munition: 4", "Schießt schwere Pfeile, die einen größeren Rückstoß garantieren"}, Material.BOW));
		availableKitts.add(new KittDescription(KittType.Sniper, "Sniper", new String[]{"Munition: 2", "Schießt sehr leichte Pfeile, die dafür schneller fliegen"}, Material.ARROW));
		availableKitts.add(new KittDescription(KittType.Creeper, "Creeper", new String[]{"Kann verbündete Creeper spawnen"}, Material.TNT));
		availableKitts.add(new KittDescription(KittType.Chemiker, "Chemiker", new String[]{"Kann seine Gegner verlangsamen"}, Material.POTION));
	}
	
	public void register(KittDescription desc){
		availableKitts.add(desc);
	}
	
	
	
	
	
	/*
	 * Important !!! Add ALL Kitts here
	 */
	
	
	public Kitt create(Player owner, KittDescription desc){ 
		if(hasKitt(owner)) removePlayer(owner);
		try {
			switch (desc.getKittType()) {
			case Teleporter:
				Teleporter teleporter = new Teleporter(owner, desc);
				setPlayer(owner, teleporter);
				return teleporter;
			case Schütze:
				Schütze schütze = new Schütze(owner, desc);
				setPlayer(owner, schütze);
				return schütze;
			case Sniper:
				Sniper sniper = new Sniper(owner, desc);
				setPlayer(owner, sniper);
				return sniper;
			case Creeper:
				Creeper creeper = new Creeper(owner, desc);
				setPlayer(owner, creeper);
				return creeper;
			case Chemiker:
				Chemiker chemiker = new Chemiker(owner, desc);
				setPlayer(owner, chemiker);
				return chemiker;
			case Kitt3120:
				Kitt3120 kitt3120 = new Kitt3120(owner, desc);
				setPlayer(owner, kitt3120);
				return kitt3120;
			default:
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace(); //Just in case something happens
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
			getKitt(player).stop();
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

	public HashMap<Player, Kitt> getActiveKitts() {
		return activeKitts;
	}
	
	public void resetKitt(Player p){		
		try {
			Kitt playerKitt = Core.kittsManager.getKitt(p);
			playerKitt.stop();
			playerKitt.setEnabled(false);
			KittDescription desc = playerKitt.getDescription();
			playerKitt = Core.kittsManager.create(p, desc);
		} catch (NoKittException e1) {}
	}

	public void onFall(Player p) {
		p.setFallDistance(0);
		p.teleport(p.getLocation().getWorld().getSpawnLocation());
		p.setHealth(p.getMaxHealth());
		p.setFireTicks(0);
		try {
			Player lastDamager = getLastDamager(p);
			Core.killstreakManager.add(lastDamager, 1);
			removeLastDamager(p);
		} catch (NoLastDamagerException e) {}
		Core.killstreakManager.reset(p);
		resetKitt(p);
		
	}
	
	public void onDeath(Player p){
		Core.killstreakManager.reset(p);
		try {
			Core.kittsManager.getKitt(p).stop();
			Core.kittsManager.getKitt(p).setEnabled(false);
		} catch (NoKittException e) {}
	}
	
	public void onDeath(Player p, Player killer){
		Core.killstreakManager.add(killer, 1);
		Core.killstreakManager.reset(p);
		try {
			Core.kittsManager.getKitt(p).stop();
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
