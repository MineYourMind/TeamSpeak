package de.mauricius17.teamspeak.verifier.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.DatabaseClientInfo;
import org.bukkit.entity.Player;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import de.mauricius17.teamspeak.verifier.mysql.TeamSpeakMySQL;
import de.mauricius17.teamspeak.verifier.system.TeamSpeakVerifier;
import net.kaikk.mc.uuidprovider.UUIDProvider;

public class TeamSpeakUtils {

	public static void addIdentity(final Player p, String UId, int rankId) {
		
		TeamSpeakMySQL.getIdentitys(UUIDProvider.getCachedPlayer(p.getName()).toString(), new Consumer<String>() {
			
			@Override
			public void accept(String result) {				
				String[] identities = result.split(";");
				
				if(identities.length >= Utils.getMaxIds()) {
					p.sendMessage(Messages.PREFIX.getMessage() + Messages.IDENTITY_ADD_TOMUCHIDS.getMessage());
					return;
				}
				
				for(int i = 0; i < identities.length; i++) {
					if(identities[i].equals(UId)) {
						p.sendMessage(Messages.PREFIX.getMessage() + Messages.IDENTITY_ADD_FAILED.getMessage());
						return;
					}
				}
				
				ClientInfo info = TeamSpeakVerifier.getTs3query().getApi().getClientByUId(UId);
				
				if(info != null) {
					int id = info.getDatabaseId();
					TeamSpeakVerifier.getTs3query().getApi().addClientToServerGroup(rankId, id);	
					TeamSpeakMySQL.addIdentity(UUIDProvider.getCachedPlayer(p.getName()).toString(), UId);
					
					p.sendMessage(Messages.PREFIX.getMessage() + Messages.IDENTITY_ADD_SUCCESS.getMessage().replace("[IDENTITY]", UId));
				} else {
					p.sendMessage(Messages.PREFIX.getMessage() + Messages.COMMAND_TEAMSPEAK_CLIENTNULL.getMessage().replace("[IDENTITY]", UId));
				}
			}
		});
	}
	
	public static void removeIdentity(final Player p, final String UId, final int rankId) {
		TeamSpeakMySQL.getIdentitys(UUIDProvider.getCachedPlayer(p.getName()).toString(), new Consumer<String>() {

			@Override
			public void accept(String result) {
				if(!result.contains(UId)) {
					p.sendMessage(Messages.PREFIX.getMessage() + Messages.IDENTITY_REMOVE_FAILED.getMessage());
					return;
				}
				TS3Api api = TeamSpeakVerifier.getTs3query().getApi();

				DatabaseClientInfo info = api.getDatabaseClientByUId(UId);

				int id = info.getDatabaseId();
				api.removeClientFromServerGroup(rankId, id);
				TeamSpeakMySQL.removeIdentity(UUIDProvider.getCachedPlayer(p.getName()).toString(), UId);

				p.sendMessage(Messages.PREFIX.getMessage() + Messages.IDENTITY_REMOVE_SUCCESS.getMessage().replace("[IDENTITY]", UId));
			}
		});
	}
	
	public static void getIdentities(final Player p) {
		TeamSpeakMySQL.getIdentitys(UUIDProvider.getCachedPlayer(p.getName()).toString(), new Consumer<String>() {

			List<String> id = new ArrayList<>();
			
			@Override
			public void accept(String result) {
				if(result.equalsIgnoreCase("wrong") || result.equalsIgnoreCase(" ") || result.equalsIgnoreCase("")) {
					p.sendMessage(Messages.PREFIX.getMessage() + Messages.IDENTITY_LIST_FAILED.getMessage());
				} else {
					String[] identities = result.split(";");
					
					p.sendMessage(Messages.IDENTITY_LIST_HEADER.getMessage());
					p.sendMessage(Messages.IDENTITY_LIST_MESSAGE.getMessage());
					
			        
					for(int i = 0; i < identities.length; i++) {
						id.add(identities[i]);
					}
					
					for(String ids : id) {
						p.sendMessage(Messages.IDENTITY_LIST_LIST.getMessage().replace("[IDS]", ids));
					}
					
					id.clear();
					p.sendMessage(Messages.IDENTITY_LIST_FOOTER.getMessage());
				}
			}
		});
	}
}