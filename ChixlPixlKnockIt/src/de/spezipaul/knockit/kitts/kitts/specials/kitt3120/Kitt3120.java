package de.spezipaul.knockit.kitts.kitts.specials.kitt3120;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import de.spezipaul.knockit.Core;
import de.spezipaul.knockit.kitts.Kitt;
import de.spezipaul.knockit.kitts.KittDescription;
import de.spezipaul.knockit.utils.ItemUtils;
import de.spezipaul.knockit.utils.ParticleUtils;

public class Kitt3120 extends Kitt implements Listener {
	
	private ArrayList<CustomSkull> skulls = new ArrayList<>();
	private WitherBuddy buddy = null;
	
	//Items
	private ItemStack stick;	
	
	private ItemStack forcefieldToggle;
	private ItemStack fireForcefieldToggle;
	
	private ItemStack spawnBuddy;
	
	private ItemStack witherskullLauncher;
	
	//Booleans
	private boolean forcefieldEnabled = false;
	private boolean fireForcefieldEnabled = false;
	
	//Scheduler
	private int forcefieldScheduler;
	private int fireForcefieldScheduler;
	
	//Settings
	private int forcefieldRange = 7;

	public Kitt3120(Player owner, KittDescription desc) {
		super(owner, desc);
		Core.instance.registerEvents(this);

		forcefieldScheduler = Core.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Core.instance, new Runnable() {
			public void run() {
				if(isEnabled() && forcefieldEnabled){
					for(Entity ent : getOwner().getNearbyEntities(forcefieldRange, forcefieldRange, forcefieldRange)) {
						Location playerLoc = getOwner().getLocation();
						Location entLoc = ent.getLocation();
						Vector v = entLoc.toVector().subtract(playerLoc.toVector()).normalize();
						ent.setVelocity(ent.getVelocity().multiply(0.5).add(v.multiply(0.25)));
						ent.setFallDistance(0);
						ParticleUtils.particleEffect(ent.getLocation(), Effect.CLOUD, 1, 1, 1, 0, 0);
					}
				}
			}
		}, 1L, 1L);
		
		fireForcefieldScheduler = Core.instance.getServer().getScheduler().scheduleSyncRepeatingTask(Core.instance, new Runnable() {
			public void run() {
				if(isEnabled() && fireForcefieldEnabled){
					for(Entity ent : getOwner().getNearbyEntities(forcefieldRange, forcefieldRange, forcefieldRange)) {
						if(ent.getFireTicks() <= 0) ent.setFireTicks(20);
						ParticleUtils.particleEffect(ent.getLocation(), Effect.SMOKE, 1, 1, 1, 0, 0);
					}
				}
			}
		}, 1L, 1L);
	}

	@Override
	public void setupItems() {
		stick = ItemUtils.stick();
		forcefieldToggle = ItemUtils.create(Material.ENDER_PEARL, 1, "§cForcefield", new String[]{"§cToggle Forcefield"}, null);
		fireForcefieldToggle = ItemUtils.create(Material.ENDER_PEARL, 1, "§cFire-Forcefield", new String[]{"§cToggle Fire-Forcefield"}, null);
		
		witherskullLauncher = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.WITHER.ordinal());
		SkullMeta witherSkullMeta = (SkullMeta) witherskullLauncher.getItemMeta();
		witherSkullMeta.setDisplayName("§aSkull Launcher");
		witherSkullMeta.setLore(Arrays.asList("§aRechtsklick zum spawnen einer Skull", "§aLinksklick zum schießen aller Skulls"));
		
		spawnBuddy = ItemUtils.create(Material.FIREBALL, 1, "§cWither-Buddy", new String[]{"§cSpawnt oder despawnt deinen Wither-Buddy"}, null);
	}

	@Override
	public void stop() {
		Core.instance.getServer().getScheduler().cancelTask(forcefieldScheduler);
		Core.instance.getServer().getScheduler().cancelTask(fireForcefieldScheduler);
		
		for(CustomSkull skull : skulls){
			skull.remove(false);
		}
		skulls.clear();
		
		killBuddy();
	}

	@Override
	protected HashMap<Integer, ItemStack> getItemHashmap() {
		HashMap<Integer, ItemStack> items = new HashMap<>();
		items.put(0, stick);
		items.put(1, witherskullLauncher);
		items.put(4, spawnBuddy);
		items.put(7, forcefieldToggle);
		items.put(8, fireForcefieldToggle);
		return items;
	}
	
	public void shootSkulls() {
		for(CustomSkull s : skulls){
			s.shoot();
		}
		skulls.clear();
	}
	
	public void spawnBuddy() {
		if(buddy == null){
			getOwner().getInventory().removeItem(spawnBuddy);
			buddy = new WitherBuddy(this);
			ItemMeta meta = spawnBuddy.getItemMeta();
			meta.setDisplayName("§aWither-Buddy");
			spawnBuddy.setItemMeta(meta);
			spawnBuddy.setType(Material.NETHER_STAR);
			getOwner().getInventory().setItem(4, spawnBuddy);
		}
	}
	
	public void killBuddy() {
		if(buddy != null){
			getOwner().getInventory().removeItem(spawnBuddy);
			buddy.die(true);
			buddy = null;
			ItemMeta meta = spawnBuddy.getItemMeta();
			meta.setDisplayName("§cWither-Buddy");
			spawnBuddy.setItemMeta(meta);
			spawnBuddy.setType(Material.FIREBALL);
			getOwner().getInventory().setItem(4, spawnBuddy);
		}
	}
	
	@EventHandler
	public void onUseItem(PlayerInteractEvent e){
		if(isEnabled() && e.getPlayer().equals(getOwner()) && e.getItem() != null) {
			e.setCancelled(true);
			ItemStack item = e.getItem();
			if(item.equals(forcefieldToggle)) {
				ItemMeta forcefieldMeta = forcefieldToggle.getItemMeta();
				if(forcefieldToggle.getType().equals(Material.ENDER_PEARL)) {
					forcefieldEnabled = true;
					forcefieldToggle.setType(Material.EYE_OF_ENDER);
					forcefieldMeta.setDisplayName("§aForcefield");
				} else {
					forcefieldEnabled = false;
					forcefieldToggle.setType(Material.ENDER_PEARL);	
					forcefieldMeta.setDisplayName("§cForcefield");				
				}
				forcefieldToggle.setItemMeta(forcefieldMeta);
				e.getPlayer().setItemInHand(forcefieldToggle);
			}
			if(item.equals(fireForcefieldToggle)) {
				ItemMeta fireForcefieldMeta = fireForcefieldToggle.getItemMeta();
				if(fireForcefieldToggle.getType().equals(Material.ENDER_PEARL)) {
					fireForcefieldEnabled = true;
					fireForcefieldToggle.setType(Material.EYE_OF_ENDER);
					fireForcefieldMeta.setDisplayName("§aFire-Forcefield");
				} else {
					fireForcefieldEnabled = false;
					fireForcefieldToggle.setType(Material.ENDER_PEARL);	
					fireForcefieldMeta.setDisplayName("§cFire-Forcefield");				
				}
				fireForcefieldToggle.setItemMeta(fireForcefieldMeta);
				e.getPlayer().setItemInHand(fireForcefieldToggle);
			}
			if(item.equals(witherskullLauncher)){
				if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					skulls.add(new CustomSkull(this));
				} else if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					shootSkulls();
				}
			}
			if(item.equals(spawnBuddy)){
				if(buddy == null) {
					spawnBuddy();
				} else {
					killBuddy();
				}
			}
		}
	}

	public ArrayList<CustomSkull> getSkulls() {
		return skulls;
	}

}
