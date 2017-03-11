package de.spezipaul.knockit.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.gui.inventories.ClassChooser;

public class JoinOpenClassChooser implements Listener{
	
	public JoinOpenClassChooser() {
		Core.instance.registerEvents(this);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		new ClassChooser(e.getPlayer());
	}

}
