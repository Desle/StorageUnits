package com.desle.storageUnits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

public class DataManager {
	
	
	private static DataManager instance;
	public static DataManager get() {
		if (instance == null)
			instance = new DataManager();
		
		return instance;
	}
	
	
	/**************************** INVISIBLE STRING HANDLING ********************************/
	
	
	public String convertToVisibleString(String string) {
    	return string.replaceAll("§", "");
	}
	
	
	public String covertToInvisibleString(String string) {
        String invisibleString = "";
        
        for (char c : string.toCharArray())
        	invisibleString += ChatColor.COLOR_CHAR+"" + c;
        
        return invisibleString;
	}
	
	
	/**************************** DATA HANDLING ********************************/
	
	
	public Map<String, String> getData(String string) {
		String rawData = convertToVisibleString(string);
		
		Map<String, String> data = new HashMap<String, String>();
		
		if (!rawData.startsWith("?"))
			return data;
		
		rawData = rawData.split("\\?")[1];
		
		for (String rawDataSplit : rawData.split(",")) {
			if (rawDataSplit.contains(":")) {
				String[] keyAndValue = rawDataSplit.split(":");
				
				if (keyAndValue.length == 2)
					data.put(keyAndValue[0], keyAndValue[1]);
			}	
		}
		
		return data;
	}
	
	
	
	
	
	
	
	public Map<String, String> getData(ItemMeta itemMeta)	 {

		Map<String, String> data = new HashMap<String, String>();
		
		if (!itemMeta.hasLore() || itemMeta.getLore().isEmpty())
			return data;
		
		List<String> lore = itemMeta.getLore();
		
		for (String loreLine : lore) {
			data.putAll(getData(loreLine));
		}
		
		return data;
	}
	
	
	
	
	
	
	public String formatDataString(Map<String, String> data) {
		String dataString = "?";
		
		for (String key : data.keySet()) {
			dataString += key + ":" + data.get(key) + ",";
		}
		
		return covertToInvisibleString(dataString + "?");
	}
	
	
	
	
	
	
	
	public ItemMeta getItemMetaWithData(ItemMeta itemMeta, Map<String, String> data, int dataLoreLine) {
		String dataString = formatDataString(data);
		
		List<String> lore = new ArrayList<String>();
		
		if (itemMeta.hasLore())
			lore = itemMeta.getLore();
		
		if (lore.size() < dataLoreLine + 1) {
			for (int x = lore.size(); x < dataLoreLine + 1; x++)
				lore.add("");
		}
		
		String oldLore = lore.get(dataLoreLine);
		lore.set(dataLoreLine, dataString + oldLore);
		
		itemMeta.setLore(lore);		
		
		return itemMeta;
	}
	
	
	
	
	
	public Map<String, String> addToData(Map<String, String> data, String key, int value, int limit) {
		
		if (data.isEmpty() || !data.containsKey(key)) {
			data.put(key, "" + value);
			return data;
		}
		
		int intResponse = isInteger(data.get(key));
		
		if (intResponse == -1) {
			data.put(key, "" + value);
			return data;
		}
		
		int newValue = value + intResponse;
		
		if (!(limit <= 0) && newValue > limit)
			newValue = limit;
		
		data.put(key, "" + newValue);
		
		return data;
	}
	
	
	/****************************  NUMBER HANDLING  ***********************************/
	
	
	public int isInteger(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	
	
	private static final NavigableMap<Long, String> suffixes = new TreeMap<> ();
	static {
	  suffixes.put(1_000L, "k");
	  suffixes.put(1_000_000L, "M");
	  suffixes.put(1_000_000_000L, "G");
	  suffixes.put(1_000_000_000_000L, "T");
	  suffixes.put(1_000_000_000_000_000L, "P");
	  suffixes.put(1_000_000_000_000_000_000L, "E");
	}

	public String formatK(long value) {
	  //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
	  if (value == Long.MIN_VALUE) return formatK(Long.MIN_VALUE + 1);
	  if (value < 0) return "-" + formatK(-value);
	  if (value < 1000) return Long.toString(value); //deal with easy case

	  Entry<Long, String> e = suffixes.floorEntry(value);
	  Long divideBy = e.getKey();
	  String suffix = e.getValue();

	  long truncated = value / (divideBy / 10); //the number part of the output times 10
	  boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
	  return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}
}
