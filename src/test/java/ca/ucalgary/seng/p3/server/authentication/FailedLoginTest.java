//package ca.ucalgary.seng.p3.server.authentication;
//
//import static org.junit.Assert.*;
//import org.junit.*;
//import java.io.*;
//
//public class FailedLoginTest {
//    private static final String CSV_FILE = "accounts.csv";
//    private FailedLogin failedLogin;
//
//    @Before
//    public void setUp() throws IOException {
//        // Create a temporary accounts.csv with known users
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
//            bw.write("user1,password1,user1@example.com");
//            bw.newLine();
//            bw.write("user2,password2,user2@example.com");
//            bw.newLine();
//        }
//        failedLogin = new FailedLogin();
//    }
//
//    @After
//    public void tearDown() {
//        // Delete the temporary CSV file
//        new File(CSV_FILE).delete();
//    }
//
//    @Test
//    public void testSuccessfulLogin() {
//        AuthResult result = failedLogin.authenticate("user1", "password1");
//        assertTrue(result.isSuccess());
//        assertTrue(result.getMessage().contains("Login successful"));
//    }
//
//    @Test
//    public void testFailedLogin() {
//        AuthResult result = failedLogin.authenticate("user1", "wrongPass");
//        assertFalse(result.isSuccess());
//        assertTrue(result.getMessage().contains("Invalid credentials"));
//    }
//
//    @Test
//    public void testInvalidCredentialsIncrementsAttempts() {
//        // First attempt
//        AuthResult firstAttempt = failedLogin.authenticate("user1", "wrongPass");
//        assertFalse(firstAttempt.isSuccess());
//        assertTrue(firstAttempt.getMessage().contains("4 attempt(s) left"));
//
//        // Second attempt
//        AuthResult secondAttempt = failedLogin.authenticate("user1", "wrongPass");
//        assertFalse(secondAttempt.isSuccess());
//        assertTrue(secondAttempt.getMessage().contains("3 attempt(s) left"));
//    }
//
//    @Test
//    public void testLockAfterMaxAttempts() {
//        // Use up all attempts
//        for (int i = 0; i < FailedLogin.MAX_ATTEMPTS; i++) {
//            AuthResult attempt = failedLogin.authenticate("user1", "wrongPass");
//            assertFalse(attempt.isSuccess());
//        }
//
//        // Next attempt should lock the account
//        AuthResult lockingAttempt = failedLogin.authenticate("user1", "wrongPass");
//        assertFalse(lockingAttempt.isSuccess());
//        assertTrue(lockingAttempt.getMessage().contains("Account locked"));
//    }
//
//    @Test
//    public void testLockedAccountRefusal() {
//        // Lock the account first
//        for (int i = 0; i < FailedLogin.MAX_ATTEMPTS; i++) {
//            failedLogin.authenticate("user1", "wrongPass");
//        }
//
//        // Even with correct password, should be rejected
//        AuthResult lockedResult = failedLogin.authenticate("user1", "password1");
//        assertFalse(lockedResult.isSuccess());
//        assertTrue(lockedResult.getMessage().contains("Account locked"));
//    }
//
//    @Test
//    public void testUnlockAccountAllowsLogin() {
//        // Lock the account first
//        for (int i = 0; i < FailedLogin.MAX_ATTEMPTS; i++) {
//            failedLogin.authenticate("user1", "wrongPass");
//        }
//
//        // Unlock account and verify success
//        assertTrue(failedLogin.unlockAccount("user1"));
//
//        // Should be able to login now
//        AuthResult resultAfterUnlock = failedLogin.authenticate("user1", "password1");
//        assertTrue(resultAfterUnlock.isSuccess());
//    }
//
//    @Test
//    public void testNonexistentUser() {
//        AuthResult result = failedLogin.authenticate("noSuchUser", "anyPass");
//        assertFalse(result.isSuccess());
//        assertTrue(result.getMessage().contains("Invalid credentials"));
//    }
//
//    @Test
//    public void testResetAttemptsAfterSuccessfulLogin() {
//        // First wrong attempt
//        failedLogin.authenticate("user1", "wrongPass");
//
//        // Then successful login
//        AuthResult successResult = failedLogin.authenticate("user1", "password1");
//        assertTrue(successResult.isSuccess());
//
//        // Next wrong attempt should show 4 attempts left (reset counter)
//        AuthResult nextWrongAttempt = failedLogin.authenticate("user1", "wrongPass");
//        assertTrue(nextWrongAttempt.getMessage().contains("4 attempt(s) left"));
//    }
//
//    @Test
//    public void testUnlockNonexistentAccount() {
//        // Attempt to unlock account that doesn't exist
//        assertFalse(failedLogin.unlockAccount("nonexistentUser"));
//    }
//
//    @Test
//    public void testUnlockNonLockedAccount() {
//        // Unlock account that isn't locked
//        assertTrue(failedLogin.unlockAccount("user1"));
//    }
//}