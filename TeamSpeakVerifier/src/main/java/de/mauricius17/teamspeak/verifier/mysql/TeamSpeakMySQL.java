package de.mauricius17.teamspeak.verifier.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class TeamSpeakMySQL {

	public static void addIdentity(final String uuid, final String identity) {
		MySQL.getExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				getIdentitys(uuid, new Consumer<String>() {

					@Override
					public void accept(String result) {
						try {
							if(result.equalsIgnoreCase("wrong")) {
								String qry  = "INSERT INTO " + MySQL.getTABLE() + " (playeruuid, " + MySQL.getIDENTITIES() + ") VALUES (?,?)";
								PreparedStatement stmt = MySQL.getConnection().prepareStatement(qry);
								stmt.setString(1, uuid);
								stmt.setString(2, identity + ";");
								stmt.executeUpdate();
								stmt.close();
							} else {
								String qry  = "UPDATE " + MySQL.getTABLE() + " SET " + MySQL.getIDENTITIES() + " = ? WHERE playeruuid = ?";
								PreparedStatement stmt = MySQL.getConnection().prepareStatement(qry);
								stmt.setString(1, result + identity + ";");
								stmt.setString(2, uuid);
								stmt.executeUpdate();
								stmt.close();
							}
						} catch(SQLException ex) {
							ex.printStackTrace();
						}
					}
				});
			}
		});
	}
	
	public static void removeIdentity(final String uuid, final String identity) {
		MySQL.getExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				getIdentitys(uuid, new Consumer<String>() {

					@Override
					public void accept(String result) {
						try {
							if(!result.equalsIgnoreCase("wrong")) {
								String[] identities = result.split(";");
								StringBuilder text = new StringBuilder();
								
								for(int i = 0; i < identities.length; i++) {
									if(identity.equals(identities[i])) continue;
									text.append(identities[i]).append(";");
								}
								
								String qry  = "UPDATE " + MySQL.getTABLE() + " SET " + MySQL.getIDENTITIES() + " = ? WHERE playeruuid = ?";
								PreparedStatement stmt = MySQL.getConnection().prepareStatement(qry);
								stmt.setString(1, text.toString());
								stmt.setString(2, uuid);
								stmt.executeUpdate();
								stmt.close();
							}
						} catch(SQLException ex) {
							ex.printStackTrace();
						}
					}
				});
			}
		});
	}
	
	public static void getIdentitys(final String uuid, final Consumer<String> consumer) {
		MySQL.getExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT " + MySQL.getIDENTITIES() + " FROM " + MySQL.getTABLE() + " WHERE playeruuid = '" + uuid + "'");
					ResultSet rs = stmt.executeQuery();
					
					if(rs.next()) {
						consumer.accept(rs.getString(MySQL.getIDENTITIES()));
					} else {
						consumer.accept("wrong");
					}
					
					rs.close();
					stmt.close();
				} catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
	}
}
