package com.gearreald.BotGameServer.games;

import java.util.List;

import org.json.JSONObject;

import com.gearreald.BotGameServer.Action;
import com.gearreald.BotGameServer.Player;
import com.gearreald.BotGameServer.games.hearts.Hearts;

public interface Game {
	public JSONObject getGameState();
	public JSONObject getGameStateAsPlayer(Player p);
	public JSONObject takeAction(Action action);
	public void fromJSON(JSONObject json);
	public void initialize(List<Player> p);
	public int getRequiredPlayers();
	public String getName();
	public static int getRequiredPlayersOfGame(String name){
		if(name.equals("hearts")){
			Game hearts = new Hearts();
			return hearts.getRequiredPlayers();
		}
		return 10000000;
	}
	public static Game getInstanceOfGame(String name, List<Player> players){
		if(name.equals("hearts")){
			Game hearts = new Hearts();
			hearts.initialize(players);
			return hearts;
		}else{
			return null;
		}
	}
}
