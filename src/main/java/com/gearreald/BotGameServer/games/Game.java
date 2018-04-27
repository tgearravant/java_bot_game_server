package com.gearreald.BotGameServer.games;

import java.util.List;

import org.json.JSONObject;

import com.gearreald.BotGameServer.Action;
import com.gearreald.BotGameServer.Player;

public interface Game {
	public JSONObject getGameState();
	public JSONObject getGameStateAsPlayer(Player p);
	public JSONObject takeAction(Action action);
	public void fromJSON(JSONObject json);
	public void initialize(List<Player> p);
	public int getRequiredPlayers();
}
