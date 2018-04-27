package com.gearreald.BotGameServer;

import org.json.JSONObject;

public class Action {
	private final Player player;
	private final JSONObject action;
	public Action(Player player, JSONObject action){
		this.player = player;
		this.action = action;
	}
	public static Action fromJSON(JSONObject json){
		Player p = Player.fromJSON(json.getJSONObject("player"));
		return new Action(p, json.getJSONObject("action"));
	}
	public Player getPlayer() {
		return player;
	}
	public JSONObject getAction() {
		return action;
	}
}