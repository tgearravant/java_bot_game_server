package com.gearreald.BotGameServer.server.controllers;

import org.json.JSONObject;

import spark.Request;
import spark.Response;
import spark.Route;

public class MatchMakingController {
	public static Route matchMakingPost = (Request request, Response response) -> {
		JSONObject jsonResponse = new JSONObject();
		int botId = Integer.parseInt(request.queryParams("bot_id"));
		String playerUUID = request.queryParams("player_uuid");
		
		jsonResponse.put("request_status", "success");
		jsonResponse.put("method", "value");
		return response;
	};
	
	public static Route matchMakingGet = (Request request, Response response) -> {
		JSONObject jsonResponse = new JSONObject();
		int botId = Integer.parseInt(request.queryParams("bot_id"));
		String playerUUID = request.queryParams("player_uuid");
		jsonResponse.put("request_status", "success");
		jsonResponse.put("method", "value");
		return response;
		
	};
}
