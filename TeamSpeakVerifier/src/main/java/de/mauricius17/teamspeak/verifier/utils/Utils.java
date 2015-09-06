package de.mauricius17.teamspeak.verifier.utils;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Utils {
	private static File teamSpeakFile = new File("plugins/TeamSpeak", "teamspeak.yml");
	private static FileConfiguration teamSpeakCfg = YamlConfiguration.loadConfiguration(teamSpeakFile);
	
	private static File messageFile = new File("plugins/TeamSpeak", "messages.yml");
	private static FileConfiguration messages = YamlConfiguration.loadConfiguration(messageFile);
	  
	private static int rank = 0;
	private static int maxIds = 1;
	
	public static int getMaxIds() {
		return maxIds;
	}
	
	public static void setMaxIds(int maxIds) {
		Utils.maxIds = maxIds;
	}
	
	public static void setRank(int rank) {
		Utils.rank = rank;
	}
	
	public static File getMessageFile() {
		return messageFile;
	}
	
	public static FileConfiguration getMessages() {
		return messages;
	}

	public static int getRank() {
		return rank;
	}
	
	public static FileConfiguration getTeamSpeakCfg() {
		return teamSpeakCfg;
	}
	
	public static File getTeamSpeakFile() {
		return teamSpeakFile;
	}
}