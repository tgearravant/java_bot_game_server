CREATE TABLE match_requests(
  id INTEGER PRIMARY KEY,
  player_uuid VARCHAR(255),
  bot_id INTEGER,
  satisfied INTEGER,
  match_id INTEGER,
  game_name VARCHAR(255)
)