package com.gearreald.BotGameServer.utils;

import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLNonTransientException;

import org.flywaydb.core.Flyway;

import net.tullco.tullutils.SQLUtil;

public class SQLConnection {
	private static Connection conn;
	
	private static String sqliteFileName = "bot_server.db";
	
	public static SQLUtil getConnection() throws SQLException{
		return new SQLUtil(getRawConnection());
	}
	private static Connection getRawConnection() throws SQLException{
		try {
			if (SQLConnection.conn==null || SQLConnection.conn.isClosed()){
				Connection c=null;
				try{
					Class.forName("org.sqlite.JDBC");
					c=DriverManager.getConnection("jdbc:sqlite:"+sqliteFileName);
				}catch(SQLException e){
					System.err.println("Could not connect to database for some reason...");
					e.printStackTrace();
				}catch(ClassNotFoundException e){
					System.err.println("No JDBC Driver");
					e.printStackTrace();
				}
				SQLConnection.conn=c;
			}
		} catch (SQLNonTransientException e) {
			e.printStackTrace();
		}
		return SQLConnection.conn;
	}
	public static boolean runMigrations(){
		Flyway flyway = new Flyway();
		flyway.setDataSource("jdbc:sqlite:"+sqliteFileName,"sa",null);
		//flyway.setLocations("C:/Users/tgearr34/eclipse/Work Personal/BotGameServer/bin/db/migration");
		flyway.setLocations("classpath:in/db/migration");
		System.out.println("Migrating...");
		for (String s:flyway.getLocations())
			System.out.println(s);
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader)cl).getURLs();
        for(URL url: urls){
        	System.out.println(url.getFile());
        }
		flyway.migrate();
		return true;
	}
	
	
}
