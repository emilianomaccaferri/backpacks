package com.emilianomaccaferri.backpacks;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import com.emilianomaccaferri.backpacks.models.Backpack;

public class Backpacks extends JavaPlugin {
	
	public static String DATA_FOLDER;
	public static Backpacks PLUGIN;
	public static HashMap<UUID, ArrayList<Backpack>> ALL = new HashMap<UUID, ArrayList<Backpack>>();
	
	public void onEnable() {
		
		this.saveDefaultConfig();
		DATA_FOLDER = this.getDataFolder().getAbsolutePath();
		PLUGIN = this;
		int backpacksCount = 0;
		this.getCommand("backpack").setExecutor(new Commands(this));
					
		//new Thread(() -> {
		// not sure if I should use a thread here because of Bukkit
			
		Bukkit.getLogger().info(Utils.colored("&eLoading backpacks"));
		new File(DATA_FOLDER + "/inventories").mkdirs();
		
		File[] directories = new File(DATA_FOLDER + "/inventories").listFiles(File::isDirectory);

		for(File directory: directories) {
			
			Backpack.friends.put(UUID.fromString(directory.getName()), new ArrayList<Backpack>());
					
			File[] backpacks = new File(DATA_FOLDER + "/inventories/" + directory.getName()).listFiles();
					
			for(File backpack: backpacks) {
				
				try {
					backpacksCount++;
					Backpack newBackpack = new Backpack(new FileReader(DATA_FOLDER + "/inventories/" + directory.getName() + "/" + backpack.getName()));
					Backpack.friends.get(UUID.fromString(directory.getName())).add(newBackpack);
					
				} catch (IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						
			}
			
			Bukkit.getLogger().info(Utils.colored("&aLoaded " + String.valueOf(backpacksCount) + " backpack(s)"));
					
					
		}
			
		// }); 
			
		this.getServer().getPluginManager().registerEvents(new Events(), this);
		
	}
	
}
