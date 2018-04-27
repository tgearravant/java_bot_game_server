package com.gearreald.BotGameServer.server.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gearreald.BotGameServer.utils.SQLConnection;

import net.tullco.tullutils.SQLUtil;

public class MatchRequest {

	private Integer id;
	private String playerUUID;
	private Integer botId;
	private Boolean satisfied;
	private Integer matchId;
	private String gameName;
	
	public static MatchRequest createMatchRequestFor(String playerUUID, int botId, String gameName) throws SQLException{
		SQLUtil sql = SQLConnection.getConnection();
		MatchRequest existingMatchRequest = getMatchRequestByPlayer(playerUUID, gameName); 
		if(existingMatchRequest != null)
			return existingMatchRequest;
		int newId = sql.executeInsert(String.format("INSERT INTO match_requests (player_uuid, bot_id, satisfied, match_id, game_name) VALUES ('%s', %d, 0, null, '%s')", playerUUID, botId, gameName));
		return getMatchRequestById(newId);
	}
	public MatchRequest(ResultSet rs) throws SQLException{
		this.id = rs.getInt("id");
		this.playerUUID = rs.getString("player_uuid");
		this.botId = rs.getInt("bot_id");
		this.satisfied = (rs.getInt("satisfied") == 0 ? false : true);
		this.matchId = rs.getInt("match_id");
		this.gameName = rs.getString("game_name");
	}
	public static MatchRequest getMatchRequestById(int id) throws SQLException{
		SQLUtil sql = SQLConnection.getConnection();
		ResultSet rs = sql.executeSelect(String.format("SELECT * FROM match_requests WHERE id = %d", id));
		if(!rs.isBeforeFirst())
			return null;
		rs.next();
		return new MatchRequest(rs);
	}
	public static  MatchRequest getMatchRequestByPlayer(String playerUUID, String gameName) throws SQLException{
		SQLUtil sql = SQLConnection.getConnection();
		ResultSet rs = sql.executeSelect(String.format("SELECT * FROM match_requests WHERE player_uuid = '%s' AND game_name = '%s'", playerUUID, gameName));
		if(!rs.isBeforeFirst())
			return null;
		rs.next();
		return new MatchRequest(rs);
	}
	public static List<MatchRequest> getMatchRequestsForGame(String gameName) throws SQLException{
		SQLUtil sql = SQLConnection.getConnection();
		List<MatchRequest> matchRequests = new ArrayList<MatchRequest>();
		ResultSet rs = sql.executeSelect(String.format("SELECT * FROM match_requests WHERE game_name = '%s'", gameName));
		while(rs.next())
			matchRequests.add(new MatchRequest(rs));
		return matchRequests;
	}
	public void save() throws SQLException{
		SQLUtil sql = SQLConnection.getConnection();
		sql.executeUpdate(String.format("UPDATE match_requests SET player_uuid = '%s', satisfied = %d, match_id = %d WHERE id = %d", this.playerUUID, (this.satisfied?1:0), this.matchId, this.id));
	}
	public Integer getBotId() {
		return botId;
	}
	public void setBotId(Integer botId) {
		this.botId = botId;
	}
	public Boolean getSatisfied() {
		return satisfied;
	}
	public void setSatisfied(Boolean satisfied) {
		this.satisfied = satisfied;
	}
	public Integer getMatchId() {
		return matchId;
	}
	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}
	public Integer getId() {
		return id;
	}
	public String getPlayerUUID() {
		return playerUUID;
	}
	public String getGameName() {
		return gameName;
	}
}
