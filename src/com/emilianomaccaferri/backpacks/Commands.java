package com.emilianomaccaferri.backpacks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.json.simple.parser.ParseException;

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
							
				if(args.length <= 0) {
					sender.sendMessage("/backpack create <backpack_name> [size (default: medium = 27)]");
					return true;
				}
				
				String backpackName;
				try {
					backpackName = args[0];
				}catch(Exception e) {
					backpackName = "";
				}
				
				switch(args[0]) {
				
				case "list":
					
					if(Backpack.getAllBackpacks().containsKey(player.getUniqueId())) {
						
						ListInventory listInventory = new ListInventory(player, "&c&lYour inventories", 54);
						listInventory.open();
						
					}else {
						
						player.sendMessage(Utils.colored("&cYou have no backpacks"));
						
					}
					
					break;
				
				case "open":
					
					HashMap<UUID, ArrayList<Backpack>> table = Backpack.getAllBackpacks();
					
					if(table.get(player.getUniqueId()) == null)
						sender.sendMessage("You have no backpacks");
					else {
						
						for(int i = 0, len = table.get(player.getUniqueId()).size(); i < len; i++) {
							
							Backpack b = table.get(player.getUniqueId()).get(i);
							if(b.getName().equalsIgnoreCase(backpackName)) {
								
								try {
									b.load();
									
									return true;
									
								} catch (IOException | ParseException e) {
									e.printStackTrace();
									sender.sendMessage("Something went wrong...");
								}
							}
							
						}
						
						sender.sendMessage("No backpacks found with that name");
												
					}
					
					break;
				
				case "create":
					
					int size = 27;
					if(args.length > 2) {
						
						if(args[2].equalsIgnoreCase("small"))
							size = 18;
						else if(args[2].equalsIgnoreCase("medium"))
							size = 27;
						else if(args[2].equalsIgnoreCase("big"))
							size = 54;
						else{
							player.sendMessage("Invalid size, must be 'small', 'medium' or 'big'");
							return true;
						}
						
					}
					
					// economy stuff here
					
					int created = Backpack.create(Utils.coloredNoPrefix(backpackName), size, player);	
					if(created == 0) 						
						player.sendMessage("That backpack already exists!");
					else if(created == -1)
						player.sendMessage("Something went wrong while creating that backpack...");	
					else
						player.sendMessage("Backpack "+ backpackName +" has been created!");
									
					break;
					
				default:
					
					sender.sendMessage("/backpack create <backpack_name> [size (default: medium = 27)]");
					
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
