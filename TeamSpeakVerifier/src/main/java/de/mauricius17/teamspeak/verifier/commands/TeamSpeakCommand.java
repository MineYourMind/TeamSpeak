package de.mauricius17.teamspeak.verifier.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mauricius17.teamspeak.verifier.system.TeamSpeakVerifier;
import de.mauricius17.teamspeak.verifier.utils.Messages;
import de.mauricius17.teamspeak.verifier.utils.Permissions;
import de.mauricius17.teamspeak.verifier.utils.TeamSpeakUtils;
import de.mauricius17.teamspeak.verifier.utils.Utils;

public class TeamSpeakCommand implements CommandExecutor {

	List<UUID> list = new ArrayList<>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		 
		if(!(sender instanceof Player)) {
			 sender.sendMessage(Messages.CONSOLE.getMessage());
			return true;
		}
		
		Player p = (Player) sender;	
		
		if(args.length != 1 && args.length != 2) {
			sendHelpMessage(p);
			return true;
		}
		
		if(args.length == 1) {
			 if(args[0].equalsIgnoreCase("list")) {
				 if(p.hasPermission(Permissions.TEAMSPEAK_IDS_LIST.getPermission())) {
					 TeamSpeakUtils.getIdentities(p);
				 } else {
					 p.sendMessage(Messages.NOPERMISSION.getMessage());
				 }
			 } else {
				 sendHelpMessage(p);
			 }
		}
		
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("add")) {
				if(p.hasPermission(Permissions.TEAMSPEAK_ID_ADD.getPermission())) {
					if(list.contains(p.getUniqueId())) {
						p.sendMessage(Messages.COMMAND_TEAMSPEAK_SPAM.getMessage());
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
					 p.sendMessage(Messages.PREFIX.getMessage() + Messages.NOPERMISSION.getMessage());
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("remove")) {
				if(p.hasPermission(Permissions.TEAMSPEAK_ID_REMOVE.getPermission())) {
					if(list.contains(p.getUniqueId())) {
						p.sendMessage(Messages.COMMAND_TEAMSPEAK_SPAM.getMessage());
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
					 p.sendMessage(Messages.PREFIX.getMessage() + Messages.NOPERMISSION.getMessage());
				}
				return true;
			}
			
			sendHelpMessage(p);
		}
		
		return true;
	}
	
	private void sendHelpMessage(Player p) {
		p.sendMessage(Messages.COMMAND_TEAMSPEAK_HELP_HEADER.getMessage());
		p.sendMessage(Messages.COMMAND_TEAMSPEAK_HELP_LIST.getMessage());
		p.sendMessage(Messages.COMMAND_TEAMSPEAK_HELP_ADD.getMessage());
		p.sendMessage(Messages.COMMAND_TEAMSPEAK_HELP_REMOVE.getMessage());
		p.sendMessage(Messages.COMMAND_TEAMSPEAK_HELP_FOOTER.getMessage());
	}
}