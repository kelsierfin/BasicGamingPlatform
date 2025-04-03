package ca.ucalgary.seng.p3.server.authentication;

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

public class UserLoginTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File accountsFile;
    private File sessionFile;

    @Before
    public void setUp() throws IOException {
        accountsFile = new File("accounts.csv");
        sessionFile = new File("session.csv");

        // Ensure clean state before each test
        if (accountsFile.exists()) accountsFile.delete();
        if (sessionFile.exists()) sessionFile.delete();

        accountsFile.createNewFile();
        sessionFile.createNewFile();
    }

    @After
    public void tearDown() {
        if (accountsFile.exists()) accountsFile.delete();
        if (sessionFile.exists()) sessionFile.delete();
        System.setIn(System.in); // Reset input stream
    }

    @Test
    public void testLoadAccounts_EmptyFile() {
        Map<String, String> accounts = UserLogin.loadAccounts();
        assertTrue(accounts.isEmpty());
    }

    @Test
    public void testLoadAccounts_ValidEntries() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFile))) {
            writer.write("user1,password1,false\nuser2,password2,true\n");
        }
        Map<String, String> accounts = UserLogin.loadAccounts();
        assertEquals(2, accounts.size());
        assertEquals("password1", accounts.get("user1"));
        assertEquals("password2", accounts.get("user2"));
    }

    @Test
    public void testSaveSession() throws IOException {
        UserLogin.saveSession("testUser");
        try (BufferedReader reader = new BufferedReader(new FileReader(sessionFile))) {
            assertEquals("testUser", reader.readLine());
        }
    }

    @Test
    public void testGuestLogin() throws IOException {
        UserLogin.guestLogin();
        try (BufferedReader reader = new BufferedReader(new FileReader(sessionFile))) {
            String guestEntry = reader.readLine();
            assertTrue(guestEntry.startsWith("Guest"));
        }
    }

    @Test
    public void testLogin_Successful() throws IOException {
        // Write a valid account to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFile))) {
            writer.write("user1,password1,false\n"); // MFA disabled
        }

        String simulatedInput = "user1\npassword1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        UserLogin.login(new Scanner(System.in));

        // Verify session was saved
        try (BufferedReader reader = new BufferedReader(new FileReader(sessionFile))) {
            assertEquals("user1", reader.readLine());
        }
    }

    @Test
    public void testLogin_Failed() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFile))) {
            writer.write("user1,password1,false\n");
        }

        String simulatedInput = "user1\nwrongpassword\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        UserLogin.login(new Scanner(System.in));

        try (BufferedReader reader = new BufferedReader(new FileReader(sessionFile))) {
            assertNull(reader.readLine()); // No session should be saved
        }
    }

    @Test
    public void testLogin_WithMFA_Successful() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFile))) {
            writer.write("user1,password1,true\n"); // MFA enabled
        }

        String simulatedInput = "user1\npassword1\n123456\n"; // MFA code
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Simulate MFA process by assuming correct MFA code is entered
        UserLogin.login(new Scanner(System.in));

        try (BufferedReader reader = new BufferedReader(new FileReader(sessionFile))) {
            assertEquals("user1", reader.readLine());
        }
    }

    @Test
    public void testIsMFAEnabled_True() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFile))) {
            writer.write("user1,password1,email1,true\n");
        }
        assertTrue(UserLogin.isMFAEnabled("user1"));
    }

    @Test
    public void testIsMFAEnabled_False() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFile))) {
            writer.write("user1,password1,false\n");
        }
        assertFalse(UserLogin.isMFAEnabled("user1"));
    }

    @Test
    public void testMain_LoginOption() throws IOException {
        // Simulating user input: choosing "login", entering username/password, then exiting
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFile))) {
            writer.write("user1,password1,false\n");
        }

        String simulatedInput = "login\nuser1\npassword1\nexit\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        UserLogin.main(new String[]{});
    }
}
