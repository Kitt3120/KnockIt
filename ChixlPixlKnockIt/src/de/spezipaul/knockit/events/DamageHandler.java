package de.spezipaul.knockit.events;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import de.spezipaul.knockit.Core;

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
		if(e.getEntity() instanceof Player && !e.getDamager().equals(e.getEntity())) {
			if(e.getDamager() instanceof Player){
				Core.kittsManager.setLastDamager((Player) e.getEntity(), (Player) e.getDamager());
				e.setDamage(0);				
			}
			if(e.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) e.getDamager();
				if(projectile.getShooter() instanceof Player) {
					Core.kittsManager.setLastDamager((Player) e.getEntity(), (Player) projectile.getShooter());
					e.setDamage(0);
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		if(e.getEntity().getKiller() != null) {
			if(e.getEntity().getKiller() instanceof Player) {
				e.getDrops().clear();
				Core.kittsManager.onDeath(e.getEntity(), e.getEntity().getKiller());				
			}
			if(e.getEntity().getKiller() instanceof Arrow){
				Arrow arrow = (Arrow) e.getEntity().getKiller();
				if(arrow.getShooter() instanceof Player){
					Core.kittsManager.onDeath(e.getEntity(), (Player) arrow.getShooter());	
				}
			}
			return;
		}
		e.getDrops().clear();
		Core.kittsManager.onDeath(e.getEntity());
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		Core.kittsManager.resetKitt(e.getPlayer());
	}

}
