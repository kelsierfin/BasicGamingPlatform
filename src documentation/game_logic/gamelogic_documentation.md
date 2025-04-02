# Game-Specific Logic

## 1. Purpose

### Purpose

-   Document game-specific logic implementations for multiple games
-   Standardize integration points across game subsystems
-   Define error handling procedures and testing guidelines
-   Provide reference for future game implementations
-   Ensure consistent player experience across all games

### Audience

-   Game Logic
-   Authentication/Profile
-   Networking
-   GUI

## 2.1 Tic Tac Toe

### Objective

Players alternate placing symbols (X/O) on a 3x3 grid. The first to form three symbols in a row, column, or diagonal wins.

### Key Components

-   **Board**: 3x3 grid managed via `Board` class.
-   **Win Conditions**: Check rows, columns, diagonals for three consecutive symbols.
-   **Game Flow**:
    1. Players select symbols (X/O).
    2. Alternating turns until win/draw.
    3. Update leaderboard and profile stats post-match.
-   **Error Handling**:
    -   Invalid cell selection triggers alerts.
    -   Handle player disconnects by awarding the win to the active player.

### Integration Points

-   **Matchmaking**: Players join a lobby after selecting Tic Tac Toe.
-   **Leaderboard**: Increment `wins/losses` in `Profile` and update `ScoreBoard`.

## 2.2 Connect 4

### Objective

Players drop discs into columns to form four consecutive discs horizontally, vertically, or diagonally.

### Key Components

-   **Board**: Customizable grid (default 6x7).
-   **Win Conditions**: Implement `HorizontalWinCondition`, `VerticalWinCondition`, and `DiagonalWinCondition` strategies.
-   **Game Flow**:
    1. Initialize board and determine starting player.
    2. Players alternate selecting valid columns.
    3. Check for win/draw after each move.
-   **Error Handling**:
    -   Invalid column selection (out-of-range/full) triggers warnings.
    -   Network issues prompt reconnection attempts or match termination.

### Integration Points

-   **Networking**: Multiplayer mode requires stable connections; handle reconnections gracefully.
-   **Leaderboard**: Update `Profile` with game outcomes and increment `matchesPlayed`.

## 2.3 Chess

### Objective

Players strategize to checkmate the opponent's king using piece-specific movement rules.

### Key Components

-   **Pieces**: Subclasses of `Piece` (King, Queen, Bishop, etc.) with unique `findMoveOptions()`.
-   **Board**: 8x8 grid managed via `BoardLocation` objects.
-   **Game Flow**:
    1. Players alternate selecting pieces and valid moves.
    2. Check for checkmate, stalemate, or draw conditions.
    3. Track captured pieces and material advantage.
-   **Error Handling**:
    -   Invalid moves (e.g., moving into check) trigger GUI alerts.
    -   Timeouts result in forfeiture.

### Integration Points

-   **GUI**: Update screens for move options, captures, and checkmate announcements.
-   **Leaderboard**: Update `rank` based on match outcomes and track `materialAdvantage` stats.

# 3. Common Systems Integration

## 3.1 Matchmaking & Multiplayer

-   **Process**:
    1. Players queue for a game via `GUI->Matchmaking`.
    2. On match found, `launchGame()` and initialize lobby.
    3. Sync game state between players using networking modules.
-   **Edge Cases**:
    -   Reassign players if a participant disconnects.
    -   Terminate matches after 2 minutes of inactivity.

## 3.2 Leaderboard & Profile Updates

-   **Post-Match Actions**:
    -   Call `Profile.addWin()/addLoss()` and `Leaderboard.updateRank()`.
    -   Log match history with timestamps and opponent details.

# 4. Error Management

| Error                              | Solution                                       | Affected Games                |
| ---------------------------------- | ---------------------------------------------- | ----------------------------- |
| Invalid move (e.g., occupied cell) | Display alert, retain turn.                    | Tic Tac Toe, Connect 4, Chess |
| Player disconnect                  | Award win to active player, update leaderboard | All                           |
| Network timeout                    | Attempt reconnection; else end match.          | Tic Tac Toe, Connect 4        |
| Checkmate not detected             | Validate via `isGameTrace(Player)` in Chess.   | Chess                         |

# 5. Testing Guidelines

## Tic Tac Toe

-   Test win/draw conditions (e.g., diagonal three-in-a-row).
-   Validate rematch functionality post-game.

## Connect 4

-   Verify win detection for all four directions.
-   Test column overflow handling.

## Chess

-   Validate piece movement rules (e.g., pawn promotion, castling).
-   Test checkmate and stalemate scenarios.

## Cross-Game Tests

-   Leaderboard updates after wins/losses.
-   Matchmaking stability under high latency.

# 6. Open Issues

1. **Tic Tac Toe**: Support for offline mode and variable grid sizes.
2. **Connect 4**: Optimize win-check algorithms for large boards.
3. **Chess**: Implement AI difficulty levels for single-player mode.
