package fr.dechiret.simplesurvie;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Plugin extends JavaPlugin implements Listener {
	
	private static ItemStack boussole;
	
	@Override
	public void onEnable() {
		
		this.getServer().getConsoleSender().sendMessage("|=============| §bSimple §aSurvie §r|=============|");
		this.getServer().getConsoleSender().sendMessage("|                                           |");
		this.getServer().getConsoleSender().sendMessage("|§a       The Plugin has been started !       §r|");
		this.getServer().getConsoleSender().sendMessage("|                                           |");
		this.getServer().getConsoleSender().sendMessage("|===========================================|");
		
		this.getCommand("boussole-tracker").setExecutor(this);
		this.getServer().getPluginManager().registerEvents(this, this);
		
		setBousole();
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new  Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				for(Player player : Bukkit.getOnlinePlayers())
				if(player.getItemInHand().equals(boussole)) {
					
					Player target = null;
					int difference = -1;
					
					//player.sendMessage("update");
					for (Player playersLoc : Bukkit.getOnlinePlayers()) {
						
						if(!playersLoc.equals(player)) {
						
							Location loc = playersLoc.getLocation();
							int dif = getDifference(player.getLocation(), loc);
							
							if(dif < difference || difference == -1) {
								difference = dif; 
								target = playersLoc;
							}
						}
						
					}
					
					if(target != null) {
						Location loc = target.getLocation();
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a" + target.getName() + "§b - " + getDifference(player.getLocation(), loc) + " blocks"));
					} else {
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cIl n'y a pas d'autre joueur dans les parages."));
					}
					
					
					
				}
			}
		}, 10, 10);
	}
	
	
	
	@Override
	public void onDisable() {
		this.getServer().getConsoleSender().sendMessage("|=============| §bSimple §aSurvie §r|=============|");
		this.getServer().getConsoleSender().sendMessage("|                                           |");
		this.getServer().getConsoleSender().sendMessage("|§c       The Plugin has been stopped !       §r|");
		this.getServer().getConsoleSender().sendMessage("|                                           |");
		this.getServer().getConsoleSender().sendMessage("|===========================================|");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			player.getInventory().addItem(boussole);
		} else {
			sender.sendMessage("§cTu dois être un joueur pour utilisé cette commande !");
		}
		return true;
	}
	
	private static void setBousole() {
		ItemStack item = new ItemStack(Material.COMPASS, 1);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§eBoussole §cT§er§aa§bc§1k§de§fr");          // pour item custom
		itemMeta.setLore(Arrays.asList("C'est une boussole pour voir le joueur le plus proche"));
		itemMeta.addEnchant(Enchantment.MENDING, 1, true);
		itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(itemMeta);
		boussole = item;
	}
	
	private static int getDifference(Location tracker, Location target) {
		int X = 0;
		int Y = 0;
		int Z = 0;
		
		if(tracker.getBlockX() > target.getBlockX()) {
			X = tracker.getBlockX() - target.getBlockX();
		} else {
			X = target.getBlockX() - tracker.getBlockX();
		}
		
		if(tracker.getBlockY() > target.getBlockY()) {
			Y = tracker.getBlockY() - target.getBlockY();
		} else {
			Y = target.getBlockY() - tracker.getBlockY();
		}
		
		if(tracker.getBlockZ() > target.getBlockZ()) {
			Z = tracker.getBlockZ() - target.getBlockZ();
		} else {
			Z = target.getBlockZ() - tracker.getBlockZ();
		}
		
		return X + Y + Z;
	}

}
