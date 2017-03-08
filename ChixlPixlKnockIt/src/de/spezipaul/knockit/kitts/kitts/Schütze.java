package de.spezipaul.knockit.kitts.kitts;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.objects.Enchant;
import de.spezipaul.knockit.utils.ItemUtils;

public class Schuetze extends Kitt implements Listener {

	public Schuetze() {
		super("Schütze", generateGUIItem(), generateItems());
		Core.instance.registerEvents(this);
	}
	
	public static ItemStack generateGUIItem() {
		return ItemUtils.create(Material.BOW, "§aSchütze", new String[]{"§cMan hat 4 Pfeile und einen Schlag 2 Bogen"}, null); //TODO Effect
	}
	
	public static HashMap<Integer, ItemStack> generateItems() {
		HashMap<Integer, ItemStack> items = new HashMap<>();
		items.put(0, ItemUtils.stick());
		items.put(1, ItemUtils.create(Material.BOW, "§cBogen", new String[]{"§cSchieße dir damit den weg an die Spitze frei!"},Arrays.asList(new Enchant(Enchantment.ARROW_KNOCKBACK, 2, true))));
		/*items.put(2, ItemUtils.create(Material.ARROW, "§cPfeil", new String[]{"§cSchieße dir damit den weg an die Spitze frei!"}, null));*/
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
