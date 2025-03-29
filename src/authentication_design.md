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


### Account Registration/ Creation ###
Allows user to create new accounts and stores the credentials in a CSV file
1)main(String[] args): Entry point for the program, displays menu asking if the user wants to register a new account.
→ Calls createNewAccount(Scanner scanner) to handle the registration process

2) createNewAccount(Scanner scanner): Collects and validates user input, ensures username is unique and passwords match, 
then saves the new account.
→ Calls loadAccounts() to check if the entered username already exists
→ Calls saveAccount(String username, String password, String email) to store the new user information in the CSV file
→ Called by main(String[] args) to handle the account creation workflow

3)loadAccounts(): Loads existing accounts from the CSV file into a map (username → [password, email]).
→ Called by createNewAccount(Scanner scanner) to validate that the username is not already taken

4)saveAccount(String username, String password, String email): Appends a new account to the CSV file with the given 
credentials.
→ Called by createNewAccount(Scanner scanner) to permanently store the account after validation