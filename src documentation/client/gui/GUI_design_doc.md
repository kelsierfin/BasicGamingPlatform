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

