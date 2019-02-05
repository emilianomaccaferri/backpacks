package com.emilianomaccaferri.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Backpacks extends JavaPlugin {
	
	public static String DATA_FOLDER;
	
	public void onEnable() {
		
		Bukkit.getLogger().info("§a[Backpacks] Plugin starting up...");
		this.saveDefaultConfig();
		DATA_FOLDER = this.getDataFolder().getAbsolutePath();
		this.getCommand("backpack").setExecutor(new Commands(this));
		// this.getServer().getPluginManager().registerEvents(arg0, this);
		
	}
	
}
