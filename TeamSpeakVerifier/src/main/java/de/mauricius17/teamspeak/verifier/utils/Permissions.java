package de.mauricius17.teamspeak.verifier.utils;

public enum Permissions {

	TEAMSPEAK_IDS_LIST("teamspeak.verifier.list"),
	TEAMSPEAK_ID_ADD("teamspeak.verifier.add"),
	TEAMSPEAK_ID_REMOVE("teamspeak.verifier.remove");
	
	String permission;
	
	private Permissions(String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
}