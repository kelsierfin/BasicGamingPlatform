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

### 1. GUI Controllers

| Controller Name              | Purpose                    |
|------------------------------|----------------------------|
| AboutController              | Display info about project |
| ChessController              | Displays chess game        | 
| Connect4Controller           | Displays connect4 game     | 
| FriendFinderController       | Displays friend finder screen|
| GoController                 | Displays go game           |  
| HomeController               | Displays home screen       | 
| LandingController            | Displays landing screen    |
| LeaderBoardController        | Displays leaderboard       |
| LogInController              | Displays log in screen     |
| MatchHistoryController       | Displays match history     |  
| ProfileController            | Displays user's profile    | 
| ResetCredentialsController   | Displays reset credentials screen| 
| SettingsController           | Displays settings screen   |
| SignUpController             | Displays screen for user to sign up|
| StartGameController          | Displays all game options for user to begin game| 
| TicTacToeController          | Displays tictactoe game    | 

Pop-ups

| Controller Name              | Purpose                    |
|------------------------------|----------------------------|
| ProfilePopUp                 | Display player profile     |
| chatPopUpController          | UI for users to chat with each other| 


## Error Handling
- Errors should be handled by backend
- GUI may display error message 
