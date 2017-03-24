package de.spezipaul.knockit.gui;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.kitts.kitts.specials.SpecialPlayers;

public class ClassChooser implements Listener {
	
	private Player owner;
	private Inventory inv;
	private boolean hasSelected = false;
	private HashMap<ItemStack, KittDescription> descriptions = new HashMap<>();
	
	public ClassChooser(Player p) {
		this.owner = p;
		int x = Core.kittsManager.getKittDescriptions().size();
		x = ((x + 8) / 9) * 9;
		this.inv = Core.instance.getServer().createInventory(null, x, "§aWähle eine Klasse");
		for(KittDescription desc : Core.kittsManager.getKittDescriptions()){
			ItemStack item = desc.getGUIItem();
			descriptions.put(item, desc);
			inv.addItem(item);
		}
		
		SpecialPlayers.addSpecial(p.getName(), descriptions, inv);
		
		p.openInventory(inv);
		p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1, 1);
		Core.instance.registerEvents(this);
	}

	@EventHandler
	public void onSelectKit(InventoryClickEvent e){
		if(e.getInventory().equals(inv) && e.getWhoClicked().equals(owner) && e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR) && !hasSelected){
			e.setCancelled(true);
			hasSelected = true;
			createKitt(descriptions.get(e.getCurrentItem()));
			owner.closeInventory();
			owner.playSound(owner.getLocation(), Sound.LEVEL_UP, 1, 10);
		}
	}
	
	public void createKitt(KittDescription desc){
		owner.getInventory().clear();
		Core.kittsManager.create(owner, desc);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(e.getPlayer().equals(owner) && (e.getPlayer().getOpenInventory() == null || !e.getPlayer().getOpenInventory().equals(inv)) && !hasSelected) {
			owner.openInventory(inv);
		}
	}

}
