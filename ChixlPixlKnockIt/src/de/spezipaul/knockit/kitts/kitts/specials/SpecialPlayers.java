package de.spezipaul.knockit.kitts.kitts.specials;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.kitts.KittType;

public class SpecialPlayers {

	public static void addSpecial(String name, HashMap<ItemStack, KittDescription> descriptions, Inventory guiInventory) {
		switch (name.toLowerCase()) {
		case "kitt3120":
			KittDescription desc = new KittDescription(KittType.Kitt3120, "Kitt3120", new String[]{"Special"}, Material.BARRIER);
			descriptions.put(desc.getGUIItem(), desc);
			guiInventory.addItem(desc.getGUIItem());
			break;
		case "spezipaul":
			//Special Kitt gefällig ? :)
			break;
		default:
			break;
		}
	}

}
