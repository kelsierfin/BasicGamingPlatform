# GUI-Authentication Class Specifications

## Table of Contents
1. [Authentication System Classes](#authentication-system-classes)
2. [GUI Controller Classes](#gui-controller-classes)
3. [Shared Utility Classes](#shared-utility-classes)
4. [Integration Interfaces](#integration-interfaces)

---

## Authentication System Classes

### 1. `UserLogin`
**Purpose**: Central login handler connecting GUI to authentication backend  
**Key Methods**:

| Method | Parameters | Returns | Description |
|--------|------------|---------|-------------|
| `login()` | `Scanner` | `void` | Main login flow with credential validation |
| `guestLogin()` | None | `String` (username) | Creates temporary guest session |
| `saveSession()` | `String` username | `void` | Persists session to `session.csv` |
| `isMFAEnabled()` | `String` username | `boolean` | Checks if user has MFA activated |

**Integration Points**:
- Called from `LogInController.handleLogInButton()`
- Uses `FailedLogin` for attempt tracking
- Updates `session.csv` on success

---

### 2. `FailedLogin`
**Purpose**: Brute force protection and account locking  
**Key Methods**:

| Method | Parameters | Returns | Description |
|--------|------------|---------|-------------|
| `authenticate()` | `String` username, `String` password | `AuthResult` | Validates credentials and tracks attempts |


---

### 3. `MultifactorAuthentication`
**Purpose**: Handles 2-factor authentication flows  
**Key Methods**:

| Method | Parameters | Returns | Description |
|--------|------------|---------|-------------|
| `startMFAProcess()` | `Scanner`, `String` username | `boolean` | Initiates OTP verification |
| `generateOneTimeCode()` | `int` length | `String` | Creates 6-digit OTP |
| `verifyCode()` | `String` input, `String` code | `boolean` | Validates user input |

**Integration Flow**:
1. Triggered from `LogInController` when MFA enabled
2. Displays in GUI via `mfaCard` visibility toggle

---

## GUI Controller Classes

### 1. `LogInController`
**Purpose**: Handles login screen interactions  
**Key Integration Methods**:

| Method | Called When | Integrates With |
|--------|------------|-----------------|
| `handleLogInButton()` | Login button click | `UserLogin`, `FailedLogin` |
| `handleGuestLogInButton()` | Guest login click | `UserLogin.guestLogin()` |
| `handleSubmitMfaCode()` | MFA code submission | `MultifactorAuthentication` |

**UI Elements**:
- `passwordInput`: Secure password field
- `mfaCard`: Hidden until MFA required
- `errorMessage`: Displays auth failures

---

### 2. `SignUpController`
**Purpose**: New account registration  
**Validation Methods**:

| Method | Validation Rule | Error Message |
|--------|----------------|---------------|
| `usernameConfirmation()` | Unique in `accounts.csv` | "Username exists" |
| `passwordConfirmation()` | 8+ chars, 1 number, 1 uppercase | "Weak password" |
| `emailConfirmation()` | Contains @ and . | "Invalid email" |

**Integration**:
```java
// If validation is ok:
AccountRegistrationCSV.saveAccount(username, password, email);
```

---

### 3. `ProfileController`
**Purpose**: User profile management  
**Security Features**:
- MFA requirement for changes
- Password confirmation for sensitive actions
- Current session validation

**Key Methods**:
```java
public void updateEmail(String newEmail) {
    if (MultifactorAuthentication.verifyUser(currentUser)) {
    }
}
```

---

## Shared Utility Classes

### 1. `CurrentUser`
**Purpose**: Singleton representing logged-in user  
**Implementation**:
```java
public class CurrentUser {
    private static CurrentUser instance;
    private String username;
    
    private CurrentUser(String username) {
        this.username = username;
    }
    
    public static CurrentUser getInstance(String username) {
        if (instance == null) {
            instance = new CurrentUser(username);
        }
        return instance;
    }
}
```

**Usage**:
```java
// Used in controllers:
String username = CurrentUser.getInstance().getUsername();
```

### 2. `PageNavigator`
**Purpose**: Secure page transitions with session checks  
**Key Method**:
```java
public static void navigateTo(String page) {
    if (requiresAuth(page) && !hasActiveSession()) {
        redirectToLogin();
    }
}
```

---

## Integration Interfaces

### 1. Authentication API Contract
```java
public interface AuthService {
    AuthResult login(String username, String password);
    void logout(String sessionToken);
    boolean verifyMFA(String username, String code);
}
```

### 2. Session Management Contract
```java
public interface SessionManager {
    String createSession(String username);
    boolean validateSession(String token);
    void endSession(String token);
}
```

### 3. Data Transfer Objects

**AuthRequest**:
```java
public class AuthRequest {
    private String username;
    private String password;
}
```

**AuthResponse**:
```java
public class AuthResponse {
    private boolean success;
    private String sessionToken;
    private String error;
}
```

---

## Class Relationships (Diagram)


---

## Error Handling
