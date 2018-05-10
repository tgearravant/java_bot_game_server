package com.gearreald.BotGameServer.games;

import org.json.JSONObject;

public class ActionResult {

	private final boolean error;
	private final String response;
	public ActionResult(boolean error, String response) {
		this.error = error;
		this.response = response;
	}
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		if(error) {
			json.put("action_result", "error");
			json.put("error_message", this.response);
		} else {
			json.put("action result", "success");
			json.put("action_message", this.response);
		}
		return json;
	}
}
