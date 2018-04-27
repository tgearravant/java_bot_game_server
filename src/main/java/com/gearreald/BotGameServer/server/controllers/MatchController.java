package com.gearreald.BotGameServer.server.controllers;

import org.json.JSONObject;

import com.gearreald.BotGameServer.server.objects.Match;

import spark.Request;
import spark.Response;
import spark.Route;

public class MatchController {
	public static Route matchGet = (Request request, Response response) -> {
		JSONObject jsonResponse = new JSONObject();
		if(!request.queryParams().contains("player_uuid")
				|| !request.queryParams().contains("match_id")){
			jsonResponse.put("message", "missing required parameters").put("request_status", "error");
		}else{
			int matchId = Integer.parseInt(request.queryParams("match_id"));
			int playerUUID = Integer.parseInt(request.queryParams("player_uuid"));
			Match match = Match.getMatchById(matchId);
			if(match == null){
				jsonResponse.put("request_status", "error").put("message", "match does not exist");
			}else{
				jsonResponse.put("request_status", "success");
				//JSONObject gameState = JSONObject(match.getGameState());
				//jsonResponse.put("message", gameState);
			}
		}
		return jsonResponse;
	};
}
