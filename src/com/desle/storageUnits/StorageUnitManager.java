package com.desle.storageUnits;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class StorageUnitManager {
	
	private static StorageUnitManager instance;
	
	public static StorageUnitManager get() {
		if (instance == null)
			instance = new StorageUnitManager();
		
		return instance;
	}
	
	
	/**************************** copies a StorageUnit (fresh without any changed data)  ***********************************/
	public ItemStack getFreshStorageUnit(int id) {
		if (StorageUnit.list.containsKey(id))
			return StorageUnit.list.get(id).getItemStack();
		
		return new ItemStack(Material.AIR);
	}
	
	
	
	
	
	public String getInfoLore(int current, int capacity) {
		
		DataManager dataManager = DataManager.get();
		
		int percentage = (int) ((double) current * 100 / capacity);
		String currentK = dataManager.formatK(current).toUpperCase();
		
		
		if (capacity <= 0)
			return ChatColor.DARK_AQUA + "" + currentK + "  " + ChatColor.GRAY + "(No capacity)";
		

		String capacityK = dataManager.formatK(capacity).toUpperCase();
		return ChatColor.DARK_AQUA + "" + currentK + "/" + capacityK + "  " + ChatColor.GRAY + "(" + percentage + "%)";
	}
	
	
	
	
	
	public ItemStack setStorageUnitValue(ItemStack itemStack, int value) {
		DataManager dataManager = DataManager.get();

		Map<String, String> data = dataManager.getData(itemStack.getItemMeta());
		
		int capacity = dataManager.isInteger(data.get("CAPACITY"));
		int old = dataManager.isInteger(data.get("CURRENT"));
		
		if (old < 0)
			old = 0;
		
		if (capacity < 0)
			capacity = 0;
		
		data.put("CURRENT", value + "");
		
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		if (value >= capacity && !(capacity <= 0)) {
			value = capacity;
			
			itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		} else {
			itemMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
		}
		
		List<String> lore = itemMeta.getLore();
		
		lore.set(3, getInfoLore(value, capacity));
		
		itemMeta.setLore(lore);
		
		itemMeta = dataManager.getItemMetaWithData(itemMeta, data, 2);
		
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
	
	
	
	
	
	public ItemStack changeStorageUnitValue(ItemStack itemStack, int value) {
		DataManager dataManager = DataManager.get();
		
		Map<String, String> data = dataManager.getData(itemStack.getItemMeta());
		
		int capacity = dataManager.isInteger(data.get("CAPACITY"));
		int old = dataManager.isInteger(data.get("CURRENT"));
		int current = old + value;
		
		if (old < 0)
			old = 0;
		
		if (capacity < 0)
			capacity = 0;
		
		data = dataManager.addToData(data, "CURRENT", value, capacity);
		
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		if (current >= capacity && !(capacity <= 0)) {
			current = capacity;
			
			itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		} else {
			itemMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
		}
		
		List<String> lore = itemMeta.getLore();
		
		lore.set(3, getInfoLore(current, capacity));
		
		itemMeta.setLore(lore);
		
		itemStack.setItemMeta(itemMeta);
		
		itemMeta = dataManager.getItemMetaWithData(itemMeta, data, 2);
		
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
}