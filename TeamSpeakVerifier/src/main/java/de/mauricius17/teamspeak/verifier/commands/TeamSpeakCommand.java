package de.mauricius17.teamspeak.verifier.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mauricius17.teamspeak.verifier.system.TeamSpeakVerifier;
import de.mauricius17.teamspeak.verifier.utils.TeamSpeakUtils;
import de.mauricius17.teamspeak.verifier.utils.Utils;

public class TeamSpeakCommand implements CommandExecutor {

	List<UUID> list = new ArrayList<>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		 
		if(!(sender instanceof Player)) {
			sender.sendMessage(Utils.getConsole());
			return true;
		}
		
		Player p = (Player) sender;	
		
		if(args.length != 1 && args.length != 2) {
			sendHelpMessage(p);
			return true;
		}
		
		if(args.length == 1) {
			 if(args[0].equalsIgnoreCase("list")) {
				 if(p.hasPermission("teamspeak.verifier.list")) {
					 TeamSpeakUtils.getIdentities(p);
				 } else {
					 p.sendMessage(Utils.getNopermission());
				 }
			 } else {
				 sendHelpMessage(p);
			 }
		}
		
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("add")) {
				if(p.hasPermission("teamspeak.verifier.add")) {
					if(list.contains(p.getUniqueId())) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("command.teamspeak.spam")));
					} else {
						list.add(p.getUniqueId());
						
						String id = args[1];
						TeamSpeakUtils.addIdentity(p, id, Utils.getRank());
						Bukkit.getScheduler().scheduleSyncDelayedTask(TeamSpeakVerifier.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								list.remove(p.getUniqueId());
							}
						}, 100L);
					}
				} else {
					p.sendMessage(Utils.getPrefix() + Utils.getNopermission());
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("remove")) {
				if(p.hasPermission("teamspeak.verifier.remove")) {
					if(list.contains(p.getUniqueId())) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("command.teamspeak.spam")));
					} else {
						list.add(p.getUniqueId());
						
						String id = args[1];
						TeamSpeakUtils.removeIdentity(p, id, Utils.getRank());
						Bukkit.getScheduler().scheduleSyncDelayedTask(TeamSpeakVerifier.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								list.remove(p.getUniqueId());
							}
						}, 100L);
					}
				} else {
					p.sendMessage(Utils.getPrefix() + Utils.getNopermission());
				}
				return true;
			}
			
			sendHelpMessage(p);
		}
		
		return true;
	}
	
	private void sendHelpMessage(Player p) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("command.teamspeak.help.header")));
	    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("command.teamspeak.help.list")));
	    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("command.teamspeak.help.add")));
	    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("command.teamspeak.help.remove")));
	    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("command.teamspeak.help.footer")));
	}
}