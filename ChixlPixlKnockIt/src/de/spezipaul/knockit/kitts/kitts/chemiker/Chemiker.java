package de.spezipaul.knockit.kitts.kitts.chemiker;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.utils.ItemUtils;

public class Chemiker extends Kitt implements Listener {
	
	private ItemStack stick;
	private ItemStack slow;
	
	private int potionStrengthAmplifier = 1;
	private int potionBackSeconds = 12;
	
	private int speedScheduler;

	public Chemiker(Player owner, KittDescription desc) {
		super(owner, desc);
		Core.instance.registerEvents(this);
		
		speedScheduler = Core.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Core.instance, new Runnable() {
			public void run() {
				owner.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5, 0, true, false), true);
			}
		}, 1L, 1L);
	}

	@Override
	public void setupItems() {
		stick = ItemUtils.stick();
		
		slow = new ItemStack(Material.POTION, 1, (short)16394);		
		PotionMeta meta = (PotionMeta) slow.getItemMeta();
		meta.setMainEffect(PotionEffectType.SLOW);
		meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 200, potionStrengthAmplifier), true);
		meta.setDisplayName("§cSlow");
		meta.setLore(Arrays.asList("§cVerlangsamt deine Gegner"));
		slow.setItemMeta(meta);
	}

	@Override
	public void stop() {
		Core.instance.getServer().getScheduler().cancelTask(speedScheduler);
	}

	@Override
	protected HashMap<Integer, ItemStack> getItemHashmap() {
		HashMap<Integer, ItemStack> items = new HashMap<>();
		items.put(0, stick);
		items.put(1, slow);
		return items;
	}
	
	@EventHandler
	public void onThrowPotion(PlayerInteractEvent e){
		if(isEnabled() && e.getPlayer().equals(getOwner()) && e.getItem() != null && e.getItem().isSimilar(slow)) {
			
			Core.instance.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
				public void run() {
					if(!isEnabled() || e.getPlayer().getInventory().contains(slow)) return;
					e.getPlayer().getInventory().addItem(slow);
				}
			}, potionBackSeconds*20L);
		}
	}

}
