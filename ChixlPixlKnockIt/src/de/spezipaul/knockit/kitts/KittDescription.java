package de.spezipaul.knockit.kitts;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KittDescription implements Listener {

	private KittType kittType;
	private String name;
	private String[] description;
	private ItemStack GUIItem;
	
	public KittDescription(KittType type, String name, String[] description, Material GUIItemMaterial) {
		this.kittType = type;
		this.name = name;
		this.description = description;
		this.GUIItem = new ItemStack(GUIItemMaterial);
		ItemMeta meta = this.GUIItem.getItemMeta();
		meta.setDisplayName("§a" + name);
		if(description != null){
			ArrayList<String> lore = new ArrayList<>();
			for(String line : description){
				lore.add("§c" + line);
			}
			meta.setLore(lore);
		}
		this.GUIItem.setItemMeta(meta);
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
