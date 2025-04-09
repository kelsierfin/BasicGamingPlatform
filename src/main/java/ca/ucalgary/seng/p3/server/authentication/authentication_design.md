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
1) main(String[] args): Entry point for the program, displays menu asking if the user wants to register a new account.
->  Calls createNewAccount(Scanner scanner) to handle the registration process

2) createNewAccount(Scanner scanner): Collects and validates user input, ensures username is unique and passwords match, 
then saves the new account.
-> Calls loadAccounts() to check if the entered username already exists
->  Calls saveAccount(String username, String password, String email) to store the new user information in the CSV file
-> Called by main(String[] args) to handle the account creation workflow

3) loadAccounts(): Loads existing accounts from the CSV file into a map (username → [password, email]).
-> Called by createNewAccount(Scanner scanner) to validate that the username is not already taken

4) saveAccount(String username, String password, String email): Appends a new account to the CSV file with the given 
credentials.
->  Called by createNewAccount(Scanner scanner) to permanently store the account after validation

## UserLogin
#### login(Scanner scanner)

-   Handles user login by verifying credentials from the accounts.csv file.
-   _**Return Value Type:**_ void

#### guestLogin()

-   Handles guest login by creating a temporary session under a randomly generated guest username.
-   _**Return Value Type:**_ void

#### loadAccounts()

-   Loads user accounts from the accounts.csv file into a HashMap.
-   _**Return Value Type:**_ Map<String, String>

#### saveSession(String username)

-   Saves the username of the logged-in user to the session file.
-   _**Return Value Type:**_ void

#### isMFAEnabled(String username)

-   Checks if Multi-Factor Authentication (MFA) is enabled for a given username.
-   _**Return Value Type:**_ boolean

## UserLogout
#### logout()

-   Handles the logout process, verifying the user’s identity and removing them from the session file.
-   _**Return Value Type:**_ void

#### getSessionUsers()

-   Retrieves the list of currently logged-in users from the session file.
-   _**Return Value Type:**_ List<String>

#### clearSession(String username)

-   Removes the specified user from the session file, effectively logging them out.
-   _**Return Value Type:**_ void

#### redirectToLogin()

-   Redirects the user to the login page after logout.
-   _**Return Value Type:**_ void

## ResetCredentials

#### resetPassword(Scanner scanner)

-   Resets the user's password after verifying their email.
-   _**Return Value Type:**_ void

#### resetUsername(Scanner scanner)

-   Resets the user's username after verifying their email.
-   _**Return Value Type:**_ void

#### isValidPassword(String password)

-   Checks if the provided password meets security requirements.
-   _**Return Value Type:**_ boolean

#### isUsernameTaken(List<String[]> accounts, String username)

-   Checks if the provided username is already taken in the user database.
-   _**Return Value Type:**_ boolean

#### loadAccounts()

-   Loads user accounts from the accounts.csv file.
-   _**Return Value Type:**_ List<String[]>

#### saveAccounts(List<String[]> accounts)

-   Saves updated user account information back to the accounts.csv file.
-   _**Return Value Type:**_ void

#### findAccountByEmail(List<String[]> accounts, String email)

-   Finds and retrieves a user account based on the provided email.
-   _**Return Value Type:**_ String[]

#### verifyEmailWithCode(Scanner scanner, String email)

-   Simulates email verification by generating a one-time code and prompting the user for input.
-   _**Return Value Type:**_ boolean

#### generateOneTimeCode(int length)

-   Generates a random numeric one-time code of a specified length.
-   _**Return Value Type:**_ String