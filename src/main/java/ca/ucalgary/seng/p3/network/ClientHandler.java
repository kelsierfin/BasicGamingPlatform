package ca.ucalgary.seng.p3.network;

import ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService;
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
            Response response = null;

            // Process the request based on action type
            switch (request.getAction()) {
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
}