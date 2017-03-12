package de.spezipaul.knockit.utils;

import org.bukkit.Effect;
import org.bukkit.Location;

public class ParticleUtils {
	
	public static void particleEffect(Location loc, Effect effect, float offX, float offY, float offZ, float speed, int particleCount){
		loc.getWorld().spigot().playEffect(loc, effect, 0, 0, offX, offY, offZ, speed, particleCount, 1000);
	}

}
