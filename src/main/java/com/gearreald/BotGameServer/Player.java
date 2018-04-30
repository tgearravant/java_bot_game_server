package com.gearreald.BotGameServer;

import java.util.UUID;

import org.json.JSONObject;

import com.gearreald.BotGameServer.utils.JSONable;

public class Player implements JSONable {
	private final String uuid;
	private final int botId;
	public Player(String uuid, int botId){
		this.uuid=uuid;
		this.botId=botId;
	}
	public Player(int botId){
		this(UUID.randomUUID().toString(), botId);
	}
	public String getUUID(){
		return this.uuid;
	}
	public int getBotID(){
		return this.botId;
	}
	public JSONObject toJSON(){
		JSONObject json = new JSONObject();
		json.put("uuid", this.uuid);
		json.put("bot_id", this.botId);
		return json;
	}
	@Override
	public String toString(){
		return uuid;
	}
	public static Player fromJSON(JSONObject json){
		return new Player(json.getString("uuid"), json.getInt("bot_id"));
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + botId;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Player))
			return false;
		Player other = (Player) obj;
		if (botId != other.botId)
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

}
