package com.gearreald.BotGameServer.server.objects;

public class Match {
	private int id;
	private String playerUUID;
	private int botId;
	public Match(String playerUUID, int botId){
		this.playerUUID = playerUUID;
		this.botId = botId;
	}
}
