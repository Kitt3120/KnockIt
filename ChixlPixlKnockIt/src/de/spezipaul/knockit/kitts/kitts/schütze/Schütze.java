package de.spezipaul.knockit.kitts.kitts.schütze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
import de.spezipaul.knockit.objects.Enchant;
import de.spezipaul.knockit.utils.ItemUtils;
import de.spezipaul.knockit.utils.ParticleUtils;

public class Schütze extends Kitt implements Listener {
	
	private ItemStack defaultStick;
	private ItemStack bow;
	private ItemStack arrow;
	
	private int arrowBackSeconds = 15;
	private ArrayList<Arrow> flyingArrows = new ArrayList<>();
	private int arrowParticleScheduler;

	public Schütze(Player owner, KittDescription desc) {
		super(owner, desc);
		Core.instance.registerEvents(this);
		arrowParticleScheduler = Core.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Core.instance, new Runnable() {
			public void run() {
				if(!isEnabled()) return;
				for(Arrow arrow : flyingArrows){
					ParticleUtils.particleEffect(arrow.getLocation(), Effect.CLOUD, 0, 0, 0, 0, 0, 100);
					ParticleUtils.particleEffect(arrow.getLocation(), Effect.CLOUD, 0, 0, 0, 0, 0, 100);
				}
			}
		}, 1L, 1L);
	}

	@Override
	public void setupItems() {
		defaultStick = ItemUtils.stick();
		bow = ItemUtils.create(Material.BOW, 1, "§cBogen", new String[]{"§cSupercooler Bogen"}, Arrays.asList(new Enchant(Enchantment.ARROW_KNOCKBACK, 2, true)));
		arrow = ItemUtils.create(Material.ARROW, 4, "§cPfeil", new String[]{"§cKnallt nicht nur deine Alte weg"}, null);
	}

	@Override
	public void stopSchedulers() {
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
					getOwner().getInventory().addItem(ItemUtils.create(Material.ARROW, 1, "§cPfeil", new String[]{"§cKnallt nicht nur deine Alte weg"}, null));
				}
			}, arrowBackSeconds*20L);
			flyingArrows.add((Arrow) e.getEntity());
		}
	}
	
	@EventHandler
	public void onArrowHit(ProjectileHitEvent e){
		if(!isEnabled()) return;
		if(flyingArrows.contains(e.getEntity())) flyingArrows.remove(e.getEntity());
	}

}
