package de.spezipaul.knockit.kitts.kitts.specials.kitt3120;

import java.util.Random;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.utils.ParticleUtils;
import de.spezipaul.knockit.utils.VectorUtils;

public class CustomSkull implements Listener {
	
	private Kitt3120 kitt;
	private Player owner;
	private Location spawnLocation;
	private WitherSkull skull;
	private int locationScheduler;
	private int particleScheduler;
	
	public CustomSkull(Kitt3120 kitt) {
		Core.instance.registerEvents(this);
		
		this.kitt = kitt;
		owner = kitt.getOwner();
		Location loc = owner.getEyeLocation().add(0, 1, 0);
		Vector v = new Vector(1, 0, 1);
		VectorUtils.rotateAroundAxisY(v, owner.getLocation().getYaw() - new Random().nextInt(360));
		loc.add(v.getX(), v.getY(), v.getZ());
		spawnLocation = loc;
		
		skull = loc.getWorld().spawn(loc, WitherSkull.class);
		skull.setYield(0.0F);
		skull.setBounce(false);
		skull.setCharged(false);
		skull.setVelocity(new Vector(0, 0, 0));
		skull.setDirection(new Vector(0, 0, 0));
		skull.setShooter(owner);
		
		locationScheduler = Core.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Core.instance, new Runnable() {
			public void run() {
				Location toTeleport = spawnLocation.clone();
				toTeleport.setYaw(owner.getLocation().getYaw() - 180);
				toTeleport.setPitch(owner.getLocation().getPitch());
				skull.teleport(toTeleport);
			}
		}, 1L, 1L);
		
		particleScheduler = Core.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Core.instance, new Runnable() {
			public void run() {
				ParticleUtils.particleEffect(skull.getLocation(), Effect.FLYING_GLYPH, 0, 0, 0, 0, 0);
			}
		}, 1L, 1L);
	}
	
	public WitherSkull getSkull() {
		return skull;
	}

	public void remove(boolean removeFromList) {
		if(removeFromList) kitt.getSkulls().remove(this);
		if(locationScheduler != 0) Core.instance.getServer().getScheduler().cancelTask(locationScheduler);
		if(particleScheduler != 0) Core.instance.getServer().getScheduler().cancelTask(particleScheduler);
		skull.remove();
	}
	
	public void shoot(){
		Core.instance.getServer().getScheduler().cancelTask(locationScheduler);
		Core.instance.getServer().getScheduler().cancelTask(particleScheduler);
		locationScheduler = 0;
		particleScheduler = 0;
		skull.teleport(spawnLocation);
		Location target = owner.getTargetBlock((Set<Material>) null, 200).getLocation();
		Location skullLoc = skull.getLocation();
		Vector direction = target.toVector().subtract(skullLoc.toVector()).normalize().multiply(0.75);
		skull.setVelocity(direction.multiply(0.1));
		skull.setDirection(direction);
		skull.setBounce(true);
	}
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		if(kitt.isEnabled() && e.getEntity().equals(skull)) {
			e.blockList().clear();
		}
	}

}
