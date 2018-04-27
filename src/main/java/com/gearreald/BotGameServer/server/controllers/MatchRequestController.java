package com.gearreald.BotGameServer.server.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.gearreald.BotGameServer.Player;
import com.gearreald.BotGameServer.games.Game;
import com.gearreald.BotGameServer.server.objects.Match;
import com.gearreald.BotGameServer.server.objects.MatchRequest;

import spark.Request;
import spark.Response;
import spark.Route;

public class MatchRequestController {
	public static Route matchRequestPost = (Request request, Response response) -> {
		JSONObject jsonResponse = new JSONObject();
		int botId = Integer.parseInt(request.queryParams("bot_id"));
		String playerUUID = request.queryParams("player_uuid");
		String gameName = request.queryParams("game_name");
		MatchRequest.createMatchRequestFor(playerUUID, botId, gameName);
		jsonResponse.put("request_status", "success");
		jsonResponse.put("message", "request queued");
		List<MatchRequest> currentMatchRequests = MatchRequest.getMatchRequestsForGame(gameName);
		int neededPlayers = Game.getRequiredPlayersOfGame(gameName);
		if(neededPlayers <= currentMatchRequests.size()){
			List<Player> players = new ArrayList<Player>();
			for(int i = 0; i < neededPlayers; i++){
				MatchRequest matchRequest = currentMatchRequests.get(i);
				players.add(new Player(matchRequest.getPlayerUUID(), matchRequest.getBotId()));
			}
			Game g = Game.getInstanceOfGame("hearts", players);
			Match.createMatch(g);
		}
		return jsonResponse;
	};
	
	public static Route matchRequestGet = (Request request, Response response) -> {
		JSONObject jsonResponse = new JSONObject();
		String playerUUID = request.queryParams("player_uuid");
		String gameName = request.queryParams("hearts");
		MatchRequest matchRequest = MatchRequest.getMatchRequestByPlayer(playerUUID, gameName);
		if(matchRequest == null){
			jsonResponse.put("request_status", "error").put("message", "there is no match request for that player and game");
		}else{
			jsonResponse.put("request_status", "success");
			jsonResponse.put("message", new JSONObject().put("match_id", matchRequest.getMatchId()));
		}
		return jsonResponse;
		
	};
}
