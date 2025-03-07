* Use Case: View Other Players Profile/Statistics
* Iteration: 1
* Primary Actor: Game Player
* Goal in Context: Allow a player to view detailed information about other players' profiles, game statistics, and performance metrics.
* Preconditions:
* The system is accessible and online.
* Player is logged into the platform.
* The player whose profile is being viewed has a public profile or appropriate privacy settings.
  * Trigger: Player selects another player's username from a leaderboard, match history, or search results.
  * Scenario:
1. Player finds another player through leaderboard, search function, or match history.
2. Player selects/clicks on the other player's username.
3. System retrieves the selected player's profile data and statistics from database.
4. System displays the profile page showing the player's:
   a. Username and profile information
   b. Game-specific statistics (wins, losses, draws)
   c. Ranking and rating across different games
   d. Recent match history
   e. Achievements and badges
5. Player can toggle between different statistical views (by game type, by time period).
6. Player can select the option to challenge this player to a game if desired.
   * Post conditions:
* Player has viewed the requested profile and statistics.
  * Exceptions:
1. Private Profile: If the selected player has set their profile to private, system displays limited information or a privacy notice.
2. Data Unavailable: If player has no match history or statistics, system displays appropriate message indicating no data available.
3. System Error: If database connection fails, system displays error message and suggests trying again later.
   * Priority: Medium – Important for social and competitive aspects of the platform.
   * When Available: First increment.
   * Frequency of Use: High – Players will regularly check other players' statistics for competitive analysis.
   * Channel to Actor: Click of a button using touchscreen or mouse click.
   * Secondary Actors: Database System
   * Channel to Secondary Actors: Data retrieval interface.
   * Open Issues:
1. Should players be able to hide certain statistics from public view?
