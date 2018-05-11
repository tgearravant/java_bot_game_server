# java_bot_game_server

This is a server using the Spark engine. It allows bots to play a game. The game implemented in this repository is 'Hearts', but any game can be added by adding a new class that implements the 'Game' interface here: https://github.com/tgearravant/java_bot_game_server/blob/master/src/main/java/com/gearreald/BotGameServer/games/Game.java

# For Bots

To design a bot, it must know four things:

* The name of the game that it is playing. See the static method getUninitializedInstanceOfGame in the Games interface to see what the name should be.
* A bot id. This should represent the version of your bot that you are currently using. If you make code changes or update the strategy, you should update this number.
* A player uuid. This should be different for every single game that is played. It can be any string less than 255 characters, but I suggest using UUIDs, potentially prefixing them with your name for readibilty if you wish.
* A match id. This is assigned by the server after successful matchmaking.

The bot must also be able to do four things:
* Create a matchmaking request by sending a GET request to: /matchmaking/create/?game_name=<game_name>&bot_id=<bot_id>&player_uuid=<player_uuid>
* Poll to see if a match has been made and get the ID, which will be 0 if there is no match, by sending a GET request to: /matchmaking/status/?game_name=<game_name>&player_uuid=<player_uuid>
* Poll to get the game state by sending a GET request to: /match/status/?match_id=<match_id>&player_uuid=<player_uuid>
* Take an action by sending POST request containing JSON of the format {"action":{<a JSON action that the game is expecting. See the game for information about the format.>}} to the following address: /match/action/?player_uuid=<player_uuid>
