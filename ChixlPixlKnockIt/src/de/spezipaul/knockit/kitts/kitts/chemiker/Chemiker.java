package de.spezipaul.knockit.kitts.kitts.chemiker;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.utils.ItemUtils;

public class Chemiker extends Kitt implements Listener {
	
	private ItemStack stick;
	private ItemStack slow;
	
	private int potionDurationInSeconds = 5;
	private int potionStrengthAmplifier = 1;
	private int potionBackSeconds = 12;

	public Chemiker(Player owner, KittDescription desc) {
		super(owner, desc);
		Core.instance.registerEvents(this);
	}

	@Override
	public void setupItems() {
		stick = ItemUtils.stick();
		slow = new ItemStack(Material.POTION);
		
		PotionMeta meta = (PotionMeta) slow.getItemMeta();
		meta.setMainEffect(PotionEffectType.SLOW);
		meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, potionDurationInSeconds*20, potionStrengthAmplifier, false, true), true);
		slow.setItemMeta(meta);
		
		Potion potion = new Potion(PotionType.SLOWNESS, potionStrengthAmplifier);
		potion.setSplash(true);
		potion.apply(slow);
	}

	@Override
	public void stop() { 
		//Nothing to stop 		
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
		if(isEnabled() && e.getPlayer().equals(getOwner()) && e.getItem().isSimilar(slow)) {
			ItemStack slowOne = slow.clone();
			slowOne.setAmount(1);
			
			Core.instance.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
				public void run() {
					if(!isEnabled() || e.getPlayer().getInventory().contains(slow)) return;
					e.getPlayer().getInventory().addItem(slowOne);
				}
			}, potionBackSeconds*20L);
		}
	}

}
