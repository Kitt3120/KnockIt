package de.spezipaul.knockit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.spezipaul.knockit.managers.KillStreakManager;
import de.spezipaul.knockit.managers.KittsManager;

public class Core extends JavaPlugin {
	
	public static Core instance;
	public static String PLPrefix = "§8[§cKnockIt§8] §3";
	
	public static KittsManager kittsManager;
	public static KillStreakManager killstreakManager;
	
	@Override
	public void onEnable() {
		instance = this;
		kittsManager = new KittsManager(); //Lel easteregg
		killstreakManager = new KillStreakManager();

		initConfig();
	}
	
	@Override
	public void onDisable() {}
	
	private void initConfig() {
		getConfig().options().header("By spezipaul");
		getConfig().options().copyDefaults(true);
		
		//TODO: config
		
		saveConfig();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return super.onCommand(sender, command, label, args);
	}
	
	public void registerEvents(Listener listener){
		getServer().getPluginManager().registerEvents(listener, this);
	}

}
