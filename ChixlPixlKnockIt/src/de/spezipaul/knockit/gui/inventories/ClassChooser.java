package de.spezipaul.knockit.gui.inventories;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;
import net.minecraft.server.v1_9_R1.Material;

public class ClassChooser implements Listener {
	
	private Player owner;
	private Inventory inv;
	private boolean hasSelected = false;
	private HashMap<KittDescription, ItemStack> items = new HashMap<>();
	
	public ClassChooser(Player p) {
		Core.instance.registerEvents(this);
		this.owner = p;
		int x = Core.kittsManager.getKitts().size();
		x = ((x + 8) / 9) * 9;
		this.inv = Core.instance.getServer().createInventory(null, x, "§aWähle eine Klasse");
		for(KittDescription desc : Core.kittsManager.getKitts()){
			
		}
		p.openInventory(inv);
		p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
	}
	
	@EventHandler
	public void onSelectKit(InventoryClickEvent e){
		if(e.getInventory().equals(inv) && e.getWhoClicked().equals(owner) && e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR) && !hasSelected){
			e.setCancelled(true);
			setItems(e.getCurrentItem());
			owner.closeInventory();
			owner.playSound(owner.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 10);
			hasSelected = true;
		}
	}
	
	public void setItems(ItemStack item){
		String name = item.getItemMeta().getDisplayName();
		Kitt kitt = Core.kittsManager.getkit
		owner.getInventory().clear();
		for(Entry<Integer, ItemStack> entry : kitt.getItems().entrySet()){
			owner.getInventory().setItem(entry.getKey(), entry.getValue());
		}
		Core.kittsManager.setPlayer(owner, kitt);
	}
	
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent e){
		if(e.getInventory().equals(inv) && e.getPlayer().equals(owner) && !hasSelected){
			owner.openInventory(inv);
		}
	}

}
