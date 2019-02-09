package com.emilianomaccaferri.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ListInventory {
	
	private Inventory listInventory;
	private String name;
	private int size;
	private Player holder;
	private ItemStack terminator;
	
	public ListInventory(Player holder, String name, int size) {
		
		this.name = name;
		this.size = size;
		this.holder = holder;
		this.listInventory = Bukkit.createInventory(this.holder, this.size, this.name);
		this.terminator = new ItemStack(Material.STICK);
		this.terminator.getItemMeta().setDisplayName(Utils.coloredNoPrefix("&e Next page"));
		this.listInventory.setItem(53, this.terminator);
		
	}
	
	public void open() {
		
		this.holder.openInventory(this.listInventory);
		
	}

}
