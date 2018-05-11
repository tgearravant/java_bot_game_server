package com.gearreald.BotGameServer.server.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.gearreald.BotGameServer.games.Game;
import com.gearreald.BotGameServer.server.objects.Match;
import com.gearreald.BotGameServer.server.objects.MatchRequest;
import com.gearreald.BotGameServer.server.objects.Player;

import spark.Request;
import spark.Response;
import spark.Route;

public class MatchRequestController {
	public static Route newMatchRequest= (Request request, Response response) -> {
		JSONObject jsonResponse = new JSONObject();
		if(!request.queryParams().contains("bot_id")
				|| !request.queryParams().contains("player_uuid")
				|| !request.queryParams().contains("game_name")
				){
			jsonResponse.put("message", "Missing Required Parameters").put("request_status", "error");
		}else{
			int botId = Integer.parseInt(request.queryParams("bot_id"));
			String playerUUID = request.queryParams("player_uuid");
			String gameName = request.queryParams("game_name");
			if(MatchRequest.getMatchRequestByPlayer(playerUUID, gameName) != null){
				jsonResponse.put("message", "You're already waiting for a game").put("request_status", "success");
			}else{
				MatchRequest.createMatchRequestFor(playerUUID, botId, gameName);
				jsonResponse.put("request_status", "success").put("message", "request queued");
				List<MatchRequest> currentMatchRequests = MatchRequest.getUnsatifiedMatchRequestsForGame(gameName);
				int neededPlayers = Game.getRequiredPlayersOfGame(gameName);
				if(neededPlayers <= currentMatchRequests.size()){
					List<Player> players = new ArrayList<Player>();
					for(int i = 0; i < neededPlayers; i++){
						MatchRequest matchRequest = currentMatchRequests.get(i);
						players.add(new Player(matchRequest.getPlayerUUID(), matchRequest.getBotId()));
					}
					Game g = Game.getInitializedInstanceOfGame("hearts", players);
					Match m = Match.createMatch(g);
					for(MatchRequest mr: currentMatchRequests){
						mr.setSatisfied(true);
						mr.setMatchId(m.getId());
						mr.save();
					}
				}
			}
		}
		return jsonResponse;
	};
	
	public static Route matchRequestGet = (Request request, Response response) -> {
		JSONObject jsonResponse = new JSONObject();
		if(!request.queryParams().contains("player_uuid")
				|| !request.queryParams().contains("game_name")){
			jsonResponse.put("message", "missing required parameters").put("request_status", "error");
		}else{
			String playerUUID = request.queryParams("player_uuid");
			String gameName = request.queryParams("game_name");
			MatchRequest matchRequest = MatchRequest.getMatchRequestByPlayer(playerUUID, gameName);
			if(matchRequest == null){
				jsonResponse.put("request_status", "error").put("message", "there is no match request for that player and game");
			}else{
				jsonResponse.put("request_status", "success");
				jsonResponse.put("message", new JSONObject().put("match_id", matchRequest.getMatchId()));
			}
		}
		return jsonResponse;
	};
}
