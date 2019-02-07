package com.emilianomaccaferri.backpacks;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.emilianomaccaferri.backpacks.models.Backpack;

public class Backpacks extends JavaPlugin {
	
	public static String DATA_FOLDER;
	public static HashMap<UUID, ArrayList<Backpack>> ALL = new HashMap<UUID, ArrayList<Backpack>>();
	
	public void onEnable() {
		
		Bukkit.getLogger().info("§a[Backpacks] Plugin starting up...");
		this.saveDefaultConfig();
		DATA_FOLDER = this.getDataFolder().getAbsolutePath();
		this.getCommand("backpack").setExecutor(new Commands(this));
					
		//new Thread(() -> {
		// not sure if I should use a thread here because of Bukkit
			
		Bukkit.getLogger().info("§a[Backpacks] Loading backpacks...");
		
		JSONParser parser = new JSONParser();
		File[] directories = new File(DATA_FOLDER + "/inventories").listFiles(File::isDirectory);

		for(File directory: directories) {
					
			Player player = Bukkit.getPlayer(directory.getName());
			
			Backpack.friends.put(UUID.fromString(directory.getName()), new ArrayList<Backpack>());
					
			File[] backpacks = new File(DATA_FOLDER + "/inventories/" + directory.getName()).listFiles();
					
			for(File backpack: backpacks) {
				
				try {
					
					Backpack newBackpack = new Backpack(new FileReader(DATA_FOLDER + "/inventories/" + directory.getName() + "/" + backpack.getName()));
					Backpack.friends.get(UUID.fromString(directory.getName())).add(newBackpack);
					
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
			}
					
					
		}
			
		// }); 
			
		// this.getServer().getPluginManager().registerEvents(arg0, this);
		
	}
	
}
