Game Design Document
Common Functions Across the Games:

    initializeGame(): Sets up players, board, and game loop.

    switchTurn(): Alternates between players.

Tic-Tac-Toe:

1. Game Overview:

Tic-Tac-Toe is a two-player game played on a 3x3 grid....

2. Game Initialization:

    Players are assigned either X or O.

    The 3x3 grid is displayed with all spaces empty.

    The player assigned X moves first, followed by O. Players then take turns making moves.

Implementation Measures:

Data Structures:

    Board: A 3x3 2D array representing the grid.

    Player: Stores player information (symbol: X/O).

    GameState: Tracks whose turn it is (initialized with X).

Functions Required:

    setBoard(): Initializes an empty 3x3 grid.

    setPlayers(): Assigns symbols (X/O) to players.

    startGame(): Begins the game loop.

3. Game Flow:

    A player selects an empty cell to place their symbol.

    The system checks if the move is valid (i.e., the cell is empty).

    If valid, the symbol is placed in the selected cell. If not, the player must choose another cell.

    The system checks for a win or draw after each move.

    The turn switches to the other player after a valid move.

Implementation Measures:

Functions Required:

    makeMove(row, col): Places the current player's symbol on the board.

    switchTurn(): Switches the turn to the other player.

    isMoveValid(row, col): Checks if the selected cell is empty.

4. Win Conditions:

    Row Win: Three matching symbols in a single row.

    Column Win: Three matching symbols in a single column.

    Diagonal Win: Three matching symbols diagonally (top-left to bottom-right or top-right to bottom-left).

Implementation Measures:

Functions Required:

    checkWin(): Checks if the current move resulted in a win.

        Example: Iterate through rows, columns, and diagonals to check for three matching symbols.

5. Game End Conditions:

    Win: A player satisfies one of the win conditions.

    Draw: All cells are filled, and no player has won.

    Player Forfeits: A player loses if they quit before the game ends.

Implementation Measures:

Functions Required:

    checkDraw(): Checks if all cells are filled without a winner.

    forfeitGame(): Ends the game if a player quits.