package de.spezipaul.knockit.kitts;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Kitt {
	
	private Player owner;
	private KittDescription description;
	private HashMap<Integer, ItemStack> items;
	
	public Kitt(Player owner, KittDescription desc) {
		this.owner = owner;
		this.description = desc;
		
		setupItems();
		setupSchedulers();
		startSchedulers();
	}

	public abstract void setupItems();
	public abstract void setupSchedulers();
	public abstract void startSchedulers();
	public abstract void stopSchedulers();
	protected abstract HashMap<Integer, ItemStack> getItemHashmap();
	
	public HashMap<Integer, ItemStack> getItems() {
		if(items == null) items = getItemHashmap();
		return items;
	}
	
	public void setItems(HashMap<Integer, ItemStack> items) {
		this.items = items;
	}

	public Player getOwner() {
		return owner;
	}
	
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public KittDescription getDescription() {
		return description;
	}
	
	public void setDescription(KittDescription description) {
		this.description = description;
	}

}
