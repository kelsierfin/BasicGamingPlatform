## Design Document for Connect4 Game

## Detailed Class Specifications

### 1\. Connect4 Class

**Purpose**: Main class that orchestrates game flow and player interaction.

-   Initializes the game board and players
-   Manages the game loop
-   Handles player turns
-   Determines game termination (win/loss/draw)
-   Processes player input

**Key Methods**:

-   `main(String[] args)`: Entry point of the application
-   Game loop implementation to alternate between players
-   Input validation and processing

### 2\. Connect4_Board Class

**Purpose**: Represents the game board and handles board-related operations.

-   `ROWS` (6): Number of rows on the board
-   `COLUMNS` (7): Number of columns on the board
-   `Board`: 2D array representing the game grid

**Key Methods**:

-   `Connect4_Board()`: Constructor to initialize the board
-   `getBoard()`: Returns the current board state
-   `placeDisc(int column, Disc disc)`: Places a disc in the specified column
-   `removeDisc(int row, int column)`: Removes a disc from the board
-   `validMove(int column)`: Validates if a move is legal
-   `checkWin(String sym)`: Checks if a player has won
-   `isColumnFull()`: Checks if a column is full
-   `printBoard()`: Displays the board in the console
-   `getSymbol(String x)`: Extracts the symbol from a cell

### 3\. Player Class

**Purpose**: Represents a player in the game. **Attributes**:

-   `name`: Player's name
-   `selectedSymbol`: Symbol used by the player ('Y' or 'R')
-   `numberOfDiscPlaced`: Count of discs placed
-   `numberOfWins`: Number of games won

**Key Methods**:

-   `Player(String name, String selectedSymbol)`: Constructor
-   `addWin()`: Increments win count
-   Getters for player attributes

### 4\. Disc Class

**Purpose**: Represents a disc placed on the board. **Attributes**:

-   `symbol`: The symbol representing the disc ('Y' or 'R')

**Key Methods**:

-   `Disc(String symbol)`: Constructor
-   `getSymbol()`: Returns the disc's symbol

### 5\. ChatBox Class

**Purpose**: Enables communication between players. **Attributes**:

-   `playerOne`: Reference to the first player
-   `playerTwo`: Reference to the second player

**Key Methods**:

-   `ChatBox(Player player1, Player player2)`: Constructor
-   `sendMessage(String message)`: Sends a message to other player
-   `receiveMessage()`: Retrieves messages

### 6\. ScoreBoard Class

**Purpose**: Tracks game statistics. **Attributes**:

-   `points`: Total points accumulated
-   `gamesPlayed`: Number of games completed

**Key Methods**:

-   `ScoreBoard()`: Constructor
-   `addPoints(int points)`: Updates the score
-   `addGamesPlayed(int x)`: Increments games played count

## Key APIs

### Board Management API

```java


// Initialize a new game board
Connect4_Board board =  new  Connect4_Board();

// Check if a move is valid
boolean isValid = board.validMove(columnNumber);

// Place a disc on the board
board.placeDisc(columnNumber,  new  Disc(playerSymbol));

// Check for a win condition
boolean hasWon = board.checkWin(playerSymbol);

// Display the current board
board.printBoard();`
```

### Player Management API

```java


// Create a new player
Player player =  new  Player(playerName, chosenSymbol);

// Record a win for a player
player.addWin();

// Access player information  String symbol = player.getSelectedSymbol();
int wins = player.getNumberOfWins();  String name = player.getName();`
```

### Game Communication API

```java
// Initialize chat between players
ChatBox chat =  new  ChatBox(player1, player2);
// Send a message  chat.sendMessage("Good move!");

// Receive messages
String message = chat.receiveMessage();`
```

### Game Statistics API

```java
// Initialize the scoreboard
ScoreBoard scoreBoard =  new  ScoreBoard();

// Update game statistics  scoreBoard.addPoints(10);
scoreBoard.addGamesPlayed(1);
```

## Data Storage

### In-Memory Data Structures

1.  **Game Board**:
    -   Implemented as a 2D String array `Board[ROWS][COLUMNS]`
    -   Each cell contains a string representation of the cell state ("[ ]" for empty, "[Y]" or "[R]" for player discs)
    -   Last column cells have an additional newline character for display purposes
2.  **Player Information**:
    -   Stored in Player objects
    -   Contains name, symbol, game statistics
3.  **Game State**:
    -   Maintained through boolean flags in the Connect4 class
    -   `moveValid` tracks if the last move was valid
    -   `gameEnd` indicates if the game has concluded

### Persistence Considerations

The current implementation does not include persistent storage. For future enhancements, consider:

-   Saving game state to files for game resumption
-   Database integration for player profiles and statistics
-   Match history recording for replay functionality

## Error Handling

### Input Validation

1.  **Player Symbol Selection**:
    -   Enforces selection of only valid symbols ('Y' or 'R')
    -   Uses a loop to continually prompt until valid input is received
2.  **Move Validation**:

```java


    public  boolean  validMove(int column)  {    if  (column <  0  || column >  6)  {    return  false;
    // Column out of bounds    }  else  if  (isColumnFull())  {    return  false;  // Column is full    }
    else  {    return  true; // Valid move    }  }

```

3.  **Game Flow Control**:
    -   Uses `moveValid` flag to ensure only legal moves progress the game
    -   The main game loop repeats until a valid move is made

### Exception Handling Strategies

1.  **Input Parsing**:
    -   Current implementation uses `Integer.parseInt()` for column selection
    -   Enhancement: Add try-catch blocks to handle non-numeric input:

```java


    `try  {    int column =  Integer.parseInt(input);    // Process valid numeric input  }
    catch  (NumberFormatException e)  {    System.out.println("Please enter a valid column number");
    // Handle invalid input  }
```

2.  **Boundary Checking**:
    -   The `validMove()` method checks column boundaries and fullness
    -   Array index out-of-bounds prevention in board manipulation methods
3.  **Null Reference Prevention**:
    -   Constructor initialization of all critical game components
    -   Validation of player objects before game start
