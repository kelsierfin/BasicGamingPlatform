# GUI System Design Document

## Table of Contents
1. [System Overview](#system-overview)
2. [Class Diagram](#class-diagram)
3. [Detailed Class Specifications](#detailed-class-specifications)
4. [Integration Points](#integration_points)
6. [Error Handling](#error-handling)

## System Overview
The GUI system provides:
- A visual interface for users to interact with

```mermaid
flowchart TD
    A[Landing] --> B[SignUp]
    A --> C[LogIn]
    A --> D[About]
    D --> B
    D --> A
    D --> C
    B --> D
    B --> A
    B --> C
    C --> B
    C --> A
    C --> D
    C --> E[Home]
    C --> F[ResetCredentials]
    E --> G[LeaderboardHome]
    E --> H[PlayerFinder]
    E --> I[Settings]
    E --> J[StartGame]
    G --> E
    G --> H
    G --> I
    G --> K[GameLeaderboard]
    K --> G
    H --> E
    H --> G
    H --> I
    I --> E
    I --> G
    I --> H
    J --> L[TicTacToe]
    J --> M[Connect4]
    J --> N[Chess]
    J --> O[Go]
    L --> E
    M --> E
    N --> E
    O --> E
    G --> P[LogOut]
    H --> P[LogOut]
    I --> P[LogOut]
    J --> P[LogOut]
    P --> A
    
```

## Class Diagram



## Detailed Class Specifications

### 1. GameApplication

### 2. Main

### 3. AboutController

### 4. ChessController

### 5. Connect4Controller

### 6.
## Integration Points



## Error Handling
- Errors should be handled by backend
- GUI may display error message 
