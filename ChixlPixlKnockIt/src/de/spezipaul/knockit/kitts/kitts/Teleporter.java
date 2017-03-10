package de.spezipaul.knockit.kitts.kitts;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;

public class Teleporter extends Kitt implements Listener {
	
	public Teleporter(Player owner, KittDescription desc) {
		super(owner, desc);
		Core.instance.registerEvents(this);
	}

	@Override
	public void setupItems() {
		
	}
	
	@Override
	public void setupSchedulers() {
		
	}

	@Override
	public void startSchedulers() {
		
	}

	@Override
	public void stopSchedulers() {
		
	}

	@Override
	protected HashMap<Integer, ItemStack> getItemHashmap() {
		// TODO Auto-generated method stub
		return null;
	}

}
