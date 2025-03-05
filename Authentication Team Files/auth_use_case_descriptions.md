# Use Cases

### Registering/Creating New Account 
**Primary Actor:**  
Game Player

**Goal in Context:**  
Enable any person to create an account. 

**Pre-Conditions:**
- Networking is running as needed.
- It's a human player and not a bot that is trying to overload the server. 

**Trigger:**
- Player clicks the create new account after opening the platform. 

**Scenario:**
1. Player loads the platform through a web browser.
2. Player clicks "Create An Account" after being promoted to login. 
3. Player inputs sufficient  details for Username, Password, Verify Password & Email. 
4. If inputted invalid details where passwords don't match the requirements or if username already exists, it asks
   the user to perform actions again until valid details are put in. 
5. When player's details are valid, it creates account for the player and asks the user to login. 

**Post-Conditions:**
- The player has created an account with an username and passwords that meet the minimum requirements. 
- The account details get added to the authentication database and thus allows the player to save progress in game and
   have a tracked identity in the platform. 

**Exceptions:**
- Server malfunction due to overloading

**Priority:**
- High – Essential for all parties in the game. 

**When Available:**
- First increment.

**Frequency of Use:**
- Frequent – Couple times every day or couple times every minute depending on the popularity of the platform.

**Channel to Actor:**
- Click of a button using touchscreen or mouse click. 

**Secondary Actors:**
- Developers & Testers

**Channels to Secondary Actors:**
- Raw Code

**Open Issues:**
1. At what point should the server forcibly shut own the player(if the user is creating many a
 accounts using the same IP)?

### User Login/Logout 
**Primary Actor:**  
Game Player

**Goal in Context:**  
Allow a player to login and logout.

**Pre-Conditions:**
- The system is accessible and online.
- The player has an already registered account.

**Trigger:**
- Player initiates login by clicking the "Login" button.
- Player initiates logout by clicking the "Logout" button.

**Scenario:**

***Login:***
1. Player clicks the "login" button.
2. Players enters their username and password.
3. System validates their login credentials.
4. if credentials are valid, the player is granted access.
5. If credentials are invalid, an error message is displayed and the player is prompted to try again.
6. The system may temporarily lock the account if the player has multiple failed attempts.

***Logout***
1. Player clicks the "logout" button.
2. System ends the session.

**Post-Conditions:**
- After login, the player has access to their profile, and the game features.
- After logout, the session is ended, and the player must login again to the access their profile.
**Exceptions:**
- Server downtime.
- Multiple incorrect credentials may result in a temporary lock.
- Automatic logout due to inactivity.

**Priority:**
- High. Essential for security, and also for user acess control.

**When Available:**
- First increment.

**Frequency of Use:**
- Frequent. Every time a player accesses or leaves the platform.

**Channel to Actor:**
- Web interface via mouse click or touch interaction.

**Secondary Actors:**
- System administrator

**Channels to Secondary Actors:**
- Admin dashbord

**Open Issues:**
1. A possible "remember me" option for easier access.
2. Should multifactor authentication be implemented?
3. After how long should should the system automatically logout due to inactivity?