package de.mauricius17.teamspeak.verifier.utils;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Utils {
	private static File teamSpeakFile = new File("plugins/TeamSpeak", "teamspeak.yml");
	private static FileConfiguration teamSpeakCfg = YamlConfiguration.loadConfiguration(teamSpeakFile);
	
	private static File messageFile = new File("plugins/TeamSpeak", "messages.yml");
	private static FileConfiguration messages = YamlConfiguration.loadConfiguration(messageFile);
	  
	private static String prefix = "";
	private static String nopermission = "";
	private static String console = "";
	private static int rank = 0;
	
	public static void setPrefix(String prefix) {
		Utils.prefix = prefix;
	}
	
	public static void setNopermission(String nopermission) {
		Utils.nopermission = nopermission;
	}
	
	public static void setConsole(String console) {
		Utils.console = console;
	}
	
	public static void setRank(int rank) {
		Utils.rank = rank;
	}
	
	public static String getConsole() {
		return console;
	}
	
	public static File getMessageFile() {
		return messageFile;
	}
	
	public static FileConfiguration getMessages() {
		return messages;
	}
	
	public static String getNopermission() {
		return nopermission;
	}
	
	public static String getPrefix() {
		return prefix;
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