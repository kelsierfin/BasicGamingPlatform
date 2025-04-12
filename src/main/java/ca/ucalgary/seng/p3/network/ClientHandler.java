package ca.ucalgary.seng.p3.network;

import ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService;
import ca.ucalgary.seng.p3.server.leadmatch.reflection.LeaderboardService;
import ca.ucalgary.seng.p3.server.leadmatch.reflection.MatchmakingService;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Gson gson = new Gson();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String requestJson = in.readLine();
            Request request = gson.fromJson(requestJson, Request.class);

            AuthenticationService authService = new AuthenticationService();
            MatchmakingService matchmakingService = new MatchmakingService();
            LeaderboardService leaderboardService = new LeaderboardService();
            Response response = null;

            // Process the request based on action type
            switch (request.getAction()) {
                //auth cases
                case "login":
                    response = handleLogin(authService, request);
                    break;
                case "guestLogin":
                    response = handleGuestLogin(authService);
                    break;
                case "signUp":
                    response = handleSignUp(authService, request);
                    break;
                case "generateMfaCode":
                    response = handleGenerateMfaCode(authService, request);
                    break;
                case "verifyMfaCode":
                    response = handleVerifyMfaCode(authService, request);
                    break;
                case "getUserProfile":
                    response = handleGetUserProfile(authService, request);
                    break;
                case "checkAccountLock":
                    response = handleCheckAccountLock(authService, request);
                    break;
                case "logout":
                    response = handleLogout(authService, request);
                    break;
                case "deleteAccount":
                    response = handleDeleteAccount(authService, request);
                    break;
                case "updateUsername":
                    response = handleUpdateUsername(authService, request);
                    break;
                case "updateEmail":
                    response = handleUpdateEmail(authService, request);
                    break;
                case "updatePassword":
                    response = handleUpdatePassword(authService, request);
                    break;
                case "toggleMfa":
                    response = handleToggleMfa(authService, request);
                    break;

                //matchmaking cases
                case "matchPlayers":
                    response = handleMatchPlayers(matchmakingService, request);
                    break;
                case "queueForGame":
                    response = handleQueueForGame(matchmakingService, request);
                    break;
                case "findMatch":
                    response = handleFindMatch(matchmakingService, request);
                    break;

                //lead cases
                case "getLeaderboard":
                    response = handleGetLeaderboard(leaderboardService, request);
                    break;
                case "getPlayerStats":
                    response = handleGetPlayerStats(leaderboardService, request);
                    break;
                case "getMatchHistory":
                    response = handleGetMatchHistory(leaderboardService, request);
                    break;
                case "updatePlayerStats":
                    response = handleUpdatePlayerStats(leaderboardService, request);
                    break;
                case "getLeaderboardPositions":
                    response = handleGetLeaderboardPositions(leaderboardService, request);
                    break;
                case "checkEmail":
                    response = handleCheckEmail(authService, request);
                    break;
                case "resetCredentials":
                    response = handleResetCredentials(authService, request);
                    break;

                default:
                    response = new Response(false, "Action not supported: " + request.getAction(), null, null);
            }

            // Send response back to client
            if (response != null) {
                out.println(gson.toJson(response));
            } else {
                out.println(gson.toJson(new Response(false, "Error processing request", null, null)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Matchmaking handlers
    private Response handleMatchPlayers(MatchmakingService matchmakingService, Request request) {
        String player1 = request.getUsername();
        String player2 = request.getPassword();
        String gameType = request.getExtra();

        MatchmakingService.MatchResult result = matchmakingService.matchPlayers(player1, player2, gameType);
        return new Response(result.isSuccess(), result.getMessage(), null, null);
    }

    private Response handleQueueForGame(MatchmakingService matchmakingService, Request request) {
        String player = request.getUsername();
        String gameType = request.getPassword();

        MatchmakingService.QueueResult result = matchmakingService.queueForGame(player, gameType);
        return new Response(result.isSuccess(), result.getMessage(), null, null);
    }

    private Response handleFindMatch(MatchmakingService matchmakingService, Request request) {
        String player = request.getUsername();
        String gameType = request.getPassword();

        MatchmakingService.FindMatchResult result = matchmakingService.findMatch(player, gameType);

        if (result.isSuccess() && result.getPlayer1() != null && result.getPlayer2() != null) {
            // Format matched players data for response
            Map<String, String> matchData = new HashMap<>();
            matchData.put("player1", result.getPlayer1());
            matchData.put("player2", result.getPlayer2());
            String matchDataJson = new Gson().toJson(matchData);

            return new Response(true, result.getMessage(), result.getPlayer1(), matchDataJson);
        } else {
            return new Response(result.isSuccess(), result.getMessage(), null, null);
        }
    }
    // Handle check email action
    private Response handleCheckEmail(AuthenticationService authService, Request request) {
        String email = request.getExtra(); // Email is stored in the extra field

        // Call the AuthenticationService to check the email
        AuthenticationService.EmailCheckResult result = authService.checkEmail(email);

        return new Response(result.isSuccess(), result.getMessage(), null, null);
    }

    // Handle reset credentials action
    private Response handleResetCredentials(AuthenticationService authService, Request request) {
        String newUsername = request.getUsername();
        String newPassword = request.getPassword();
        String email = request.getExtra();

        // Call the AuthenticationService to reset the credentials
        AuthenticationService.CredentialResetResult result =
                authService.resetCredentials(newUsername, newPassword, email);

        return new Response(result.isSuccess(), result.getMessage(), null, null);
    }

    // Handle login action
    private Response handleLogin(AuthenticationService authService, Request request) {
        AuthenticationService.LoginResult loginResult = authService.login(request.getUsername(), request.getPassword());
        return new Response(loginResult.isSuccess(), loginResult.getMessage(), loginResult.getUsername(), String.valueOf(loginResult.requiresMfa()));
    }

    // Handle guest login action
    private Response handleGuestLogin(AuthenticationService authService) {
        AuthenticationService.LoginResult guestResult = authService.guestLogin();
        return new Response(guestResult.isSuccess(), guestResult.getMessage(), guestResult.getUsername(), null);
    }

    // Handle sign up action
    private Response handleSignUp(AuthenticationService authService, Request request) {
        AuthenticationService.RegistrationResult result = authService.register(
                request.getUsername(),
                request.getPassword(),
                request.getConfirmPassword(),
                request.getEmail()
        );
        return new Response(result.isSuccess(), result.getMessage(), request.getUsername(), null);
    }

    // Handle MFA code generation action
    private Response handleGenerateMfaCode(AuthenticationService authService, Request request) {
        AuthenticationService.MfaCodeResult mfaResult = authService.generateMfaCode(request.getUsername());
        return new Response(mfaResult.isSuccess(), mfaResult.getMessage(), mfaResult.getCode(), String.valueOf(mfaResult.getGenerationTime()));
    }

    // Handle MFA verification action
    private Response handleVerifyMfaCode(AuthenticationService authService, Request request) {
        String username = request.getUsername();
        String code = request.getPassword();
        long generationTime = 0;

        try {
            generationTime = Long.parseLong(request.getExtra());
        } catch (NumberFormatException e) {
            return new Response(false, "Invalid generation time format", null, null);
        }

        AuthenticationService.MfaVerifyResult mfaVerifyResult = authService.verifyMfaCode(
                username, code, code, generationTime);
        return new Response(mfaVerifyResult.isSuccess(), mfaVerifyResult.getMessage(), null, null);
    }

    // Handle get user profile action
    private Response handleGetUserProfile(AuthenticationService authService, Request request) {
        AuthenticationService.ProfileResult profileResult = authService.getUserProfile(request.getUsername());
        return new Response(profileResult.isSuccess(), profileResult.getMessage(), profileResult.getEmail(),
                String.valueOf(profileResult.isMfaEnabled()));
    }
    

    // Handle check account lock action
    private Response handleCheckAccountLock(AuthenticationService authService, Request request) {
        AuthenticationService.AccountLockResult lockResult = authService.checkAccountLock(request.getUsername());
        return new Response(lockResult.isLocked(), lockResult.getMessage(), null, null);
    }

    // Handle logout action
    private Response handleLogout(AuthenticationService authService, Request request) {
        AuthenticationService.LogoutResult logoutResult = authService.logout(request.getUsername());
        return new Response(logoutResult.isSuccess(), logoutResult.getMessage(), null, null);
    }

    // Handle delete account action
    private Response handleDeleteAccount(AuthenticationService authService, Request request) {
        AuthenticationService.AccountDeletionResult deletionResult =
                authService.deleteAccount(request.getUsername(), request.getPassword());
        return new Response(deletionResult.isSuccess(), deletionResult.getMessage(), null, null);
    }

    // Handle update username action
    private Response handleUpdateUsername(AuthenticationService authService, Request request) {
        try {
            String currentUsername = request.getUsername();
            String newUsername = request.getPassword();

            // This would need to be added to the AuthenticationService
            // For now we'll simulate the functionality
            boolean success = true;
            String message = "Username updated successfully";

            return new Response(success, message, newUsername, null);
        } catch (Exception e) {
            return new Response(false, "Error updating username: " + e.getMessage(), null, null);
        }
    }

    // Handle update email action
    private Response handleUpdateEmail(AuthenticationService authService, Request request) {
        try {
            String username = request.getUsername();
            String newEmail = request.getPassword();

            // This would need to be added to the AuthenticationService
            // For now we'll simulate the functionality
            boolean success = true;
            String message = "Email updated successfully";

            return new Response(success, message, newEmail, null);
        } catch (Exception e) {
            return new Response(false, "Error updating email: " + e.getMessage(), null, null);
        }
    }

    // Handle update password action
    private Response handleUpdatePassword(AuthenticationService authService, Request request) {
        try {
            String username = request.getUsername();
            String newPassword = request.getPassword();

            // This would need to be added to the AuthenticationService
            // For now we'll simulate the functionality
            boolean success = true;
            String message = "Password updated successfully";

            return new Response(success, message, null, null);
        } catch (Exception e) {
            return new Response(false, "Error updating password: " + e.getMessage(), null, null);
        }
    }

    // Handle toggle MFA action
    private Response handleToggleMfa(AuthenticationService authService, Request request) {
        try {
            String username = request.getUsername();
            boolean enableMfa = Boolean.parseBoolean(request.getPassword());

            // This would need to be added to the AuthenticationService
            // For now we'll simulate the functionality
            boolean success = true;
            String message = enableMfa ? "MFA enabled successfully" : "MFA disabled successfully";

            return new Response(success, message, null, String.valueOf(enableMfa));
        } catch (Exception e) {
            return new Response(false, "Error toggling MFA: " + e.getMessage(), null, null);
        }
    }

    // Leaderboard handlers

    private Response handleGetLeaderboard(LeaderboardService leaderboardService, Request request) {
        String gameType = request.getUsername(); // Using username field for gameType
        LeaderboardService.LeaderboardResult result = leaderboardService.getLeaderboard(gameType);
        return new Response(result.isSuccess(), result.getMessage(), null, result.getLeaderboardJson());
    }

    private Response handleGetPlayerStats(LeaderboardService leaderboardService, Request request) {
        String username = request.getUsername();
        LeaderboardService.PlayerStatsResult result = leaderboardService.getPlayerStats(username);
        return new Response(result.isSuccess(), result.getMessage(), username, result.getStatsJson());
    }

    private Response handleGetMatchHistory(LeaderboardService leaderboardService, Request request) {
        String username = request.getUsername();
        LeaderboardService.MatchHistoryResult result = leaderboardService.getMatchHistory(username);
        return new Response(result.isSuccess(), result.getMessage(), username, result.getMatchesJson());
    }

    private Response handleUpdatePlayerStats(LeaderboardService leaderboardService, Request request) {
        String player1 = request.getUsername();
        String player2 = request.getPassword();
        String extraData = request.getExtra();

        LeaderboardService.StatsUpdateResult result = leaderboardService.updatePlayerStats(player1, player2, extraData);
        return new Response(result.isSuccess(), result.getMessage(), null, null);
    }

    private Response handleGetLeaderboardPositions(LeaderboardService leaderboardService, Request request) {
        String username = request.getUsername();
        LeaderboardService.LeaderboardPositionResult result = leaderboardService.getLeaderboardPositions(username);
        return new Response(result.isSuccess(), result.getMessage(), username, result.getPositionsJson());
    }
}