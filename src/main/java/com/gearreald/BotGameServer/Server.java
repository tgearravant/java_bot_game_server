package com.gearreald.BotGameServer;

import static spark.Spark.*;

import com.gearreald.BotGameServer.utils.SQLConnection;

import spark.servlet.SparkApplication;

public class Server implements SparkApplication {
	public static void staticLaunch(){
		configureInstance();
	}
	public void init(){
		configureInstance();
	}
	public static void configureInstance(){
		initialConfiguration();
    	getRouting();
    	postRouting();
	}
	public static void initialConfiguration(){
		SQLConnection.runMigrations();
		staticFiles.location("/public");
		port(12345);
	}
	public static void getRouting(){
		
	}
	public static void postRouting(){
		
	}
	public static void exit(){
		stop();
	}
}
