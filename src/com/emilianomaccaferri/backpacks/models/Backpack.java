package com.emilianomaccaferri.backpacks.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.emilianomaccaferri.backpacks.Backpacks;

public class Backpack {
	
	private static ArrayList<Backpack> friends = new ArrayList<Backpack>();
	private String name;
	private int size; 
	private Inventory backpack;
	private Player holder;
	private JSONParser parser = new JSONParser();
	
	public Backpack(String name, int size, Player holder){
		
		this.name = name;
		this.size = size;
		this.holder = holder;
		this.backpack = Bukkit.createInventory(this.holder, this.size, this.name);
		
	}
	
	public void load() throws FileNotFoundException, IOException, ParseException {
		
		JSONArray arr = (JSONArray)this.parser.parse(new FileReader(Backpacks.DATA_FOLDER + "/inventories/" + this.holder.getUniqueId() + "/" + this.name + ".json"));
		
}
	
	public void open() {
		
		Bukkit.getPlayer(this.holder.getUniqueId()).openInventory(this.backpack);
		
	}
	
	public String getName() {
		
		return this.name;
		
	}
	
	public static int create(Backpack backpack, Player player) {
		
		String backpackDir = Backpacks.DATA_FOLDER + "/inventories/" + player.getUniqueId();
		File directory = new File(backpackDir);
		
		if(!directory.exists())
			directory.mkdirs();
		
		File inventory = new File(backpackDir + "/" + backpack.getName() + ".json");
		if(inventory.exists()) {
			return 0;
		}
		
		try {
			
			inventory.createNewFile();
			PrintWriter out = new PrintWriter(inventory);
			
			out.println("[]");
			out.close();
			
			friends.add(backpack);
			backpack.load();
			return 1;
			
		} catch (IOException | ParseException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
			
		}
		
	}
	
}
