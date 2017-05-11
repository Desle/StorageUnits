package com.desle.storageUnits;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		saveDefaultConfig();
		
		/*List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_GRAY + "Required to fuel all");
		lore.add(ChatColor.DARK_GRAY + "energy-related items.");
		lore.add("");
		lore.add("");
		*/
		
		Set<String> cs = getConfig().getConfigurationSection("STORAGE_UNITS").getKeys(false);
		
		String path = "STORAGE_UNITS.";
		
		for (String key : cs) {
			
			String displayName = ChatColor.translateAlternateColorCodes('&', getConfig().getString(path + key + ".DISPLAY_NAME"));
			int id = getConfig().getInt(path + key + ".ID");
			Material material = Material.valueOf(getConfig().getString(path + key + ".MATERIAL_NAME"));
			int capacity = getConfig().getInt(path + key + ".CAPACITY");
			List<String> rawLore = getConfig().getStringList(path + key + ".LORE");
			List<String> lore = new ArrayList<String>();
			
			for (String loreLine : rawLore) {
				lore.add(ChatColor.translateAlternateColorCodes('&', loreLine));
				
				if (lore.size() == 2)
					break;
			}
			
			lore.add("");
			lore.add("");
			
			new StorageUnit(material, id, lore, displayName, capacity);
		}
	}
	
	@Override
	public void onDisable() {
	}
}
