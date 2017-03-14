package de.spezipaul.knockit.kitts.kitts.teleporter;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.utils.ItemUtils;

public class Teleporter extends Kitt implements Listener {
	
	private ItemStack defaultStick;
	private ItemStack teleporter;
	
	private boolean teleporterSchedulerActive = false;
	private int teleporterScheduler;
	private int teleporterCooldown = 10;
	
	private Location lastGroundLocation = null;
	
	public Teleporter(Player owner, KittDescription desc) {
		super(owner, desc);
		Core.instance.registerEvents(this);
	}

	@Override
	public void setupItems() {
		defaultStick = ItemUtils.stick();
		teleporter = ItemUtils.create(Material.BLAZE_ROD, 1, "§cTeleporter", new String[]{"§cTeleportier dich zurück"}, null);
	}

	@Override
	public void stop() {
		if(teleporterSchedulerActive) Core.instance.getServer().getScheduler().cancelTask(teleporterScheduler);
	}

	@Override
	protected HashMap<Integer, ItemStack> getItemHashmap() {
		HashMap<Integer, ItemStack> items = new HashMap<>();
		items.put(0, defaultStick);
		items.put(1, teleporter);
		return items;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(!isEnabled()) return;
		if(e.getPlayer().equals(getOwner()) && !getOwner().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.AIR)){
			lastGroundLocation = getOwner().getLocation().add(0, 1, 0);
		}
	}
	
	@EventHandler
	public void onInteractItem(PlayerInteractEvent e){
		if(!isEnabled()) return;
		if(e.getPlayer().equals(getOwner()) && e.getItem() != null && e.getItem().isSimilar(teleporter)){
			teleport();
		}
	}
	
	private void teleport(){
		if(lastGroundLocation != null){
			getOwner().getWorld().playSound(getOwner().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
			getOwner().teleport(lastGroundLocation);
			getOwner().setFallDistance(0F);
			getOwner().getWorld().playSound(getOwner().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
			getOwner().getInventory().removeItem(teleporter);
			teleporterSchedulerActive = true;
			teleporterScheduler = Core.instance.getServer().getScheduler().scheduleSyncDelayedTask(Core.instance, new Runnable() {
				public void run() {
					getOwner().getInventory().addItem(teleporter);
					teleporterSchedulerActive = false;
				}
			}, teleporterCooldown*20L);
		}
	}

}
