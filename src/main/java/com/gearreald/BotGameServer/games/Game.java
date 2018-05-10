package com.gearreald.BotGameServer.games;

import java.util.List;

import org.json.JSONObject;

import com.gearreald.BotGameServer.games.hearts.Hearts;
import com.gearreald.BotGameServer.server.objects.Match;
import com.gearreald.BotGameServer.server.objects.Player;

/**
 * The generic interface that any new game should implement.
 * To add a new game, implement the interface, and then add the game to the static methods at the bottom of the class.
 * @author Budst
 *
 */
public interface Game {
	/**
	 * Get a JSONObject containing the game state. This is what will be stored in the database.
	 * @return
	 */
	public JSONObject getGameState();
	/**
	 * Get the game state that will be sent to the player when they request the game state.
	 * This should not contain anything that the player is not allowed to see.
	 * @param p The player who is requesting the game state.
	 * @return
	 */
	public JSONObject getGameStateAsPlayer(Player p);
	/**
	 * Get the game state that will be sent to the player when they request the game state.
	 * This should not contain anything that the player is not allowed to see.
	 * @param playerUUID The uuid of the player who is requesting the game state.
	 * @return
	 */
	public JSONObject getGameStateAsPlayer(String playerUUID);
	/**
	 * This method should process the action object, which is passed from the player.
	 * @param action The action that a player wants to take.
	 * @return The action result JSON that should contain an action_result and an action_
	 */
	public ActionResult takeAction(Action action);
	public void fromJSON(JSONObject json);
	public void initialize(List<Player> p);
	public int getRequiredPlayers();
	public String getName();
	public boolean isCompleted();
	public Player getWinningPlayer();
	public JSONObject getFinalStatistics();
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
	public static Game getGameFromMatch(Match m){
		String name = m.getGameName();
		if(name.equals("hearts")){
			Game hearts = new Hearts();
			hearts.fromJSON(new JSONObject(m.getGameState()));
			return hearts;
		}else{
			return null;
		}
	}
}
