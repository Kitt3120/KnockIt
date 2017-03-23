package de.spezipaul.knockit.kitts.kitts.creeper;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.utils.ItemUtils;

public class Creeper extends Kitt implements Listener {
	
	private ItemStack stick;
	private ItemStack creeperEggs;
	
	private int creeperSpawnEggCooldown = 30;
	private ArrayList<CustomCreeper> creepers = new ArrayList<>();

	public Creeper(Player owner, KittDescription desc) {
		super(owner, desc);
		Core.instance.registerEvents(this);
	}

	@Override
	public void setupItems() {
		stick = ItemUtils.stick();
		creeperEggs = ItemUtils.create(Material.TNT, 2, "§cCreeper", new String[]{"§cSpawnt einen Creeper, der deine gegner angreift"}, null);
	}
	
	public void removeCreeper(CustomCreeper creeper){
		creepers.remove(creeper);
	}

	@Override
	public void stop() {
		for(CustomCreeper creeper : creepers){
			creeper.die(false);
		}
		creepers.clear();
	}

	@Override
	protected HashMap<Integer, ItemStack> getItemHashmap() {
		HashMap<Integer, ItemStack> items = new HashMap<>();
		items.put(0, stick);
		items.put(1, creeperEggs);
		return items;
	}
	
	public void placeCreeper(Location loc){
		creepers.add(new CustomCreeper(getOwner(), loc, this));
	}
	
	@EventHandler
	public void onPlaceCreeper(PlayerInteractEvent e){
		if(!isEnabled()) return;
		if(e.getPlayer().equals(getOwner()) && e.getClickedBlock() != null && e.getItem() != null && e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem().isSimilar(creeperEggs)) {
			e.setCancelled(true);
			ItemStack eggs = getOwner().getInventory().getItemInHand();
			int amount = eggs.getAmount() - 1;
			if(amount <= 0) {
				e.getPlayer().getInventory().remove(eggs);
			} else {
				eggs.setAmount(amount);
				e.getPlayer().setItemInHand(eggs);
			}
			placeCreeper(e.getClickedBlock().getLocation().add(0, 1, 0));
			Core.instance.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
				public void run() {
					if(!isEnabled() || e.getPlayer().getInventory().contains(creeperEggs)) return;
					ItemStack creeperEgg = ItemUtils.create(Material.TNT, 1, "§cCreeper", new String[]{"§cSpawnt einen Creeper, der deine gegner angreift"}, null);
					e.getPlayer().getInventory().addItem(creeperEgg);
				}
			}, creeperSpawnEggCooldown*20L);
		}
	}

	public ArrayList<CustomCreeper> getCustomCreepers() {
		return creepers;	
	}

}
