package com.gearreald.BotGameServer.games.hearts;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.gearreald.BotGameServer.utils.JSONable;

public class Card implements Comparable<Card>, JSONable {
	public static String[] SUITS = {"hearts", "spades", "clubs", "diamonds"};
	public static Card TWO_OF_CLUBS = new Card("clubs",1);
	private final String suit;
	private final int value;
	public Card(String suit, int value){
		this.suit = suit;
		this.value = value;
	}

	public int pointValue(){
		if(suit.equals("hearts"))
			return 1;
		if(suit.equals("spades") && value == 11)
			return 13;
		return 0;
	}

	public static List<Card> createDeck(){
		List<Card> deck = new ArrayList<Card>();
		for (int i=1; i <=13; i++){
			for(String suit: SUITS){
				deck.add(new Card(suit, i));
			}
		}
		return deck;
	}
	public static Card fromJSON(JSONObject json){
		return new Card(json.getString("suit"), json.getInt("value"));
	}
	@Override
	public int compareTo(Card o) {
		int numberCompare = Integer.compare(this.value, o.value);
		int suitCompare = this.suit.compareTo(o.suit);
		if (this.equals(o))
			return 0;
		if(suitCompare != 0){
			return suitCompare;
		}else{
			/*
			if(this.value == 1)
				return 1;
			if(o.value == 1)
				return -1;
			*/
			return numberCompare;
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		result = prime * result + value;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (suit == null) {
			if (other.suit != null)
				return false;
		} else if (!suit.equals(other.suit))
			return false;
		if (value != other.value)
			return false;
		return true;
	}
	public String getSuit() {
		return suit;
	}
	public int getValue() {
		return value;
	}
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("suit", this.suit);
		json.put("value", this.value);
		return json;
	}
	
}
