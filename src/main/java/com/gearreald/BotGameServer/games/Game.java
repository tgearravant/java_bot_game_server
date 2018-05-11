package com.gearreald.BotGameServer.games;

import java.util.List;

import org.json.JSONObject;

import com.gearreald.BotGameServer.games.hearts.Hearts;
import com.gearreald.BotGameServer.server.objects.Match;
import com.gearreald.BotGameServer.server.objects.Player;

/**
 * The generic interface that any new game should implement.
 * To add a new game, implement the interface,
 * and then add the game to the static method getUninitializedInstanceOfGame at the bottom of the class.
 * @author Tull Gearreald
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
	 * @return An instance of ActionResult containing the response. Should only be an error if no action was taken.
	 */
	public ActionResult takeAction(Action action);
	/**
	 * Populate an instance of the game from the JSONObject.
	 * It will receive something in the same format as getGameState() returns. 
	 * @param json The JSON object containing the game state.
	 */
	public void fromJSON(JSONObject json);
	/**
	 * Initialize a game for the players in the list.
	 * This should put the game in a state where it is ready to receive the initial action from the starting player.
	 * @param p The list of players who will be playing the game.
	 */
	public void initialize(List<Player> p);
	/**
	 * This should return the number of players that are required for this game.
	 * @return The minimum number of players.
	 */
	public int getRequiredPlayers();
	/**
	 * This method should return the name of the game in a string.
	 * @return The name of the game. This will be saved in the database.
	 * Should be exactly the same as the string in the getUninitializedInstanceOfGame method below.
	 */
	public String getName();
	/**
	 * This should return true if the game is completed.
	 * If this is true, there should be no more valid actions.
	 * @return True if the game is over. False otherwise.
	 */
	public boolean isCompleted();
	/**
	 * Get the player who won the game.
	 * This should be null unless the isCompleted function returns true.
	 * @return The player who won the game or null if the game is still in progress.
	 */
	public Player getWinningPlayer();
	/**
	 * This will be called once upon the last action of the game, after it is completed.
	 * It will populate the 'info' column on the final statistics table.
	 * It can contain any information you think is relevant to the final statistics.
	 * @return A JSONObject containing the final statistics.
	 */
	public JSONObject getFinalStatistics();
	/**
	 * Gets the number of players required to play the game.
	 * Should not need to be edited to add a new game.
	 * @param name The name of the game.
	 * @return An int with the required players of the game.
	 * Will be 10000000 if the game does not exist.
	 */
	public static int getRequiredPlayersOfGame(String name){
		Game g = getUninitializedInstanceOfGame(name);
		if (g == null)
			return 10000000;
		return g.getRequiredPlayers();
	}
	/**
	 * Gets and initializes an instance of the given game with the given players.
	 * Should not need to be edited to add a new game.
	 * @param name The name of the game.
	 * @param players The list of people who will be playing the game.
	 * @return The initialized game.
	 */
	public static Game getInitializedInstanceOfGame(String name, List<Player> players){
		Game g = getUninitializedInstanceOfGame(name);
		if(g == null)
			return null;
		g.initialize(players);
		return g;
	}
	/**
	 * Gets an instance of the game from a Match object. Will set it to the state from the DB.
	 * Should not need to be edited to add a new game.
	 * @param m The match containing the game state.
	 * @return The current game based on the match.
	 */
	public static Game getGameFromMatch(Match m){
		Game g = getUninitializedInstanceOfGame(m.getGameName());
		if(g == null)
			return null;
		g.fromJSON(new JSONObject(m.getGameState()));
		return g;
	}
	/**
	 * This method gets an uninitialized instance of a game.
	 * If a new game is added, this method should be updated to include it.
	 * This is the only method that will need to be updated.
	 * @param name The name of the game. Should match the getName method.
	 * @return
	 */
	public static Game getUninitializedInstanceOfGame(String name) {
		if(name.equals("hearts"))
			return new Hearts();
		else
			return null;
	}
}
