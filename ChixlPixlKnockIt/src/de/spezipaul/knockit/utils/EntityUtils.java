package de.spezipaul.knockit.utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import net.minecraft.server.v1_9_R1.EntityInsentient;

public class EntityUtils {
	
	public static Entity getNearest(Location loc, ArrayList<Entity> entities){
		Entity nearest = null;
		for(Entity ent : entities){
			if(nearest == null || nearest.getLocation().distance(loc) > ent.getLocation().distance(loc)) nearest = ent;
		}
		return nearest;
	}
	
	public static void walk(LivingEntity entity, Location loc, float speed){
		((EntityInsentient) ((CraftLivingEntity) entity).getHandle()).getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), speed);
	}
	
}
