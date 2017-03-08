package de.spezipaul.knockit.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.spezipaul.knockit.objects.Enchant;

public class ItemUtils {
	
	public static ItemStack create(Material material, String name, String[] lore, List<Enchant> enchants){
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		if(enchants != null){
			for(Enchant e : enchants){
				meta.addEnchant(e.getEnchantment(), e.getLevel(), e.isIgnore());
			}
		}
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack stick(){
		return create(Material.STICK, "§cKnüppel", new String[]{"§8§m-------------------", "§e§lSchlag sie Runter", "§8§m-------------------"}, Arrays.asList(new Enchant(Enchantment.KNOCKBACK, 2, true)));
	}

}
