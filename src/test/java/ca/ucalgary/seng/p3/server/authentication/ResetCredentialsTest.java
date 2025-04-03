package ca.ucalgary.seng.p3.server.authentication;

import org.junit.*;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;

public class ResetCredentialsTest {
    private static final String TEST_FILE = "test_accounts.csv";

    @Before
    public void setUp() throws IOException {
        // Ensure accounts.csv contains test data
        File file = new File("accounts.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("testuser,OldPassword123,test@example.com\n");
            writer.write("user2,Password456,user2@example.com\n");
        }
    }

    @After
    public void tearDown() {
        // Delete the test file to clean up after the test
        new File("accounts.csv").delete();
    }

    @Test
    public void testIsValidPassword() {
        assertTrue(ResetCredentials.isValidPassword("Strong1Password"));
        assertFalse(ResetCredentials.isValidPassword("weak"));
        assertFalse(ResetCredentials.isValidPassword("NoNumberHere"));
        assertFalse(ResetCredentials.isValidPassword("nospaces123"));
        assertFalse(ResetCredentials.isValidPassword("12345678"));
    }

    @Test
    public void testIsUsernameTaken() {
        List<String[]> accounts = Arrays.asList(
                new String[]{"testuser", "password", "test@example.com"},
                new String[]{"user2", "password", "user2@example.com"}
        );
        assertTrue(ResetCredentials.isUsernameTaken(accounts, "testuser"));
        assertFalse(ResetCredentials.isUsernameTaken(accounts, "newuser"));
    }

    @Test
    public void testLoadAccounts() {
        List<String[]> accounts = ResetCredentials.loadAccounts();
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        assertEquals(2, accounts.size());
    }

    @Test
    public void testSaveAccounts() {
        List<String[]> accounts = new ArrayList<>();
        accounts.add(new String[]{"newuser", "NewPass123", "new@example.com"});
        ResetCredentials.saveAccounts(accounts);

        List<String[]> loadedAccounts = ResetCredentials.loadAccounts();
        assertEquals(1, loadedAccounts.size());
        assertEquals("newuser", loadedAccounts.get(0)[0]);
    }

    @Test
    public void testFindAccountByEmail() {
        List<String[]> accounts = ResetCredentials.loadAccounts();
        String[] account = ResetCredentials.findAccountByEmail(accounts, "test@example.com");
        assertNotNull(account);
        assertEquals("testuser", account[0]);
        assertEquals("OldPassword123", account[1]);
        assertNull(ResetCredentials.findAccountByEmail(accounts, "nonexistent@example.com"));
    }

    @Test
    public void testGenerateOneTimeCode() {
        String code = ResetCredentials.generateOneTimeCode(6);
        assertEquals(6, code.length());
        assertTrue(code.matches("\\d{6}"));
    }


    @Test
    public void testResetPasswordInvalidEmail() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("invalid@example.com\n".getBytes()));
        ResetCredentials.resetPassword(scanner);
    }

    @Test
    public void testResetUsernameInvalidEmail() {
        Scanner scanner = new Scanner(new ByteArrayInputStream("invalid@example.com\n".getBytes()));
        ResetCredentials.resetUsername(scanner);
    }

    @Test
    public void testVerifyEmailWithCode() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        Scanner scanner = new Scanner(new ByteArrayInputStream("wrong\nwrong\nwrong\n".getBytes()));
        boolean result = ResetCredentials.verifyEmailWithCode(scanner, "test@example.com");

        System.setOut(originalOut);
        assertFalse(result);
    }
}
