package com.emilianomaccaferri.backpacks;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.emilianomaccaferri.backpacks.models.Backpack;

public class Events implements Listener{
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e) {
		
		Player player = (Player) e.getInventory().getHolder();
		UUID uuid = player.getUniqueId();
		
		if(Backpack.getAllBackpacks().get(uuid) == null || uuid == null)
			return;
		
		// loop backpacks of the user who closed the inventory
		
		ArrayList<Backpack> backpacks = Backpack.getAllBackpacks().get(uuid);
		
		for(int i = 0, len = backpacks.size(); i < len; i++) {
			
			Backpack current = backpacks.get(i);
			
			Bukkit.getLogger().info(current.getName());
			
			// check if the inventory is a backpack
			
			if(current.getName().equals(e.getInventory().getName())) {
				
				current.update(e.getInventory().getContents());
				
			}
			
		}
		
	}

}
