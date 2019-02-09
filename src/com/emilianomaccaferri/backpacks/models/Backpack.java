package com.emilianomaccaferri.backpacks.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.emilianomaccaferri.backpacks.Backpacks;


public class Backpack {
	
	public static HashMap<UUID, ArrayList<Backpack>> friends = new HashMap<UUID, ArrayList<Backpack>>();
	private String name;
	private int size; 
	private Inventory backpack;
	private Player holder;
	private JSONParser parser = new JSONParser();
	private JSONObject data;
	private JSONArray items;
	
	public Backpack(FileReader data) throws IOException, ParseException{
			
		this.data = (JSONObject)this.parser.parse(data);
		this.name = (String) this.data.get("name");
		this.size = Integer.parseInt((String) this.data.get("size"));
		this.holder = Bukkit.getPlayer(UUID.fromString((String) this.data.get("holder")));
		this.items = (JSONArray) this.parser.parse(this.data.get("items").toString());
		this.backpack = Bukkit.createInventory(this.holder, this.size, this.name);
		
		for(Object item: this.items) {
			
			JSONObject itm = (JSONObject) item;
			
			if(((String) itm.get("item")).equalsIgnoreCase("AIR")) {
				this.backpack.addItem(new ItemStack(Material.AIR));
				continue;
			}
				ItemStack is = new ItemStack(Material.valueOf((String) itm.get("item")), Integer.parseInt((String)itm.get("amount")));
				String[] enchants = ((String) itm.get("enchantments")).split(",");
				ItemMeta itemMeta = is.getItemMeta();
				
				if((String)itm.get("display-name") != null) 
					itemMeta.setDisplayName((String)itm.get("display-name"));
				
				Bukkit.getLogger().info(enchants[0]);
				
				if(enchants.length > 0) {
					
					for(int i = 0, len = enchants.length; i < len; i++) {
						
						String enchName = enchants[i].split(":")[0];
						int enchLvl = Integer.parseInt(enchants[i].split(":")[1]);
						
						Bukkit.getLogger().info(enchName + ":" + String.valueOf(enchLvl));
						
						itemMeta.addEnchant(
								Enchantment.getByKey(
									NamespacedKey.minecraft(enchName)
								), enchLvl, true);
						
					}
					
				}
				
				is.setItemMeta(itemMeta);
				
				this.backpack.addItem(is);
			
		}
		
	}
	
	public void update(ItemStack[] content) {
		
		/*
		 *
		 * updating storage of certain backpack
		 * 
		 * */
		
		JSONArray items = new JSONArray();
		
		for(int i = 0, len = content.length; i < len; i++) {
			
			JSONObject currentItem = new JSONObject();
			
			ItemStack item = content[i];
			if(item == null) {
				currentItem.put("item", "AIR");
				items.add(currentItem);
				continue;
			}
				
			String enchs = "";

			for(Entry<Enchantment, Integer> entry: item.getEnchantments().entrySet()) {
				
				enchs += entry.getKey().getKey().toString().split(":")[1] + ":" + String.valueOf(entry.getValue()) + ",";
				
				
			}
			
			enchs = enchs.replaceAll(",$", ""); // replace the last comma with nothing
			
			currentItem.put("item", item.getType().toString());
			currentItem.put("display-name", item.getItemMeta().getDisplayName());
			currentItem.put("amount", String.valueOf(item.getAmount()));
			currentItem.put("lore", (item.getItemMeta().hasLore()) ? item.getItemMeta().getLore().toString() : "");
			currentItem.put("enchantments", enchs);			
			items.add(currentItem);
			
		}
		
		// dump inventory to config
		
		Bukkit.getLogger().info(items.toString());
		String newBackpack = "{\"name\": \"" + this.name + "\", \"holder\": \"" + this.holder.getUniqueId() + "\", \"size\": \"" + this.size + "\", \"items\": " + items.toString() + " }";
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream(Backpacks.DATA_FOLDER + "/inventories/" + this.holder.getUniqueId() + "/" + this.name + ".json", false));
			out.println(newBackpack);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.holder.sendMessage("Error while updating inventory...");
		}
	}
	
	public Inventory getInventory() {
		
		return this.backpack;
		
	}
	
	public void load() throws FileNotFoundException, IOException, ParseException {
		
		this.holder.openInventory(this.backpack);
		
	}
	
	public static HashMap<UUID, ArrayList<Backpack>> getAllBackpacks(){
		
		return friends;
		
	}
	
	public String getName() {
		
		return this.name;
		
	}
	
	public int getSize() {
		
		return this.size;
		
	}
	
	public Player getHolder() {
		
		return this.holder;
		
	}
	
	public static int create(String name, int size, Player player) {
		
		String backpackDir = Backpacks.DATA_FOLDER + "/inventories/" + player.getUniqueId();
		File directory = new File(backpackDir);
		
		if(!directory.exists())
			directory.mkdirs();
		
		File inventory = new File(backpackDir + "/" + name + ".json");
		if(inventory.exists()) {
			return 0;
		}
		
		try {
			
			inventory.createNewFile();
			PrintWriter out = new PrintWriter(inventory);
			
			out.println("{\"name\": \""+ name +"\", \"holder\": \"" + player.getUniqueId() + "\", \"size\": \"" + size + "\", \"items\": [] }");
			out.close();
			
			Backpack backpack = new Backpack(new FileReader(Backpacks.DATA_FOLDER + "/inventories/" + player.getUniqueId() + "/" + name + ".json"));
			
			if(friends.get(backpack.getHolder().getUniqueId()) != null)
				friends.get(backpack.getHolder().getUniqueId()).add(backpack);
			else {
			
				friends.put(backpack.getHolder().getUniqueId(), new ArrayList<Backpack>());
				friends.get(backpack.getHolder().getUniqueId()).add(backpack);
			}

			backpack.load();
			
			return 1;
			
		} catch (IOException | ParseException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
			
		}
		
	}
	
}
