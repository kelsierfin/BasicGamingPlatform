# Online Multiplayer Board Game Platform (OMG)

Welcome to **OMG** - a feature-rich online platform where players can enjoy classic board games with friends and competitors worldwide. Built as part of SENG300, this system demonstrates full-stack multiplayer gaming capabilities with extensible architecture.

## Key Features 🎮

### 🔒 Enhanced Authentication System
- **Secure registration** with password complexity enforcement
- **Multi-factor authentication** (email-based OTP)
- **Account recovery** (username/password reset flows)
- **Session management** with automatic timeout

### 🏆 Expanded Game Library
| Game        | Features Implemented                  |
|-------------|---------------------------------------|
| Chess       | Full move validation, check detection |
| Go          | Stone placement, scoring              |
| Connect Four| Win detection, column drops           |
| Tic-Tac-Toe | Classic 3x3 with win logic            |

### ✨ Platform Additional Features
- **Player profiles** with avatars and stats
- **Cross-game leaderboards** with rankings
- **In-game chat** (text-based during matches)
- **Match history** with replay capability

### Components
- **Frontend**: JavaFX with MVC architecture
- **Authentication**: JWT-based sessions with CSRF protection
- **Game Logic**: Rule engines for each game type
- **Networking**: Hybrid TCP/UDP communication
- **Data**: CSV persistence (simulated DB)

## Getting Started 🚀

### Prerequisites
- Java 17+
- Maven 3.8+
- JavaFX SDK

### Installation
```bash
git clone https://csgit.ucalgary.ca/sanbeer.shafin/seng300-w25-project
```

### Running the Platform
