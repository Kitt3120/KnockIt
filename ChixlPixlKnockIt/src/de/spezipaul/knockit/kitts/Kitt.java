package de.spezipaul.knockit.kitts;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Kitt implements Listener {
	
	private String name;
	private ItemStack GUIItem;
	private HashMap<Integer, ItemStack> items = new HashMap<>();
	
	public Kitt(String name, ItemStack GUIItem, HashMap<Integer, ItemStack> items) {
		this.name = name;
		this.GUIItem = GUIItem;
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<Integer, ItemStack> getItems() {
		return items;
	}

	public void setItems(HashMap<Integer, ItemStack> items) {
		this.items = items;
	}
	
	public ItemStack getGUIItem() {
		return GUIItem;
	}
	
	public void setGUIItem(ItemStack gUIItem) {
		GUIItem = gUIItem;
	}

}
