Use case: Player viewing their own statistics
Iteration: 1, last modification: Mar 6 by Min Oh
Primary actor: Player
Goal in context: To allow the player to view their personal profile, including their game statistics, ranking, match history, and highest ranked achieved
Preconditions:
    The player has an active and verified account
    The player has played all of their placement game(s) and received a ranking
    Trigger: The player select their profile from the game menu
Scenario:
    The player clicks on their profile from the game menu
    The system retrieves the player's stored profile data from the database.
    The system displays key information, including:
    Username and profile picture
    Current ranking and rank title
    W/L ratio
    Recent match history
    Total games played
    The player clicks on any one of the options above and each options will display its own features (i.e. when clicked on recent match history it will show the player and opponent name and ranking, results, match duration)
Exceptions:
    If there is a server issue or missing data, the system displays an error message and prompts the player to retry
    If the player hasn’t played any ranked matches but has played casual matches, the system will display an unranked status and statistics for casual matches will be provided and vice versa
Priority: High
When available: First increment
Frequency of use: Frequent as players like to check their profile as they want to check for improvements
Channel to actor: When profile in game menu is selected
Secondary actors: Server database (fetching and updating profile data)
Channel to secondary actors: API request to server database
Open issues: None
