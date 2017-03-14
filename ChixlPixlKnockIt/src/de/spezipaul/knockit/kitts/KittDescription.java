package de.spezipaul.knockit.kitts;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.utils.ItemUtils;

public class KittDescription implements Listener {

	private KittType kittType;
	private String name;
	private String[] description;
	private ItemStack GUIItem;

	public KittDescription(KittType type, String name, String[] description, Material GUIItemMaterial) {
		this.kittType = type;
		this.name = name;
		this.description = description;
		this.GUIItem = ItemUtils.create(GUIItemMaterial, 1, "§a" + name, description, null);
	}
	
	public KittType getKittType() {
		return kittType;
	}
	
	public String getName() {
		return name;
	}

	public String[] getDescription() {
		return description;
	}

	public ItemStack getGUIItem() {
		return GUIItem;
	}

}
