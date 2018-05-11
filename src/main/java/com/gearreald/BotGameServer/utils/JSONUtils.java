package com.gearreald.BotGameServer.utils;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gearreald.BotGameServer.games.hearts.Card;

import net.tullco.tullutils.Pair;

public class JSONUtils {
	public static JSONArray listToJSON(List<? extends JSONable> jsonables){
		JSONArray array = new JSONArray();
		for(JSONable jsonable: jsonables){
			array.put(jsonable.toJSON());
		}
		return array;
	}
	public static JSONObject jsonMapToJSON(Map<String, ? extends JSONable> jsonables){
		JSONObject json = new JSONObject();
		for(String key: jsonables.keySet()){
			json.put(key, jsonables.get(key).toJSON());
		}
		return json;
	}
	public static JSONObject jsonableListMapToJSON(Map<String, ? extends List<? extends JSONable>> hands){
		JSONObject json = new JSONObject();
		for(String key: hands.keySet()){
			json.put(key, listToJSON(hands.get(key)));
		}
		return json;
	}
	public static JSONArray jsonableMapListToJSON(List<? extends Map<String, ? extends JSONable>> mapList){
		JSONArray array = new JSONArray();
		for(Map<String, ? extends JSONable> map: mapList){
			array.put(jsonMapToJSON(map));
		}
		return array;
	}
	public static JSONObject intMapToJSON(Map<String, Integer> map){
		JSONObject json = new JSONObject();
		for(String key: map.keySet()){
			json.put(key, map.get(key).intValue());
		}
		return json;	
	}
	public static JSONArray pairListToJSON(String pairKeyJsonKey, String pairValueJsonKey, List<Pair<String, Card>> pairList) {
		JSONArray json = new JSONArray();
		for(Pair<String, Card> pair: pairList) {
			JSONObject pairJson = new JSONObject();
			pairJson.put(pairKeyJsonKey, pair.getKey());
			pairJson.put(pairValueJsonKey, pair.getValue().toJSON());
			json.put(pairJson);
		}
		return json;
	}
}
