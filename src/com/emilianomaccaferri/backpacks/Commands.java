package com.emilianomaccaferri.backpacks;

import java.util.Map;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {
		
	private Backpacks instance;
	
	public Commands(Backpacks backpacks) {
		
		this.instance = backpacks;
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("test")){
				
			if(sender instanceof Player) {
				
				Player player = (Player) sender;				
				ItemStack item = player.getInventory().getItemInMainHand();
				Map<String, Object> serialized = item.serialize();
				
				serialized
				.entrySet()
				.parallelStream()
				.forEach(i -> {
					
					Bukkit.getLogger().info(i.getKey().toString() + " - " + i.getValue().toString());
					
				});
				
				
				player.sendMessage("BONGLE");
				
				
			}else {
				
				sender.sendMessage("You must be a player to execute this command.");
				
			}
			
		}
		
		return true;
	}
	
	

}
