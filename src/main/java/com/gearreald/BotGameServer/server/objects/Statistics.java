package com.gearreald.BotGameServer.server.objects;

import java.sql.SQLException;

import com.gearreald.BotGameServer.games.Game;
import com.gearreald.BotGameServer.server.SQLConnection;

import net.tullco.tullutils.SQLUtil;

public class Statistics {
//	private int id;
//	private String gameName;
//	private int matchId;
//	private int winningBotId;
//	private String winningPlayerUuid;
//	private JSONObject info;
	
	public static void createStatistics(int matchId, Game g) throws SQLException {
		SQLUtil sql = SQLConnection.getConnection();
		String gameName = g.getName();
		int winningBotId = g.getWinningPlayer().getBotID();
		String winningPlayerUuid = g.getWinningPlayer().getUUID();
		String info = g.getFinalStatistics().toString();
		String statement = "INSERT INTO statistics (game_name, match_id, winning_bot_id, winning_player_uuid, info) VALUES ('%s', %d, %d, '%s', '%s')";
		sql.executeInsert(
				String.format(
						statement, gameName, matchId, winningBotId, winningPlayerUuid, info 
						));
	}
}
