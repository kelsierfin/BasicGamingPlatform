Use case: Player can view Game Leaderboard
Iteration: 1, last modification: Mar 5 by Min Oh
Primary actor: Player
Goal in context: To allow the player check their regional/global ranking and able to see their rank with other players
Preconditions:
    The player has an active and verified account
    The player has played all of their placement game(s) and received a ranking
    The leaderboard system is active and accessible
Trigger: The player select the leaderboard button from the game menu
Scenario:
    The player selects the Leaderboard option from the game menu.
    The player can select the filter to display the local regional leaderboard or the global leaderboard
    The system retrieves and displays the top 100 ranked players based on Elo/MMR.
    The player sees their current rank, statistics and match history.
Exceptions:
    If the leaderboard fails to load, the system displays an error message.
    If the player hasn’t played any ranked matches, the system will display an unranked status
Priority: High
When available: First increment
Frequency of use: Frequent as players like to check their ranking
Channel to actor: When leaderboard button in menu is selected
Secondary actors: Server database (fetching and updating leaderboard)
Channel to secondary actors: API request to server database
Open issues: None
