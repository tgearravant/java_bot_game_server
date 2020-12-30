package com.gearreald.BotGameServer.server.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.json.JSONObject;

import com.gearreald.BotGameServer.games.Action;
import com.gearreald.BotGameServer.games.ActionResult;
import com.gearreald.BotGameServer.games.Game;
import com.gearreald.BotGameServer.server.objects.Match;
import com.gearreald.BotGameServer.server.objects.Statistics;

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
			String playerUUID = request.queryParams("player_uuid");
			Match match = Match.getMatchById(matchId);
			if(match == null){
				jsonResponse.put("request_status", "error").put("message", "match does not exist");
			}else if (match.isTimedOut()){
				return jsonResponse.put("request_status", "error").put("message", "match has timed out");
			}else{
				Game g = Game.getGameFromMatch(match);
				JSONObject gameState = g.getGameStateAsPlayer(playerUUID);
				if(gameState == null) {
					return jsonResponse.put("request_status", "error").put("message", "Could not get a game state for that player. Does that player exist in this match?");
				}

				jsonResponse.put("request_status", "success").put("message", gameState);
			}
		}
		return jsonResponse;
	};
	
	public static Route takeAction = (Request request, Response response) -> {
		JSONObject jsonResponse = new JSONObject();
		try{
			JSONObject requestJSON = new JSONObject(request.body());
			if(!request.queryParams().contains("match_id")&& !request.queryParams().contains("player_uuid")){
				jsonResponse.put("message", "missing required parameters").put("request_status", "error");
			}else{
				int matchId = Integer.parseInt(request.queryParams("match_id"));
				String playerUuid = request.queryParams("player_uuid");
				Action a = Action.fromJSON(requestJSON, playerUuid);
				Match match = Match.getMatchById(matchId);
				if(match == null){
					return jsonResponse.put("request_status", "error").put("message", "match does not exist");
				}else{
					jsonResponse.put("request_status", "success");
					Game g = Game.getGameFromMatch(match);
					ActionResult actionResult = g.takeAction(a);
					jsonResponse.put("message", actionResult.toJSON());
					match.setGameState(g.getGameState().toString());
					match.save();
					if(g.isCompleted()) {
						Statistics.createStatistics(matchId, g);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(jsonResponse.toString());
			System.out.println(request.body());
			jsonResponse.put("request_status", "error").put("message", sw.toString());
		}
		return jsonResponse;
	};
}
