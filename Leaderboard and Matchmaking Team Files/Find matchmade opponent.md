* Use Case: Find Matchmade Opponent
* Iteration: 1
* Primary Actor: Game Player
* Goal in Context: Enable a player to be matched against another player of similar skill level for a selected game.
* Preconditions:
*  The system is accessible and online.
* Player is logged into the platform.
* Player has completed enough games to have an established skill rating.
* Trigger: Player chooses a game type and selects 'Find Opponent'.
* Scenario:
1. Player chooses a game type and selects 'Find Opponent'.
2. System retrieves player's current skill rating and game history from database.
3. System searches for other online players who are also seeking matches for the same game.
4. System identifies players within an appropriate skill range (±100 rating points).
5. System selects the best match based on skill similarity, connection quality, and wait time.
6. System notifies both players that a match has been found.
7. Both players confirm readiness within 30 seconds.
8. System initializes the game session with both players.
9. Game begins with standard rules and settings.
   * Post conditions:
* Both players are placed in the same game session.
* Match is recorded in the system for future skill calculations.
* Players' status is changed to "In Game".
  * Exceptions:
1. No Suitable Opponent: If no suitable opponents are found within 45 seconds, system expands search criteria to include wider skill range.
2. Search Cancellation: If player cancels search, system removes player from matchmaking queue.
3. Readiness Confirmation Failure: If matched player doesn't confirm readiness, system cancels match setup and searches again.
   * Priority: High – Essential for the matchmaking functionality of the platform.
   * When Available: First increment.
   * Frequency of Use: Very frequent – Will be used constantly as players seek matches.
   * Channel to Actor: Click of a button using touchscreen or mouse click.
   * Secondary Actors: Matchmaking System, Other Players
   * Channel to Secondary Actors: Game client interface, network communication.
   * Open Issues:
1. How quickly should the skill range expand if matches aren't found?
2. Should players be able to set preferences for opponents beyond skill level?
3. How to handle disconnections during the matchmaking process?