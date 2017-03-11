package de.spezipaul.knockit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.spezipaul.knockit.Core;

public class JoinAndQuitMessage implements Listener {
	
	public JoinAndQuitMessage() {
		Core.instance.registerEvents(this);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		String message = Core.joinMessage;
		if(message.length() > 0){
			message = message.replace("%NAME", e.getPlayer().getName()).replace("&", "§");
			e.setJoinMessage(message);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		String message = Core.leaveMessage;
		if(message.length() > 0){
			message = message.replace("%NAME", e.getPlayer().getName()).replace("&", "§");
			e.setQuitMessage(message);
		}
		Core.killstreakManager.reset(e.getPlayer());
		Core.kittsManager.removeLastDamager(e.getPlayer());
		Core.kittsManager.removePlayer(e.getPlayer());
	}

}
