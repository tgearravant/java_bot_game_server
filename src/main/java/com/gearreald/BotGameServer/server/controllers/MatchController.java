package com.gearreald.BotGameServer.server.controllers;

import org.json.JSONObject;

import com.gearreald.BotGameServer.server.objects.Match;

import spark.Request;
import spark.Response;
import spark.Route;

public class MatchController {
	public static Route matchGet = (Request request, Response response) -> {
		JSONObject jsonResponse = new JSONObject();
		int matchId = Integer.parseInt(request.queryParams("match_id"));
		Match match = Match.getMatchById(matchId);
		if(match == null){
			jsonResponse.put("request_status", "error").put("message", "match does not exist");
		}else{
			jsonResponse.put("request_status", "success");
			JSONObject gameState = new JSONObject().put("game_state", match.getGameState());
			jsonResponse.put("message", gameState);
		}
		return jsonResponse;
	};
}
