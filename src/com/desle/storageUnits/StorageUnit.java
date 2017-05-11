package com.desle.storageUnits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StorageUnit {
	
	public static Map<Integer, StorageUnit> list = new HashMap<Integer, StorageUnit>();
	
	
	
	private int id;
	private Material material;
	private String displayName;
	private List<String> lore;
	private int capacity;
	private ItemStack itemStack;
	
	
	
	public StorageUnit(Material material, int id, List<String> lore, String displayName, int capacity) {
		
		this.material = material;
		this.id = id;
		this.lore = lore;
		this.displayName = displayName;
		this.capacity = capacity;

		this.initItemStack();
		
		list.put(id, this);
	}
	
	
	
	
	
	public int getId() {
		return this.id;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public List<String> getLore() {
		return this.lore;
	}
	
	public ItemStack getItemStack() {		
		return this.itemStack.clone();
	}
	
	
	
	
	public void initItemStack() {
		
		ItemStack itemStack = new ItemStack(this.getMaterial());
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.setLore(this.getLore());
		itemMeta.setDisplayName(this.getDisplayName());
		
		
		DataManager dataManager = DataManager.get();
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("STORAGE_UNIT", "true");
		data.put("ID", "" + this.getId());
		data.put("CAPACITY", "" + this.getCapacity());
		
		
		itemMeta = dataManager.getItemMetaWithData(itemMeta, data, 2);		

		itemStack.setItemMeta(itemMeta);
		
		this.itemStack = StorageUnitManager.get().setStorageUnitValue(itemStack, 0);
	}
	
}
