package de.spezipaul.knockit.kitts.kitts.specials.kitt3120;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.utils.EntityUtils;

public class WitherBuddy implements Listener {
	
	private ArrayList<WitherSkull> witherSkulls = new ArrayList<>();
	
	private Kitt3120 kitt;
	private Player owner;
	private Wither wither;
	
	private int walkScheduler;
	private int distanceToTeleport = 25;
	private int rangeToTarget = 15;
	
	public WitherBuddy(Kitt3120 kitt) {
		Core.instance.registerEvents(this);
		
		this.kitt = kitt;
		owner = kitt.getOwner();
		
		wither = owner.getLocation().getWorld().spawn(owner.getLocation().add(0, 2, 0), Wither.class);
		wither.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 1, true, false));
		
		walkScheduler = Core.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Core.instance, new Runnable() {
			public void run() {
				try {
					if(wither.getTarget() == null) {
						if(wither.getLocation().distance(owner.getLocation()) > distanceToTeleport) {
							wither.teleport(owner.getLocation().add(0, 2, 0));
						}
						EntityUtils.walk(wither, owner.getLocation(), 1.0F);
						
						List<Entity> nearbyEntities = wither.getNearbyEntities(rangeToTarget, rangeToTarget, rangeToTarget);
						ArrayList<Entity> possibleTargets = new ArrayList<>();
						for(Entity ent : nearbyEntities){
							if(ent instanceof Player && !ent.equals(owner)) possibleTargets.add(ent);
						}
						if(possibleTargets.size() > 0){
							LivingEntity le = (LivingEntity) EntityUtils.getNearest(wither.getLocation(), possibleTargets);
							wither.setTarget(le);							
						} else {
							wither.setTarget(null);
						}
					}
				} catch (NoClassDefFoundError e) {
					die(false);
					System.err.println(Core.PLPrefix + "Error: Server does not support this version of KnockIt [Kitt3120KitError]");
					owner.sendMessage(Core.PLPrefix + "Diese Funktion wird nicht unterstützt");
				}
			}
		}, 10L, 10L);
	}

	public void die(boolean extern) {
		if(!extern){
			kitt.killBuddy();
		} else {
			Core.instance.getServer().getScheduler().cancelTask(walkScheduler);
			for(WitherSkull skull : witherSkulls){
				skull.remove();
			}
			wither.remove();
		}
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e){
		if(kitt.isEnabled() && e.getEntity() instanceof WitherSkull && e.getEntity().getShooter().equals(wither)) {
			if(wither.getTarget() == null || wither.getTarget().equals(owner)) {
				e.setCancelled(true);
				return;
			}
			witherSkulls.add((WitherSkull) e.getEntity());			
			Core.instance.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
				public void run() {
					if(!e.getEntity().isDead()) e.getEntity().remove();
					if(witherSkulls.contains(e.getEntity())) witherSkulls.remove(e.getEntity());
				}
			}, 20*20L);
		}
	}
	
	@EventHandler
	public void onWitherSkullExplode(EntityExplodeEvent e){
		if(kitt.isEnabled() && e.getEntity() instanceof WitherSkull && witherSkulls.contains(e.getEntity())) {
			WitherSkull skull = (WitherSkull) e.getEntity();
			skull.setYield(0.0F);
			e.blockList().clear();
		}
	}
	
	@EventHandler
	public void onDamageBySkullOrWither(EntityDamageByEntityEvent e){
		if(!kitt.isEnabled()) return;
		Entity damager = e.getDamager();
		if(damager instanceof Projectile) damager = (Entity) ((Projectile)e.getDamager()).getShooter();
		if(damager.equals(wither)) e.setDamage(0);
	}
	
	@EventHandler
	public void onTargetLivingEntity(EntityTargetLivingEntityEvent e){
		if(!kitt.isEnabled() || !e.getEntity().equals(wither)) return;
		if(e.getTarget() != null && (!(e.getTarget() instanceof Player) || e.getTarget().equals(owner))) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void blockBreakFix(EntityChangeBlockEvent e) {
		if(kitt.isEnabled() && (witherSkulls.contains(e.getEntity()) || e.getEntity().equals(wither))) e.setCancelled(true);
	}

}
