package de.mauricius17.teamspeak.verifier.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import de.mauricius17.teamspeak.verifier.mysql.TeamSpeakMySQL;
import de.mauricius17.teamspeak.verifier.system.TeamSpeakVerifier;

public class TeamSpeakUtils {

	public static void addIdentity(final Player p, String UId, int rankId) {
		TeamSpeakMySQL.getIdentitys(UUIDFetcher.getUUID(p.getName()).toString(), new Consumer<String>() {
			
			@Override
			public void accept(String result) {				
				String[] identities = result.split(";");
				
				for(int i = 0; i < identities.length; i++) {
					if(identities[i].equals(UId)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("identity.add.failed")));
						return;
					}
				}
				
				ClientInfo info = TeamSpeakVerifier.getTs3query().getApi().getClientByUId(UId);
				int id = info.getDatabaseId();
				TeamSpeakVerifier.getTs3query().getApi().addClientToServerGroup(rankId, id);	
				TeamSpeakMySQL.addIdentity(UUIDFetcher.getUUID(p.getName()).toString(), UId);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("identity.add.success").replace("[IDENTITY]", UId)));
			}
		});
	}
	
	public static void removeIdentity(final Player p, final String UId, final int rankId) {
		TeamSpeakMySQL.getIdentitys(UUIDFetcher.getUUID(p.getName()).toString(), new Consumer<String>() {

			@Override
			public void accept(String result) {
				if(!result.contains(UId)) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("identity.remove.failed")));
					return;
				}
				
				ClientInfo info = TeamSpeakVerifier.getTs3query().getApi().getClientByUId(UId);
				int id = info.getDatabaseId();
				TeamSpeakVerifier.getTs3query().getApi().removeClientFromServerGroup(rankId, id);
				TeamSpeakMySQL.removeIdentity(UUIDFetcher.getUUID(p.getName()).toString(), UId);
				
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("identity.remove.success").replace("[IDENTITY]", UId)));
			}
		});
	}
	
	public static void getIdentities(final Player p) {
		TeamSpeakMySQL.getIdentitys(UUIDFetcher.getUUID(p.getName()).toString(), new Consumer<String>() {

			List<String> id = new ArrayList<>();
			
			@Override
			public void accept(String result) {
				if(result.equalsIgnoreCase("wrong") || result.equalsIgnoreCase(" ") || result.equalsIgnoreCase("")) {
					p.sendMessage(Utils.getPrefix() + ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("identities.list.failed")));
				} else {
					
					String[] identities = result.split(";");
					
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("identities.list.header")));
			        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("identities.list.message")));
					
					for(int i = 0; i < identities.length; i++) {
						id.add(identities[i]);
					}
					
					for(String ids : id) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("identities.list.list").replace("[IDS]", ids)));
					}
					
					id.clear();
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString("identities.list.footer")));
				}
			}
		});
	}
}