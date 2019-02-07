package com.emilianomaccaferri.backpacks;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.emilianomaccaferri.backpacks.models.Backpack;

public class Events implements Listener{
	
	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		
		UUID uuid = e.getWhoClicked().getUniqueId();
		
		if(Backpack.getAllBackpacks().get(uuid) == null)
			return;
		
		ArrayList<Backpack> backpacks = Backpack.getAllBackpacks().get(uuid);
		
		for(int i = 0, len = backpacks.size(); i < len; i++) {
			
			Backpack current = backpacks.get(i);
			
		}
		
	}

}
