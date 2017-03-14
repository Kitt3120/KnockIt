package de.spezipaul.knockit.kitts;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Kitt {
	
	private Player owner;
	private KittDescription description;
	private HashMap<Integer, ItemStack> items;
	private boolean enabled = true;
	
	public Kitt(Player owner, KittDescription desc) {
		this.owner = owner;
		this.description = desc;
		
		setupItems();
		setItems();
	}

	public abstract void setupItems();
	public abstract void stop();
	protected abstract HashMap<Integer, ItemStack> getItemHashmap();
	
	public HashMap<Integer, ItemStack> getItems() {
		if(items == null) items = getItemHashmap();
		return items;
	}
	
	public void setItems(){
		for(Entry<Integer, ItemStack> ent : getItems().entrySet()){
			owner.getInventory().setItem(ent.getKey(), ent.getValue());
		}
	}

	public Player getOwner() {
		return owner;
	}
	
	public KittDescription getDescription() {
		return description;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
