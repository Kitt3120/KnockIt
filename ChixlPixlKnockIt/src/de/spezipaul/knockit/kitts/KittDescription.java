package de.spezipaul.knockit.kitts;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class KittDescription implements Listener {
	
	private String name;
	private String[] description;
	private ItemStack GUIItem;
	private HashMap<Integer, ItemStack> items;
	
	public KittDescription(String name, String[] description, Material GUIItemMaterial, HashMap<Integer, ItemStack> items) {
		this.name = name;
		this.description = description;
		this.GUIItem = new ItemStack(GUIItemMaterial);
		ItemMeta meta = this.GUIItem.getItemMeta();
		meta.setDisplayName("§a" + name);
		meta.setLore(Arrays.asList("§c" + description));
		this.GUIItem.setItemMeta(meta);
		this.items = items;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getDescription() {
		return description;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}

	public ItemStack getGUIItem() {
		return GUIItem;
	}

	public void setGUIItem(ItemStack gUIItem) {
		GUIItem = gUIItem;
	}

	public HashMap<Integer, ItemStack> getItems() {
		return items;
	}

	public void setItems(HashMap<Integer, ItemStack> items) {
		this.items = items;
	}

}
