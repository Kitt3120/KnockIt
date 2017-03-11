package de.spezipaul.knockit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.spezipaul.knockit.Core;

public class NoHunger implements Listener {
	
	public NoHunger() {
		Core.instance.registerEvents(this);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(e.getPlayer().getFoodLevel() < 20) e.getPlayer().setFoodLevel(20);
	}

}
