package com.gearreald.BotGameServer.games.hearts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.gearreald.BotGameServer.Action;
import com.gearreald.BotGameServer.Player;
import com.gearreald.BotGameServer.games.Game;
import com.gearreald.BotGameServer.utils.JSONUtils;

import net.tullco.tullutils.Pair;

public class Hearts implements Game {

	private static final int REQUIRED_PLAYERS = 4;
	private static final int MAX_POINTS = 100;
	
	private static final List<String> PASSING_STAGES = Arrays.asList("left", "right","across","none");
	
	private String stage;
	private String passingStage;
	private List<Player> players;
	private Player currentPlayer;
	private Player winningPlayer;
	private Map<String, Card> cardsOnTable;
	private Map<String, List<Card>> hands;
	private Map<String, List<Card>> wonCards;
	private List<Map<String, Card>> previousRounds;
	private Map<String, Integer> pointCounts;
	
	@Override
	public JSONObject getGameState() {
		JSONObject json = new JSONObject();
		json.put("stage", stage)
			.put("current_player", currentPlayer.getUUID())
			.put("cards_on_table", JSONUtils.jsonMapToJSON(cardsOnTable))
			.put("winning_player", winningPlayer)
			.put("passing_stage", passingStage)
			.put("players", JSONUtils.listToJSON(this.players))
			.put("hands", JSONUtils.jsonableListMapToJSON(this.hands))
			.put("won_cards", JSONUtils.jsonableListMapToJSON(this.wonCards))
			.put("previous_rounds", JSONUtils.jsonableMapListToJSON(this.previousRounds))
			.put("point_counts", JSONUtils.intMapToJSON(pointCounts));
		return json;
	}

	@Override
	public JSONObject getGameStateAsPlayer(Player p) {
		JSONObject gameState = getGameState();
		JSONObject playerHand = new JSONObject();
		playerHand.put(p.getUUID(), JSONUtils.listToJSON(getPlayerHand(p)));
		gameState.put("hands", playerHand);
		if(stage.equals("pass"))
			gameState.remove("cards_on_table");
		return gameState;
	}
	public boolean passCards() {
		if(!stage.equals("pass") || cardsOnTable.size() != REQUIRED_PLAYERS)
			return false;
		List<Pair<String, String>> passingOrders = new ArrayList<Pair<String, String>>();
		if (passingStage.equals("left")){
			passingOrders.add(Pair.of(players.get(0).getUUID(), players.get(1).getUUID()));
			passingOrders.add(Pair.of(players.get(1).getUUID(), players.get(2).getUUID()));
			passingOrders.add(Pair.of(players.get(2).getUUID(), players.get(3).getUUID()));
			passingOrders.add(Pair.of(players.get(3).getUUID(), players.get(0).getUUID()));
		}else if (passingStage.equals("right")){
			passingOrders.add(Pair.of(players.get(0).getUUID(), players.get(3).getUUID()));
			passingOrders.add(Pair.of(players.get(1).getUUID(), players.get(0).getUUID()));
			passingOrders.add(Pair.of(players.get(2).getUUID(), players.get(1).getUUID()));
			passingOrders.add(Pair.of(players.get(3).getUUID(), players.get(2).getUUID()));
		}else if (passingStage.equals("right")){
			passingOrders.add(Pair.of(players.get(0).getUUID(), players.get(2).getUUID()));
			passingOrders.add(Pair.of(players.get(1).getUUID(), players.get(3).getUUID()));
			passingOrders.add(Pair.of(players.get(2).getUUID(), players.get(0).getUUID()));
			passingOrders.add(Pair.of(players.get(3).getUUID(), players.get(1).getUUID()));
		}else{
			return false;
		}
		for(String playerUUID: cardsOnTable.keySet()){
			Pair<String, String> passPatternForPlayer = null;
			for(Pair<String,String> passingOrder: passingOrders){
				if(passingOrder.left().equals(playerUUID)){
					passPatternForPlayer = passingOrder;
					break;
				}
			}
			if (passPatternForPlayer == null)
				return false;
			List<Card> receivingHand = hands.get(passPatternForPlayer.right()); 
			receivingHand.add(cardsOnTable.get(playerUUID));
		}
		cardsOnTable.clear();
		return true;
	}
	public List<Card> getPlayerHand(Player p){
		String playerUUID = p.getUUID();
		for(String handUUID: hands.keySet()){
			if(handUUID == playerUUID){
				return hands.get(handUUID); 
			}
		}
		return null;
	}
	private void endRound(){
		for(String playerUUID: wonCards.keySet()){
			int earnedPoints = 0;
			for(Card c: wonCards.get(playerUUID)){
				if(c.getSuit().equals("hearts"))
					earnedPoints++;
				if(c.getSuit().equals("spades") && c.getValue() == 12){
					earnedPoints+=13;
				}
			}
			pointCounts.put(playerUUID, pointCounts.get(playerUUID) + earnedPoints);
		}
		stage = "pass";
		progressPassingStage();
	}
	
	private void progressPassingStage(){
		int currentIndex = PASSING_STAGES.indexOf(passingStage);
		currentIndex++;
		currentIndex = currentIndex % 4;
		passingStage = PASSING_STAGES.get(currentIndex);
	}

	@Override
	public JSONObject takeAction(Action action) {
		Player player = action.getPlayer();
		Card cardToPlay = Card.fromJSON(action.getAction());
		List<Card> playerHand = getPlayerHand(player);
		JSONObject actionResult = new JSONObject();
		if(!playerHand.contains(cardToPlay)){
			return actionResult.put("action_result", "error").put("error_message", "player doesn't have the specified card");
		}
		if(!currentPlayer.equals(player)){
			return actionResult.put("action_result", "error").put("error_message", "it's not this player's turn");
		}
		if(winningPlayer != null){
			return actionResult.put("action_result", "error").put("error_message", "the game is already over");
		}
		playerHand.remove(cardToPlay);
		cardsOnTable.put(player.getUUID(), cardToPlay);
		if (stage.equals("pass")){
			if(cardsOnTable.size() == REQUIRED_PLAYERS){
				passCards();
				stage = "play";
			}
		}
		else{
			if(cardsOnTable.size() == REQUIRED_PLAYERS){
				resolveTrick();
			}else{
				int nextPlayerIndex = players.indexOf(currentPlayer) + 1;
				currentPlayer = players.get(nextPlayerIndex);
			}
			int remainingCards = cardsRemainingInHands();
			if (remainingCards==0){
				endRound();
			}
			boolean gameCompleted = false;
			for(String uuid: pointCounts.keySet()){
				if(pointCounts.get(uuid) >= MAX_POINTS)
					gameCompleted = true;
			}
			if(gameCompleted){
				int minPoints = 100000;
				String winningUUID = "";
				for(String uuid: pointCounts.keySet()){
					int playerPoints = pointCounts.get(uuid);
					if(playerPoints < minPoints){
						minPoints = playerPoints;
						winningUUID = uuid;
					}
				}
				winningPlayer = getPlayerByUUID(winningUUID);
			}
		}
		return actionResult.put("action_result", "success");
	}

	private int cardsRemainingInHands(){
		int cardsRemaining = 0;
		for(String playerUUID: hands.keySet()){
			cardsRemaining+=hands.get(playerUUID).size();
		}
		return cardsRemaining;
	}
	private Player getPlayerByUUID(String uuid){
		for(Player p: players){
			if(p.getUUID().equals(uuid)){
				return p;
			}
		}
		return null;
	}
	private void resolveTrick(){
		Player leadingPlayer = this.players.get((players.indexOf(currentPlayer) + 1) % 4);
		Card leadingCard = cardsOnTable.get(leadingPlayer.getUUID());
		Card winningCard = leadingCard;
		Player winningPlayer = leadingPlayer;
		for(String playerUUID: cardsOnTable.keySet()){
			Card tableCard = cardsOnTable.get(playerUUID);
			
			if(!tableCard.getSuit().equals(leadingCard.getSuit()))
				continue;
			
			if(tableCard.getValue() > winningCard.getValue()){
				winningCard = tableCard;
				winningPlayer = getPlayerByUUID(playerUUID);
			}
		}
		List<Card> winningPlayerPile = wonCards.get(winningPlayer.getUUID());
		cardsOnTable.forEach((String s, Card c) -> {winningPlayerPile.add(c);});
		cardsOnTable.clear();
		currentPlayer = winningPlayer;
	}
	
	@Override
	public void fromJSON(JSONObject json) {
	}

	@Override
	public void initialize(List<Player> players) {
		this.stage = "pass";
		this.winningPlayer = null;
		this.passingStage = "left";
		this.hands = new HashMap<String, List<Card>>();
		this.wonCards = new HashMap<String, List<Card>>();
		this.previousRounds = new ArrayList<Map<String, Card>>();
		this.pointCounts = new HashMap<String, Integer>();
		for(Player p: players){
			this.players.add(p);
			this.hands.put(p.getUUID(), new ArrayList<Card>());
			this.wonCards.put(p.getUUID(), new ArrayList<Card>());
			this.pointCounts.put(p.getUUID(), 0);
		}
	}

	@Override
	public int getRequiredPlayers() {
		return REQUIRED_PLAYERS;
	}
}
