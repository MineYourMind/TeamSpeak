package de.mauricius17.teamspeak.verifier.utils;

public enum Messages {

	PREFIX("", "teamspeak.prefix"),
	CONSOLE("", "teamspeak.console"),
	NOPERMISSION("", "teamspeak.nopermission"),
	
	COMMAND_TEAMSPEAK_HELP_HEADER("", "command.teamspeak.help.header"),
	COMMAND_TEAMSPEAK_HELP_LIST("", "command.teamspeak.help.list"),
	COMMAND_TEAMSPEAK_HELP_ADD("", "command.teamspeak.help.add"),
	COMMAND_TEAMSPEAK_HELP_REMOVE("", "command.teamspeak.help.remove"),
	COMMAND_TEAMSPEAK_HELP_FOOTER("", "command.teamspeak.help.footer"),
	
	COMMAND_TEAMSPEAK_SPAM("", "command.teamspeak.spam"),
	COMMAND_TEAMSPEAK_CLIENTNULL("", "command.teamspeak.client_null"),

	IDENTITY_ADD_SUCCESS("", "identity.add.success"),
	IDENTITY_ADD_FAILED("", "identity.add.failed"),
	IDENTITY_ADD_TOMUCHIDS("", "identity.add.tomuchids"),
	
	IDENTITY_REMOVE_SUCCESS("", "identity.remove.success"),
	IDENTITY_REMOVE_FAILED("", "identity.remove.failed"),
	
	IDENTITY_LIST_FAILED("", "identities.list.failed"),
	IDENTITY_LIST_HEADER("", "identities.list.header"),
	IDENTITY_LIST_MESSAGE("", "identities.list.message"),
	IDENTITY_LIST_LIST("", "identities.list.list"),
	IDENTITY_LIST_FOOTER("", "identities.list.footer");
	
	private String message;
	private String path;
	
	private Messages(String message, String path) {
		this.message = message;
		this.path = path;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getMessage() {
		return message;
	}
	
	
	public String getPath() {
		return path;
	}
}
