Class diagrams notes

MatchmakingLogic.java

- Takes the player match rules and game type and find a player near their mmr waiting for a match and send their info to the match class so the game can start

Leaderboard.java

- Gets updated player rank and mmr from the player stat class after a match  
- Checks if the player moved up or down a ranking on the leaderboard and update the leaderboard accordingly

PlayerStats.java

- Gets match info and mmr changes to keep in a database  
- Determines win/loss ratio, total games played, if they were wins or losses, who they were against, mmr, leaderboard rank and displays it to profile  
- Divides stats based on game types

Match.java

- Gives match rules and game type (ranked/unranked) to a game logic class to run the game itself  
- Takes match result, updates players’ mmr according to the differences and sends with match result to PlayerStats

GameDataDatabase.java

- Stub  
- Used as a mock database with set values for player stats, leaderboard and match history for testing and to simulate how it will connect to the real database

