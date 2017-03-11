package de.spezipaul.knockit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.spezipaul.knockit.Core;

public class FallDownEvent implements Listener {
	
	public FallDownEvent() {
		Core.instance.registerEvents(this);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(e.getPlayer().getLocation().getY() <= 20) Core.kittsManager.onFall(e.getPlayer());
	}

}
