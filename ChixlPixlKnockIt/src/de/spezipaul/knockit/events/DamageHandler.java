package de.spezipaul.knockit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.exceptions.NoKittException;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;

public class DamageHandler implements Listener {
	
	public DamageHandler() {
		Core.instance.registerEvents(this);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player && e.getCause().equals(DamageCause.FALL)) e.setCancelled(true);
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player && !e.getDamager().equals(e.getEntity())) {
			Core.kittsManager.setLastDamager((Player) e.getEntity(), (Player) e.getDamager());
			e.setDamage(0);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		if(e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) {
			e.getDrops().clear();
			Core.kittsManager.onDeath(e.getEntity(), e.getEntity().getKiller());
		} else {
			e.getDrops().clear();
			Core.kittsManager.onDeath(e.getEntity());
		}
	}
	
	@EventHandler
	public void onDropItems(PlayerDropItemEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		try {
			Kitt playerKitt = Core.kittsManager.getKitt(e.getPlayer());
			KittDescription desc = playerKitt.getDescription();
			playerKitt = Core.kittsManager.create(e.getPlayer(), desc);
		} catch (NoKittException e1) {}
	}

}
