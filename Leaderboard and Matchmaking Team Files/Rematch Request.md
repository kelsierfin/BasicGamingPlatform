Use case: Rematch Request
Iteration: 1, last modification: Mar 7 by Min Oh
Primary actor: Player
Goal in context: To allow the player to send a rematch request to their most recent opponent
Preconditions:
    The player has an active and verified account
    The player just completed a match
    The opponent is still online and agrees to the rematch
Trigger: The player selects “Request Rematch” after game ends
Scenario:
    After finishing a game the request rematch prompt appear under the continue prompt
    The player selects rematch request
    The system sends a rematch invitation to the opponent.
    The opponent can accept or decline.
    If accepted, the system starts a new game
Exceptions:
    If the opponent is no longer online the system will prompt a message “Your opponent is no longer online.”
    If the opponent refused the rematch then the system will prompt a message “Rematch Declined”
Priority: High
When available: First increment
Frequency of use: Frequent as players like to check their profile as they want to check for improvements
Channel to actor: In game screen after a finished match
Secondary actors: Server database (fetching and updating info from most recent match)
Channel to secondary actors: API request to server database
Open issues: None
