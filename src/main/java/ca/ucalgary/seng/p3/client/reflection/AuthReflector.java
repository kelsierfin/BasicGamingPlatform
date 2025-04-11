package ca.ucalgary.seng.p3.client.reflection;

import ca.ucalgary.seng.p3.network.Request;
import ca.ucalgary.seng.p3.network.ClientSocketService;
import ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService.LoginResult;
import ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService.MfaCodeResult;
import ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService.MfaVerifyResult;
import ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService.ProfileResult;
import ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService.AccountLockResult;
import ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService.LogoutResult;
import ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService.AccountDeletionResult;
import ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService.RegistrationResult;

import ca.ucalgary.seng.p3.network.Response;

public class AuthReflector {
    private static AuthReflector instance;
    private final ClientSocketService socketService;

    public static AuthReflector getInstance() {
        if (instance == null) {
            instance = new AuthReflector();
        }
        return instance;
    }

    private AuthReflector() {
        this.socketService = ClientSocketService.getInstance();
    }

    // Handle login action
    public LoginResult login(String username, String password) {
        Request req = new Request("login", username, password);
        Response res = socketService.sendRequest(req);

        return new LoginResult(res.isSuccess(), res.getMessage(), res.getUsername(), Boolean.parseBoolean(res.getExtra()));
    }

    // Handle guest login action
    public LoginResult guestLogin() {
        Request req = new Request("guestLogin", "", "");
        Response res = socketService.sendRequest(req);

        return new LoginResult(res.isSuccess(), res.getMessage(), res.getUsername(), false);
    }

    // Handle sign up action
    public RegistrationResult signUp(String username, String password, String confirmPassword, String email) {
        Request req = new Request("signUp", username, password, confirmPassword, email);
        Response res = socketService.sendRequest(req);

        return new RegistrationResult(res.isSuccess(), res.getMessage());
    }

    // Handle generate MFA code action
    public MfaCodeResult generateMfaCode(String username) {
        Request req = new Request("generateMfaCode", username, "");
        Response res = socketService.sendRequest(req);

        return new MfaCodeResult(res.isSuccess(), res.getMessage(), res.getUsername(),
                res.getExtra() != null ? Long.parseLong(res.getExtra()) : 0);
    }

    // Handle verify MFA code action
    public MfaVerifyResult verifyMfaCode(String username, String code, String expectedCode, long generationTime) {
        // We need to pass both the code and the generation time to the server
        Request req = new Request("verifyMfaCode", username, code, String.valueOf(generationTime));
        Response res = socketService.sendRequest(req);

        return new MfaVerifyResult(res.isSuccess(), res.getMessage());
    }

    // Handle get user profile action
    public ProfileResult getUserProfile(String username) {
        Request req = new Request("getUserProfile", username, "");
        Response res = socketService.sendRequest(req);

        return new ProfileResult(res.isSuccess(), res.getMessage(), res.getUsername(), null,
                res.getExtra() != null ? Boolean.parseBoolean(res.getExtra()) : false);
    }

    // Handle check account lock action
    public AccountLockResult checkAccountLock(String username) {
        Request req = new Request("checkAccountLock", username, "");
        Response res = socketService.sendRequest(req);

        return new AccountLockResult(res.isSuccess(), res.getMessage());
    }

    // Handle logout action
    public LogoutResult logout(String username) {
        Request req = new Request("logout", username, "");
        Response res = socketService.sendRequest(req);

        return new LogoutResult(res.isSuccess(), res.getMessage());
    }

    // Handle delete account action
    public AccountDeletionResult deleteAccount(String username, String password) {
        Request req = new Request("deleteAccount", username, password);
        Response res = socketService.sendRequest(req);

        return new AccountDeletionResult(res.isSuccess(), res.getMessage());
    }

    // Handle update username
    public ProfileResult updateUsername(String currentUsername, String newUsername) {
        Request req = new Request("updateUsername", currentUsername, newUsername);
        Response res = socketService.sendRequest(req);

        return new ProfileResult(res.isSuccess(), res.getMessage(), newUsername, null, false);
    }

    // Handle update email
    public ProfileResult updateEmail(String username, String newEmail) {
        Request req = new Request("updateEmail", username, newEmail);
        Response res = socketService.sendRequest(req);

        return new ProfileResult(res.isSuccess(), res.getMessage(), newEmail, null, false);
    }

    // Handle update password
    public ProfileResult updatePassword(String username, String newPassword) {
        Request req = new Request("updatePassword", username, newPassword);
        Response res = socketService.sendRequest(req);

        return new ProfileResult(res.isSuccess(), res.getMessage(), null, null, false);
    }

    // Handle toggle MFA
    public ProfileResult toggleMfa(String username, boolean enable) {
        Request req = new Request("toggleMfa", username, String.valueOf(enable));
        Response res = socketService.sendRequest(req);

        return new ProfileResult(res.isSuccess(), res.getMessage(), null, null, enable);
    }
}