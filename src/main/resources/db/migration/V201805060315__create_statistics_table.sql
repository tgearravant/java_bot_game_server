CREATE TABLE statistics(
  id INTEGER PRIMARY KEY,
  game_name VARCHAR(255),
  match_id INT,
  winning_bot_id INT,
  winning_player_uuid VARCHAR(255),
  info TEXT
)