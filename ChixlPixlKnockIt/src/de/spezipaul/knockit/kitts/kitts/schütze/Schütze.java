package de.spezipaul.knockit.kitts.kitts.schütze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.objects.Enchant;
import de.spezipaul.knockit.utils.ItemUtils;

public class Schütze extends Kitt implements Listener {
	
	private ItemStack defaultStick;
	private ItemStack bow;
	private ItemStack arrow;
	
	private ArrayList<Integer> arrowBackTasks = new ArrayList<>();
	private int arrowBackSeconds = 15;

	public Schütze(Player owner, KittDescription desc) {
		super(owner, desc);
		Core.instance.registerEvents(this);
	}

	@Override
	public void setupItems() {
		defaultStick = ItemUtils.stick();
		bow = ItemUtils.create(Material.BOW, 1, "§cBogen", new String[]{"§cSupercooler Bogen"}, Arrays.asList(new Enchant(Enchantment.ARROW_KNOCKBACK, 1, true)));
		arrow = ItemUtils.create(Material.ARROW, 4, "§cPfeil", new String[]{"§cKnallt nicht nur deine Alte weg"}, null);
	}

	@Override
	public void stopSchedulers() {
		for(int id : arrowBackTasks){
			Core.instance.getServer().getScheduler().cancelTask(id);
		}
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
		if(e.getEntity().getShooter().equals(getOwner()) && e.getEntity() instanceof Arrow) {
			if(arrowBackTasks.size() < 4) {
				int id = Core.instance.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
					public void run() {
						getOwner().getInventory().addItem(ItemUtils.create(Material.ARROW, 1, "§cPfeil", new String[]{"§cKnallt nicht nur deine Alte weg"}, null));
					}
				}, arrowBackSeconds*20L);
				Core.instance.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
					
					@Override
					public void run() {
						arrowBackTasks.remove(id);
					}
				}, arrowBackSeconds*20L);
			}
		}
	}

}
