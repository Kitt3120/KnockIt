package de.spezipaul.knockit.kitts.kitts.sniper;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.utils.ItemUtils;
import de.spezipaul.knockit.utils.ParticleUtils;

public class Sniper extends Kitt implements Listener {
	
	private ItemStack defaultStick;
	private ItemStack bow;
	private ItemStack arrow;
	
	private int arrowBackSeconds = 15;
	private ArrayList<Arrow> flyingArrows = new ArrayList<>();
	private int arrowParticleScheduler;

	public Sniper(Player owner, KittDescription desc) {
		super(owner, desc);
		Core.instance.registerEvents(this);
		arrowParticleScheduler = Core.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Core.instance, new Runnable() {
			public void run() {
				if(!isEnabled()) return;
				for(Arrow arrow : flyingArrows){
					ParticleUtils.particleEffect(arrow.getLocation(), Effect.COLOURED_DUST, 0, 0, 0, 0, 0);
				}
			}
		}, 1L, 1L);
	}

	@Override
	public void setupItems() {
		defaultStick = ItemUtils.stick();
		bow = ItemUtils.create(Material.BOW, 1, "§cBogen", new String[]{"§cSpeed-Bogen"}, null);
		arrow = ItemUtils.create(Material.ARROW, 2, "§cPfeil", new String[]{"§cFliegt schneller als der Schall"}, null);
	}

	@Override
	public void stop() {
		Core.instance.getServer().getScheduler().cancelTask(arrowParticleScheduler);
	}

	@Override
	protected HashMap<Integer, ItemStack> getItemHashmap() {
		HashMap<Integer, ItemStack> items = new HashMap<>();
		items.put(0, defaultStick);
		items.put(1, bow);
		items.put(2, arrow);
		return items;
	}
	
	@EventHandler
	public void onArrow(ProjectileLaunchEvent e) {
		if(!isEnabled()) return;
		if(e.getEntity().getShooter().equals(getOwner()) && e.getEntity() instanceof Arrow) {
			Core.instance.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
				public void run() {
					if(!isEnabled() || getOwner().getInventory().contains(arrow)) return;
					getOwner().getInventory().addItem(ItemUtils.create(Material.ARROW, 1, "§cPfeil", new String[]{"§cFliegt schneller als der Schall"}, null));
				}
			}, arrowBackSeconds*20L);
			((Arrow)e.getEntity()).setVelocity(e.getEntity().getVelocity().multiply(1.5));
			((Arrow)e.getEntity()).setKnockbackStrength(0);
			flyingArrows.add((Arrow) e.getEntity());
		}
	}
	
	@EventHandler
	public void onArrowHit(ProjectileHitEvent e){
		if(!isEnabled()) return;
		if(flyingArrows.contains(e.getEntity())) {
			flyingArrows.remove(e.getEntity());
			ParticleUtils.particleEffect(e.getEntity().getLocation(), Effect.COLOURED_DUST, 1, 1, 1, 1, 5);
			Core.instance.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
				public void run() {
					if(!e.getEntity().isDead()){
						ParticleUtils.particleEffect(e.getEntity().getLocation(), Effect.COLOURED_DUST, 0, 0, 0, 0, 15);
						e.getEntity().remove();						
					}
				}
			}, 2*20L);
		}
	}

}
