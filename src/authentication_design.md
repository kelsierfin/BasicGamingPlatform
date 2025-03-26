## View Match History
- takes User as an input to the class
- addMatch : adds match to matches list
- removeMatch : removes match from the matches list
- getMatchhistory : get match history, view a list of all the match objects

### Profile Editor ###
Allows user to make changes to their profile
- startProfileEditor(): Main menu for profile editing
- getPassword(): Returns a masked password for display purposes
- getEmail(String email): Masks user email for display purposes
- getAccountDetails(String username): Retrieves user information from file
- updateUsername(): Updates username
- updateEmail() : Updates email if input is valid
- updatePassword(): Updates password if all requirements are met
- writeAllAccounts(): Saves all accounts 
- loadAccounts(): Loads accounts from CSV file to a map 
- isValidEmail(): Ensures email is in correct format
- isValidPassword(): Ensures password meets criteria 