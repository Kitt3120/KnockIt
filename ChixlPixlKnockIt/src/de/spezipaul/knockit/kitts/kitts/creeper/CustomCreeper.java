package de.spezipaul.knockit.kitts.kitts.creeper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.utils.EntityUtils;

public class CustomCreeper implements Listener {
	
	private Player owner;
	private Location spawnLocation;
	private Creeper creeperKitt;
	private org.bukkit.entity.Creeper creeper;
	private int targetScheduler;
	
	private float speed = 1.5F;
	private float speedTarget = 1.6F;
	private int rangeToTarget = 12;
	private int followRange = 3;
	private int followTeleport = 12;
	
	public CustomCreeper(Player owner, Location loc, Creeper creeperKitt) {
		Core.instance.registerEvents(this);
		this.owner = owner;
		this.spawnLocation = loc;
		this.creeperKitt = creeperKitt;
		creeper = loc.getWorld().spawn(loc, org.bukkit.entity.Creeper.class);
		creeper.setCollidable(false);
		targetScheduler = Core.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Core.instance, new Runnable() {
			public void run() {
				try {
					if(getCreeper().getTarget() == null || getCreeper().getLocation().distance(getCreeper().getTarget().getLocation()) > rangeToTarget) {
						if(getCreeper().getLocation().distance(getOwner().getLocation()) > followRange) {
							EntityUtils.walk(getCreeper(), getOwner().getLocation(), speed);
							if(getCreeper().getLocation().distance(getOwner().getLocation()) > followTeleport && getCreeper().getFallDistance() < 5) {
								getCreeper().setFallDistance(0);
								getCreeper().teleport(getOwner());
							}
						}
						List<Entity> nearbyEntities = getCreeper().getNearbyEntities(12, 12, 12);
						ArrayList<Entity> possibleTargets = new ArrayList<>();
						for(Entity ent : nearbyEntities){
							if(ent instanceof Player && !ent.equals(getOwner())) possibleTargets.add(ent);
						}
						if(possibleTargets.size() > 0){
							LivingEntity le = (LivingEntity) EntityUtils.getNearest(getCreeper().getLocation(), possibleTargets);
							getCreeper().setTarget(le);							
						} else {
							getCreeper().setTarget(null);
						}
					} else {
						EntityUtils.walk(getCreeper(), getCreeper().getTarget().getLocation(), speedTarget);
					}
				} catch (NoClassDefFoundError e) {
					die(true);
					System.err.println(Core.PLPrefix + "Error: Server does not support this version of KnockIt [CreeperKitError]");
				}
			}
		}, 10L, 10L);
	}
	
	public void die(boolean removeFromList){
		Core.instance.getServer().getScheduler().cancelTask(targetScheduler);
		creeper.remove();
		if(removeFromList) creeperKitt.removeCreeper(this);
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public Location getSpawnLocation() {
		return spawnLocation;
	}
	
	public Creeper getCreeperKitt() {
		return creeperKitt;
	}
	
	public org.bukkit.entity.Creeper getCreeper() {
		return creeper;
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(!getCreeperKitt().isEnabled()) return;
		if(e.getDamager().equals(getCreeper())) {
			if(e.getEntity().equals(getOwner())) {
				e.setCancelled(true);
			} else {
				e.setDamage(0);				
			}
		}
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e){
		if(!getCreeperKitt().isEnabled()) return;
		if(e.getEntity().equals(getCreeper())){
			e.setCancelled(true);
			die(true);
		}
	}
	
	@EventHandler
	public void onTarget(EntityTargetEvent e){
		if(!getCreeperKitt().isEnabled()) return;
		if(e.getEntity().equals(getCreeper())) {
			if(!(e.getTarget() instanceof Player) || e.getTarget().equals(getOwner())) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onTargetLivingEntity(EntityTargetLivingEntityEvent e){
		if(!getCreeperKitt().isEnabled()) return;
		if(e.getTarget() != null && !(e.getTarget() instanceof Player) || e.getEntity().equals(getCreeper())) {
			if(e.getTarget().equals(getOwner())) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onOwnerGetsDamage(EntityDamageByEntityEvent e){
		if(!getCreeperKitt().isEnabled()) return;
		if(e.getEntity().equals(getOwner()) && e.getDamager() instanceof Player) {
			getCreeper().setTarget((LivingEntity) e.getDamager());
		}
	}

}
