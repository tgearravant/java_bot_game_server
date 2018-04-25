package com.gearreald.BotGameServer;

import static spark.Spark.*;
import spark.servlet.SparkApplication;

public class Server {
	public static void staticLaunch(){
		
	}
	public void instanceLaunch(){
		SQLUtils.runMigrations();
		SystemUtils.checkForRequiredProperties();
		if(!SystemUtils.inTesting())
			UserController.createAdmin(SystemUtils.getProperty("admin_username"), SystemUtils.getProperty("admin_password"));
		staticFiles.location("/public");
		port(Integer.parseInt(SystemUtils.getProperty("port", "12345")));
	}
}
