package com.gearreald.BotGameServer.games;

import org.json.JSONObject;

import com.gearreald.BotGameServer.server.objects.Player;

public class Action {
	private final Player player;
	private final JSONObject action;
	public Action(Player player, JSONObject action){
		this.player = player;
		this.action = action;
	}
	public static Action fromJSON(JSONObject json, String playerUuid){
		Player p = new Player(playerUuid, 0);
		return new Action(p, json.getJSONObject("action"));
	}
	public Player getPlayer() {
		return player;
	}
	public JSONObject getAction() {
		return action;
	}
}
