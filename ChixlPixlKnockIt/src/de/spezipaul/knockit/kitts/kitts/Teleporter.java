package de.spezipaul.knockit.kitts.kitts;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.utils.ItemUtils;

public class Teleporter extends Kitt implements Listener {

	public Teleporter() {
		super("Teleporter", generateGUIItem(), generateItems());
		Core.instance.registerEvents(this);
	}
	
	public static ItemStack generateGUIItem() {
		return ItemUtils.create(Material.BLAZE_ROD, "§aTeleporter", new String[]{"§cKann sich einmal nach oben teleportieren"}, null);
	}
	
	public static HashMap<Integer, ItemStack> generateItems() {
		HashMap<Integer, ItemStack> items = new HashMap<>();
		items.put(0, ItemUtils.stick());
		items.put(1, ItemUtils.create(Material.BLAZE_ROD, "§cTeleporter", new String[]{"§cTeleportiert dich hoch"}, null));
		return items;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(Core.kittsManager.getPlayers(this).contains(e.getPlayer())){
			Player p = e.getPlayer();
			if(p.getLocation().getY() < 20){
				Core.kittsManager.onFall(p);
			}
		}
	}

}
