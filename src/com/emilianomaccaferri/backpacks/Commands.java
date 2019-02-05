package com.emilianomaccaferri.backpacks;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.emilianomaccaferri.backpacks.models.Backpack;

public class Commands implements CommandExecutor {
		
	private Backpacks instance;
	private final String PLUGIN_FOLDER;
	
	public Commands(Backpacks backpacks) {
		
		this.instance = backpacks;
		this.PLUGIN_FOLDER = this.instance.getDataFolder().getAbsolutePath(); 
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("backpack")){
				
			if(!(sender instanceof Player)) {
				
				sender.sendMessage("You must be a player to execute this command.");
				
			}else {
				
				Player player = (Player) sender;
								
				String inventoryDir = this.PLUGIN_FOLDER + "/inventories/" + player.getUniqueId();
				
				if(args.length <= 0) {
					sender.sendMessage("/backpack create <backpack_name> [size (default: medium = 27)]");
					return true;
				}
				
				switch(args[0]) {
				
				case "create":
					
					int size = 27;
					if(args.length > 2) {
						
						if(args[2].equalsIgnoreCase("small"))
							size = 18;
						else if(args[2].equalsIgnoreCase("medium"))
							size = 27;
						else if(args[2].equalsIgnoreCase("big"))
							size = 64;
						else{
							player.sendMessage("Invalid size, must be 'small', 'medium' or 'big'");
							return true;
						}
						
					}
					
					String backpackName = args[1];
					
					Backpack newBackpack = new Backpack(backpackName, size, player);
					int created = Backpack.create(newBackpack, player);	
					
					if(created == 0) 						
						player.sendMessage("That backpack already exists!");
					else if(created == -1)
						player.sendMessage("Something went wrong while creating that backpack...");	
					else
						player.sendMessage("Backpack "+ backpackName +" has been created!");
					
					newBackpack.open();
											
					break;
				
				}
				
				/*ItemStack item = player.getInventory().getItemInMainHand();
				Map<String, Object> serialized = item.serialize();
				
				serialized
				.entrySet()
				.parallelStream()
				.forEach(i -> {
					
					Bukkit.getLogger().info(i.getKey().toString() + " - " + i.getValue().toString());
					
				});*/			
				
			}
			
		}
		
		return true;
	}
	
	

}
