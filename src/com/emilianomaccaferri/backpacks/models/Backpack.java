package com.emilianomaccaferri.backpacks.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
		
		/*
		 * 
		 * items will be stored like this
		 * 
		 * */
		
		Bukkit.getLogger().info(this.backpack.getName());
		
	}
	
	public void update(ItemStack[] content) {
		
		/*
		 *
		 * updating storage of certain backpack
		 * 
		 * */
		
		JSONArray items = new JSONArray();
		
		for(int i = 0, len = content.length; i < len; i++) {
			
			ItemStack item = content[i];
			if(item == null)
				continue;
			
			JSONObject currentItem = new JSONObject();
			currentItem.put("enchantments", new String());
			
			int length = item.getEnchantments().entrySet().size();
			int last = 1;
			
			item.getEnchantments()
			.entrySet()
			.stream()
			.forEach(it -> {
				
				
				
			});
			
			/*item
			.serialize()
			.entrySet()
			.stream()
			.forEach(is -> {
				
				if(is.getKey() == "meta") {
					
					item.getEnchantments()
					.entrySet()
					.stream()
					.forEach(it -> {
						
						Bukkit.getLogger().info(it.getKey().getName());
						
					});
					
				}else
				currentItem.put(is.getKey().toString(), is.getValue().toString());
				
			});*/
			
			items.add(currentItem);
			
		}
		
		Bukkit.getLogger().info(items.toString());
		
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
