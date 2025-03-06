* Use Case: Challenge Player from Friends List
* Iteration: 1
* Primary Actor: Game Player
* Goal in Context: Allow a player to directly challenge a friend to a game without affecting either player's ranking.
  * Preconditions:
* The system is accessible and online.
* Player is logged into the platform.
* Player has at least one friend added to their friends list.
* The friend is currently online and available.
  Trigger: Player selects a friend from their friends list and clicks "Challenge to Game".
  * Scenario:
1. Player navigates to their friends list in the platform.
2. System displays all online friends with their current status (available, in-game, busy).
3. Player selects a specific friend who is marked as available.
4. Player chooses "Challenge to Game" option.
5. System presents game selection menu with available board games.
6. Player selects desired game type (e.g., Chess, Connect Four).
7. Player selects "Friendly Match" option to indicate the game won't affect rankings.
8. Player confirms and sends challenge request.
9. System delivers notification to friend.
10. Friend receives and accepts challenge.
11. System initializes the game session between both players.
    * Post conditions:
* Both players are placed in the same game session with selected parameters.
* Match is recorded in the system as a "Friendly Match" that doesn't affect player rankings.
* Players' match history is updated to include the friendly match.
  * Exceptions:
1. Friend Unavailable: If the selected friend changes status to unavailable before challenge is sent, system notifies player and prevents challenge.
2. Challenge Declined: If friend declines challenge, system notifies challenger and returns to friends list.
3. Challenge Timeout: If friend doesn't respond within 2 minutes, challenge expires and system notifies challenger.
* Priority: Medium – Important for social aspects but not critical to core gameplay.
* When Available: First increment.
* Frequency of Use: Moderate – Will be used regularly by players who prefer playing with friends.
* Channel to Actor: Click of a button using touchscreen or mouse click.
* Secondary Actors: Friend (Challenged Player)
* Channel to Secondary Actors: Game client interface, notification system.
* Open Issues:
1. Should players be able to spectate friendly matches between other players?
2. Should there be an option to convert a friendly match into a ranked match if both players agree?
3. Should friendly matches have different rules or settings available compared to ranked matches?