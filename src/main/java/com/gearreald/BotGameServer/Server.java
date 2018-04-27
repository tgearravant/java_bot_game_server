package com.gearreald.BotGameServer;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import com.gearreald.BotGameServer.server.controllers.MatchController;
import com.gearreald.BotGameServer.server.controllers.MatchRequestController;
import com.gearreald.BotGameServer.utils.Path;
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
    	System.out.print("Server Fully Configured");
	}
	public static void initialConfiguration(){
		SQLConnection.runMigrations();
		staticFiles.location("/public");
		port(12345);
		enableDebugScreen();
	}
	public static void getRouting(){
		//get(Path.MATCHMAKING, MatchRequestController.matchRequestGet);
		get(Path.GAME_STATUS, MatchController.matchGet);
	}
	public static void postRouting(){
		get(Path.MATCHMAKING, MatchRequestController.matchRequestPost);
	}
	public static void exit(){
		stop();
	}
}
