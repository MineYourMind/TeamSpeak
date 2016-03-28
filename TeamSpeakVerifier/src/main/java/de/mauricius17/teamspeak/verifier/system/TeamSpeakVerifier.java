package de.mauricius17.teamspeak.verifier.system;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

import de.mauricius17.teamspeak.verifier.commands.TeamSpeakCommand;
import de.mauricius17.teamspeak.verifier.mysql.MySQL;
import de.mauricius17.teamspeak.verifier.utils.Messages;
import de.mauricius17.teamspeak.verifier.utils.Utils;

public class TeamSpeakVerifier extends JavaPlugin {

	private static TeamSpeakVerifier instance;
	private static TS3Config ts3config;
	private static TS3Query ts3query;

	
	@Override
	public void onEnable() {
		instance = this;
		
		loadConfig();
		loadTeamSpeakConfig();
		registerCommand();
		
		for(Messages m : Messages.values()) {
			m.setMessage(ChatColor.translateAlternateColorCodes('&', Utils.getMessages().getString(m.getPath())));
		}
	
	    Utils.setRank(Utils.getTeamSpeakCfg().getInt("rank_up"));
	    Utils.setMaxIds(Utils.getTeamSpeakCfg().getInt("max_addable_identities"));
		
	    new MySQL();
	    
	    if(MySQL.getSql().getBoolean("mysql")) {
	    	MySQL.connect();
	    	MySQL.createTable();
	    } else {
	    	Bukkit.getConsoleSender().sendMessage("§cYou have to setup the mysql settings in mysql.yml to connect to MySQL!");
	        Bukkit.shutdown();
	    }
	    
	    if(Utils.getTeamSpeakCfg().getBoolean("teamspeak")) {
	    	Bukkit.getConsoleSender().sendMessage("§aTeamSpeak is connecting ...");
	        ts3config = new TS3Config();
	        
	        ts3config.setHost(Utils.getTeamSpeakCfg().getString("host"));
	        
	        if(Utils.getTeamSpeakCfg().getBoolean("debug_mode")) {
	        	ts3config .setDebugLevel(Level.ALL);
	        	Bukkit.getConsoleSender().sendMessage("§9Debugmode enabled");
	        } else {
	        	ts3config.setDebugLevel(Level.OFF);
	        }
	        
	        // TS Api updated - no longer set through config.
	        //ts3config.setLoginCredentials(Utils.getTeamSpeakCfg().getString("username"), Utils.getTeamSpeakCfg().getString("password"));
	        ts3config.setQueryPort(Utils.getTeamSpeakCfg().getInt("server.port.query"));
	        ts3query = new TS3Query(ts3config);
	        
	        ts3query.connect();
	       
	        ts3query.getApi().login(Utils.getTeamSpeakCfg().getString("username"), Utils.getTeamSpeakCfg().getString("password"));
	        
	        if(Utils.getTeamSpeakCfg().getBoolean("server.serverId.enable")) {
		        ts3query.getApi().selectVirtualServerById(Utils.getTeamSpeakCfg().getInt("server.serverId.id"));	        	
	        } else {
	        	ts3query.getApi().selectVirtualServerByPort(Utils.getTeamSpeakCfg().getInt("server.port.voice"));	        	
	        }

			ts3query.getApi().setNickname(Utils.getTeamSpeakCfg().getString("username"));

			WelcomeMessage.main();
	       
	        Bukkit.getConsoleSender().sendMessage("§aTeamSpeak connected!");
	    } else {
	    	Bukkit.getConsoleSender().sendMessage("§cYou have to setup the teamspeak settings in teamspeak.yml to connect to TeamSpeak!");
	        Bukkit.shutdown();
	    }
	}
	
	@Override
	public void onDisable() {
		instance = null;
	}
	
	public static TeamSpeakVerifier getInstance() {
		return instance;
	}
	
	public static TS3Config getTs3config() {
		return ts3config;
	}
	
	public static TS3Query getTs3query() {
		return ts3query;
	}
	
	private void registerCommand() {
		getCommand("teamspeak").setExecutor(new TeamSpeakCommand());
	}
	
	private void loadConfig() {
		Utils.getMessages().options().header("In this file you can edit the messages. You should not remove the following: [IDENTITY] -> identity and [IDS] -> identities.");

		Utils.getMessages().addDefault("welcome_message", "Welcome!");

	    Utils.getMessages().addDefault("teamspeak.prefix", "&8[&5Teamspeak&8] ");
	    Utils.getMessages().addDefault("teamspeak.nopermission", "&cYou do not have the permissions!");
	    Utils.getMessages().addDefault("teamspeak.console", "&cThe console is not allowed to use that command!");
	    
	    Utils.getMessages().addDefault("command.teamspeak.help.header", "&8==========[&5TeamSpeak&8]==========");
	    Utils.getMessages().addDefault("command.teamspeak.help.list", "&7/teamspeak list &5 - list your identities");
	    Utils.getMessages().addDefault("command.teamspeak.help.add", "&7/teamspeak add &5 - adds an identity");
	    Utils.getMessages().addDefault("command.teamspeak.help.remove", "&7/teamspeak remove &5 - removes an identity");
	    Utils.getMessages().addDefault("command.teamspeak.help.footer", "&8===============================");
	    
	    Utils.getMessages().addDefault("command.teamspeak.spam", "&cPlease do not spam that command!");
	    Utils.getMessages().addDefault("command.teamspeak.client_null", "&cThe client with the identity &e[IDENTITY] &cis not online!");
	    
	    Utils.getMessages().addDefault("identity.add.success", "&aYou added the identity &e[IDENTITY]");
	    Utils.getMessages().addDefault("identity.add.failed", "&cThis identity is already verified!");
	    Utils.getMessages().addDefault("identity.add.tomuchids", "&cYou have already verified the max size of identities!");
	    
	    Utils.getMessages().addDefault("identity.remove.success", "&aYou removed the identity &e[IDENTITY]");
	    Utils.getMessages().addDefault("identity.remove.failed", "&cThis identity is not verified!");
	    
	    Utils.getMessages().addDefault("identities.list.failed", "&cYou have no identities verified!");
	    Utils.getMessages().addDefault("identities.list.header", "&8==========[&5TeamSpeak&8]==========");
	    Utils.getMessages().addDefault("identities.list.message", "&7You have verified the following identities:");
	    Utils.getMessages().addDefault("identities.list.list", "&a> [IDS]");
	    Utils.getMessages().addDefault("identities.list.footer", "&8===============================");
		
	    Utils.getMessages().options().copyDefaults(true);
	    
	    try {
			Utils.getMessages().save(Utils.getMessageFile());
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage("§cThe messages.yml could not be saved!");
			e.printStackTrace();
		}
	}
	
	private void loadTeamSpeakConfig() {
		Utils.getTeamSpeakCfg().options().header("In this file you can edit the teamspeak settings");
		
		Utils.getTeamSpeakCfg().addDefault("teamspeak", Boolean.valueOf(false));
	    Utils.getTeamSpeakCfg().addDefault("debug_mode", Boolean.valueOf(false));
		Utils.getTeamSpeakCfg().addDefault("welcome_message", Boolean.valueOf(false));
	    Utils.getTeamSpeakCfg().addDefault("host", "localhost");
	    Utils.getTeamSpeakCfg().addDefault("username", "user");
	    Utils.getTeamSpeakCfg().addDefault("password", "password");
	    
	    Utils.getTeamSpeakCfg().addDefault("server.serverId.enable", false);
	    Utils.getTeamSpeakCfg().addDefault("server.serverId.id", 1);
	    Utils.getTeamSpeakCfg().addDefault("server.port.query", 10011);
	    Utils.getTeamSpeakCfg().addDefault("server.port.voice", 9987);
	    Utils.getTeamSpeakCfg().addDefault("rank_up", Integer.valueOf(7));
	    Utils.getTeamSpeakCfg().addDefault("max_addable_identities", 5);
	    
	    Utils.getTeamSpeakCfg().options().copyDefaults(true);
	    
	    try {
			Utils.getTeamSpeakCfg().save(Utils.getTeamSpeakFile());
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage("§cThe config.yml could not be saved!");
			e.printStackTrace();
		}
	}
}