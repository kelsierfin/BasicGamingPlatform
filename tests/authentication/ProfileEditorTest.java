package authentication;

import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;

public class ProfileEditorTest {
    private static final String TEST_FILE = "test_accounts.csv";
    private ProfileEditor profileEditor;

    @Before
    public void createFile() throws IOException {
        profileEditor = new ProfileEditor(TEST_FILE);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TEST_FILE))) {
            bw.write("user1,pass1,user1@example.com");
            bw.newLine();
            bw.write("user2,pass2,user2@example.com");
            bw.newLine();
        }
    }

    @After
    public void deleteFile() {
        new File(TEST_FILE).delete();
    }

    @Test
    public void testLoadAccounts() throws IOException {
        Map<String, String[]> accounts = profileEditor.loadAccounts();
        assertEquals(2, accounts.size());
        assertArrayEquals(new String[]{"pass1", "user1@example.com"}, accounts.get("user1"));
        assertArrayEquals(new String[]{"pass2", "user2@example.com"}, accounts.get("user2"));
    }

    @Test
    public void testLoadAccounts_EmptyFile() throws IOException {
        new FileWriter(TEST_FILE).close();
        Map<String, String[]> accounts = profileEditor.loadAccounts();
        assertTrue(accounts.isEmpty());
    }

    @Test
    public void testLoadAccounts_NonexistentFile() throws IOException {
        new File(TEST_FILE).delete();
        Map<String, String[]> accounts = profileEditor.loadAccounts();
        assertTrue(accounts.isEmpty());
        assertTrue(new File(TEST_FILE).exists());
    }

    @Test
    public void testWriteAllAccounts() throws IOException {
        Map<String, String[]> accounts = new HashMap<>();
        accounts.put("test1", new String[]{"pw1", "e1@test.com"});
        accounts.put("test2", new String[]{"pw2", "e2@test.com"});

        profileEditor.writeAllAccounts(accounts);

        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TEST_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        assertEquals(2, lines.size());
        assertTrue(lines.contains("test1,pw1,e1@test.com"));
        assertTrue(lines.contains("test2,pw2,e2@test.com"));
    }

    @Test
    public void testGetAccountDetails_ExistingUser() throws IOException {
        String[] details = profileEditor.getAccountDetails("user1");
        assertNotNull(details);
        assertEquals("pass1", details[0]);
        assertEquals("user1@example.com", details[1]);
    }

    @Test
    public void testGetAccountDetails_NonExistingUser() throws IOException {
        String[] details = profileEditor.getAccountDetails("nonexistent");
        assertNull(details);
    }

    @Test
    public void testIsValidEmail_Valid() {
        assertTrue(profileEditor.isValidEmail("test@example.com"));
        assertTrue(profileEditor.isValidEmail("a@b.c"));
    }

    @Test
    public void testIsValidEmail_Invalid() {
        assertFalse(profileEditor.isValidEmail(""));
        assertFalse(profileEditor.isValidEmail("noat.com"));
        assertFalse(profileEditor.isValidEmail("@nouser.com"));
        assertFalse(profileEditor.isValidEmail("user@"));
    }

    @Test
    public void testIsValidPassword_Valid() {
        assertTrue(profileEditor.isValidPassword("ValidPass1"));
        assertTrue(profileEditor.isValidPassword("A1bcdefg"));
    }

    @Test
    public void testIsValidPassword_Invalid() {
        assertFalse(profileEditor.isValidPassword("short"));
        assertFalse(profileEditor.isValidPassword("no numbers"));
        assertFalse(profileEditor.isValidPassword("12345678"));
        assertFalse(profileEditor.isValidPassword("with space 1"));
    }

    @Test
    public void testUpdateUsername_Success() throws IOException {
        boolean result = profileEditor.updateUsername("user1", "newuser");
        assertTrue(result);

        String[] details = profileEditor.getAccountDetails("newuser");
        assertNotNull(details);
        assertEquals("pass1", details[0]);
        assertNull(profileEditor.getAccountDetails("user1"));
    }

    @Test
    public void testUpdateUsername_AlreadyExists() throws IOException {
        boolean result = profileEditor.updateUsername("user1", "user2");
        assertFalse(result);
    }

    @Test
    public void testUpdateEmail_Success() throws IOException {
        profileEditor.updateEmail("user1", "new@email.com");
        String[] details = profileEditor.getAccountDetails("user1");
        assertEquals("new@email.com", details[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEmail_InvalidEmail() throws IOException {
        profileEditor.updateEmail("user1", "invalid-email");
    }

    @Test
    public void testUpdatePassword_Success() throws IOException {
        profileEditor.updatePassword("user1", "NewPass1");
        String[] details = profileEditor.getAccountDetails("user1");
        assertEquals("NewPass1", details[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePassword_InvalidPassword() throws IOException {
        profileEditor.updatePassword("user1", "invalid");
    }

    @Test
    public void testGetEmail_Masking() {
        assertEquals("t****t@example.com", profileEditor.getEmail("test@example.com"));
        assertEquals("a****b@domain.com", profileEditor.getEmail("ab@domain.com"));
        assertEquals("a****@domain.com", profileEditor.getEmail("a@domain.com"));
        assertEquals("****", profileEditor.getEmail("invalid-email"));
        assertEquals("", profileEditor.getEmail(""));
    }
}