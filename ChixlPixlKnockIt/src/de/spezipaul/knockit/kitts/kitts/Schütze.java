package de.spezipaul.knockit.kitts.kitts;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;

public class Schütze extends Kitt {
	
	public Schütze(Player owner, KittDescription desc) {
		super(owner, desc);
	}

	@Override
	public void setupItems() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopSchedulers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected HashMap<Integer, ItemStack> getItemHashmap() {
		// TODO Auto-generated method stub
		return null;
	}

}
