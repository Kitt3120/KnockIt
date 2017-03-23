package de.spezipaul.knockit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import de.spezipaul.knockit.Core;

public class NoItems implements Listener {
	
	public NoItems() {
		Core.instance.registerEvents(this);
	}
	
	@EventHandler
	public void onDropItems(PlayerDropItemEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPickupItem(PlayerPickupItemEvent e){
		e.setCancelled(true);
	}

}
