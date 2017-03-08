package de.spezipaul.knockit.objects;

import org.bukkit.enchantments.Enchantment;

public class Enchant {
	
	private Enchantment enchantment;
	private int level;
	private boolean ignore;
	
	public Enchant(Enchantment enchant, int level, boolean ignore) {
		this.enchantment = enchant;
		this.level = level;
		this.ignore = ignore;
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}

	public void setEnchantment(Enchantment enchantment) {
		this.enchantment = enchantment;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}	

}
