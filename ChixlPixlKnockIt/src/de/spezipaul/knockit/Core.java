package de.spezipaul.knockit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.spezipaul.knockit.events.DamageHandler;
import de.spezipaul.knockit.events.FallDownEvent;
import de.spezipaul.knockit.events.JoinAndQuitMessage;
import de.spezipaul.knockit.events.JoinOpenClassChooser;
import de.spezipaul.knockit.events.NoHunger;
import de.spezipaul.knockit.gui.ClassChooser;
import de.spezipaul.knockit.managers.KillStreakManager;
import de.spezipaul.knockit.managers.KittsManager;

public class Core extends JavaPlugin {
	
	public static Core instance;
	public static String PLPrefix = "§8[§cKnockIt§8] §3";

	public static String kickMessage = "Der Server startet neu. Bitte versuche, erneut beizutreten.";
	public static String joinMessage = "%NAME ist dem Spiel beigetreten.";
	public static String leaveMessage = "%NAME hat das Spiel verlassen.";
	public static String privateKillstreakMessage = "Du bist auf einer %COUNT-er Killstreak.";
	public static String privateKillstreakEndMessage = "Deine %COUNT-er Killstreak wurde beendet.";
	public static String globalKillstreakMessage = "%NAME ist auf einer %COUNT-er Killstreak.";
	public static String globalKillstreakEndMessage = "%NAME's %COUNT-er Killstreak wurde beendet.";
	public static int globalKillstreakDivider = 5;
	
	public static KittsManager kittsManager;
	public static KillStreakManager killstreakManager;
	
	@Override
	public void onEnable() {
		
		initiateManagers();
		registerEvents();
		
		initConfig();
	}

	private void initiateManagers() {
		instance = this;
		kittsManager = new KittsManager(); //Lel easteregg
		killstreakManager = new KillStreakManager();		
	}
	
	private void registerEvents() {
		new FallDownEvent();
		new JoinOpenClassChooser();
		new DamageHandler();
		new JoinAndQuitMessage();
		new NoHunger();
	}

	@Override
	public void onDisable() {
		for(Player p : getServer().getOnlinePlayers()){
			p.kickPlayer(PLPrefix + " " + kickMessage);
		}
	}
	
	private void initConfig() {
		getConfig().options().header("By spezipaul & Kitt3120");
		getConfig().options().copyDefaults(true);

		getConfig().addDefault("Messages.Join", joinMessage);
		getConfig().addDefault("Messages.Leave", leaveMessage);
		getConfig().addDefault("Messages.Kick", kickMessage);
		getConfig().addDefault("Messages.Killstreak.Private.Message", privateKillstreakMessage);
		getConfig().addDefault("Messages.Killstreak.Private.End", privateKillstreakEndMessage);
		getConfig().addDefault("Messages.Killstreak.Global.Message", globalKillstreakMessage);
		getConfig().addDefault("Messages.Killstreak.Global.End", globalKillstreakEndMessage);
		getConfig().addDefault("Messages.Killstreak.Global.Divider", globalKillstreakDivider);
		
		saveConfig();
		
		joinMessage = getConfig().getString("Messages.Join");
		leaveMessage = getConfig().getString("Messages.Leave");
		kickMessage = getConfig().getString("Messages.Kick");
		privateKillstreakMessage = getConfig().getString("Messages.Killstreak.Private.Message");
		privateKillstreakEndMessage = getConfig().getString("Messages.Killstreak.Private.End");
		globalKillstreakMessage = getConfig().getString("Messages.Killstreak.Global.Message");
		globalKillstreakEndMessage = getConfig().getString("Messages.Killstreak.Global.End");
		globalKillstreakDivider = getConfig().getInt("Messages.Killstreak.Global.Divider");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("kit")) {
			if(sender instanceof Player) {
				new ClassChooser((Player) sender);
				return true;
			} else {
				sender.sendMessage(PLPrefix + "Warning: Console or CommandBlock tried to open the ClassChooser");
				return true;
			}
		}
		return false;
	}
	
	public void registerEvents(Listener listener){
		getServer().getPluginManager().registerEvents(listener, this);
	}

}
