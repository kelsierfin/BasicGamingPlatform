package authentication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class FailedLoginTest {
    private static final String CSV_FILE = "accounts.csv";
    private FailedLogin failedLogin;

    @Before
    public void setUp() throws IOException {
        // Create a temporary accounts.csv with known users
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            bw.write("user1,password1,user1@example.com");
            bw.newLine();
            bw.write("user2,password2,user2@example.com");
            bw.newLine();
        }
        failedLogin = new FailedLogin();
    }

    @After
    public void tearDown() {
        // Delete the temporary CSV file
        new File(CSV_FILE).delete();
    }

    @Test
    public void testSuccessfulLogin() {
        AuthResult result = failedLogin.authenticate("user1", "password1");
        assertTrue(result.getMessage().contains("Login successful"));
    }

    @Test
    public void testInvalidCredentialsIncrementsAttempts() {
        AuthResult firstAttempt = failedLogin.authenticate("user1", "wrongPass");
        assertFalse(firstAttempt.getMessage().contains("Login successful"));
        assertTrue(firstAttempt.getMessage().contains("4 attempt(s) left"));
    }

    @Test
    public void testLockAfterMaxAttempts() {
        for (int i = 0; i < 5; i++) {
            failedLogin.authenticate("user1", "wrongPass");
        }
        AuthResult result = failedLogin.authenticate("user1", "wrongPass");
        assertFalse(result.getMessage().contains("Login successful"));
        assertTrue(result.getMessage().contains("Account locked for 15 minutes"));
    }

    @Test
    public void testLockedAccountRefusal() {
        for (int i = 0; i < 5; i++) {
            failedLogin.authenticate("user1", "wrongPass");
        }
        AuthResult lockedResult = failedLogin.authenticate("user1", "password1");
        assertFalse(lockedResult.getMessage().contains("Login successful"));
        assertTrue(lockedResult.getMessage().contains("Account locked"));
    }

    @Test
    public void testUnlockAccountAllowsLogin() {
        for (int i = 0; i < 5; i++) {
            failedLogin.authenticate("user1", "wrongPass");
        }
        assertTrue(failedLogin.unlockAccount("user1"));
        AuthResult resultAfterUnlock = failedLogin.authenticate("user1", "password1");
        assertTrue(resultAfterUnlock.getMessage().contains("Login successful"));
    }

    @Test
    public void testNonexistentUser() {
        AuthResult result = failedLogin.authenticate("noSuchUser", "anyPass");
        assertFalse(result.getMessage().contains("Login successful"));
        assertTrue(result.getMessage().contains("Invalid credentials"));
    }
}
