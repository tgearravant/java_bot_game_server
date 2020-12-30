package com.gearreald.BotGameServer.server.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.json.JSONObject;

import com.gearreald.BotGameServer.games.Game;
import com.gearreald.BotGameServer.server.SQLConnection;

import net.tullco.tullutils.SQLUtil;

public class Match {
	private final int id;
	private final String gameName;
	private String gameState;
	private final  LocalDateTime updatedTime;

	private final int TIMEOUT = 60;
	
	public static Match createMatch(Game game) throws SQLException{
		SQLUtil sql = SQLConnection.getConnection();
		int newMatchId = sql.executeInsert(String.format("INSERT INTO matches (game_name, game_state, updated_time) VALUES ('%s', '%s', '%s')", game.getName(), game.getGameState(), LocalDateTime.now().toString()));
		return getMatchById(newMatchId);
	}
	public static Match getMatchById(int id) throws SQLException{
		SQLUtil sql = SQLConnection.getConnection();
		ResultSet rs = sql.executeSelect(String.format("SELECT * FROM matches WHERE id = %d", id));
		if(!rs.isBeforeFirst())
			return null;
		rs.next();
		return new Match(rs);
	}
	
	public Match(ResultSet rs) throws SQLException{
		this.id = rs.getInt("id");
		this.gameName = rs.getString("game_name");
		this.gameState = rs.getString("game_state");
		this.updatedTime = LocalDateTime.parse(rs.getString("updated_time"));
	}
	public void save() throws SQLException{
		SQLUtil sql = SQLConnection.getConnection();
		sql.executeUpdate(String.format("UPDATE matches SET game_state = '%s', updated_time = '%s' WHERE id = %d", this.gameState, LocalDateTime.now().toString(), this.id));
	}

	public boolean isTimedOut(){
		long timeSinceLastMove = Duration.between(this.updatedTime, LocalDateTime.now()).getSeconds();
		return timeSinceLastMove > TIMEOUT;
	}

	public String getGameState() {
		return gameState;
	}
	
	public JSONObject getGameStateJSON() {
		return new JSONObject(gameState);
	}

	public void setGameState(String gameState) {
		this.gameState = gameState;
	}

	public int getId() {
		return id;
	}

	public String getGameName() {
		return gameName;
	}
	
}
