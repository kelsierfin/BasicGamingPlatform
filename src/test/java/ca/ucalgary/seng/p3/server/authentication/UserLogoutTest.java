package ca.ucalgary.seng.p3.server.authentication;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

public class UserLogoutTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File sessionFile;

    @Before
    public void setUp() throws IOException {
        sessionFile = new File("session.csv");
        if (sessionFile.exists()) sessionFile.delete();
        sessionFile.createNewFile();
    }

    @After
    public void tearDown() {
        if (sessionFile.exists()) sessionFile.delete();
        System.setIn(System.in);
    }

    @Test
    public void testLogout_CancelledByUser() {
        String simulatedInput = "no\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        UserLogout.logout();

        assertTrue(sessionFile.exists());
    }

    @Test
    public void testLogout_NoActiveSessions() {
        String simulatedInput = "yes\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        UserLogout.logout();

        assertTrue(sessionFile.exists());
    }

    @Test
    public void testLogout_WrongUsername() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sessionFile))) {
            writer.write("correctUser\n");
        }
        String simulatedInput = "yes\nwrongUser\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        UserLogout.logout();
        List<String> users = UserLogout.getSessionUsers();
        assertEquals(1, users.size());
        assertEquals("correctUser", users.get(0));
    }

    @Test
    public void testLogout_Success() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sessionFile))) {
            writer.write("user1\n");
            writer.flush();
        }
        assertEquals(1, UserLogout.getSessionUsers().size());
        String simulatedInput = "yes\nuser1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        try {
            UserLogout.logout();
        } catch (NoSuchElementException e) {
            System.out.println("Expected exception caught: " + e.getMessage());
        }
        assertTrue(UserLogout.getSessionUsers().isEmpty());
    }

    @Test
    public void testGetSessionUsers_EmptyFile() {
        List<String> users = UserLogout.getSessionUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    public void testGetSessionUsers_WithUsers() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sessionFile))) {
            writer.write("user1\nuser2\n");
        }

        List<String> users = UserLogout.getSessionUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains("user1"));
        assertTrue(users.contains("user2"));
    }

    @Test
    public void testClearSession_UserExists() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sessionFile))) {
            writer.write("user1\nuser2\n");
        }

        UserLogout.clearSession("user1");

        List<String> users = UserLogout.getSessionUsers();
        assertEquals(1, users.size());
        assertEquals("user2", users.get(0));
    }

    @Test
    public void testClearSession_UserNotFound() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sessionFile))) {
            writer.write("user1\n");
        }

        UserLogout.clearSession("user2");

        List<String> users = UserLogout.getSessionUsers();
        assertEquals(1, users.size());
        assertEquals("user1", users.get(0));
    }

    @Test
    public void testRedirectToLogin() {
        System.setIn(new ByteArrayInputStream("exit\n".getBytes()));
        UserLogout.redirectToLogin();
    }
}
