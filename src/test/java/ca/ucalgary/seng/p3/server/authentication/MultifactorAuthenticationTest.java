package ca.ucalgary.seng.p3.server.authentication;

import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;

public class MultifactorAuthenticationTest {
    private static final String CSV_FILE = "accounts.csv";
    private PrintStream originalOut;
    private ByteArrayOutputStream outputCapture;

    @Before
    public void setUp() throws IOException {
        // Create a temporary accounts.csv file with test accounts
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            bw.write("testuser,dummyPass,testuser@example.com");
            bw.newLine();
            bw.write("user2,password2,user2@example.com");
            bw.newLine();
        }

        // Setup output capture
        originalOut = System.out;
        outputCapture = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputCapture));
    }

    @After
    public void tearDown() {
        // Delete the temporary CSV file after tests
        new File(CSV_FILE).delete();

        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    public void testSuccessfulMFAProcess() throws IOException {
        // Get the generated MFA code
        String generatedCode = captureGeneratedMFACode("testuser");

        // Simulate user input with the correct code
        String simulatedInput = generatedCode + "\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Verify MFA succeeds with correct code
        boolean result = MultifactorAuthentication.startMFAProcess(scanner, "testuser");
        assertTrue(result);

        scanner.close();
    }

    @Test
    public void testFailedMFAProcess() throws IOException {
        // Generate code first (not used but needed for complete test)
        captureGeneratedMFACode("testuser");

        // Simulate three wrong attempts
        String simulatedInput = "wrong1\nwrong2\nwrong3\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Verify MFA fails with wrong codes
        boolean result = MultifactorAuthentication.startMFAProcess(scanner, "testuser");
        assertFalse(result);

        scanner.close();
    }

    @Test
    public void testMFASuccessOnSecondAttempt() throws IOException {
        // Get the generated MFA code
        String generatedCode = captureGeneratedMFACode("testuser");

        // Simulate wrong then correct input
        String simulatedInput = "wrong1\n" + generatedCode + "\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Verify MFA succeeds on second attempt
        boolean result = MultifactorAuthentication.startMFAProcess(scanner, "testuser");
        assertTrue(result);

        scanner.close();
    }

    @Test
    public void testMFAForNonexistentUser() throws IOException {
        // Simulate any input for nonexistent user
        String simulatedInput = "anycode\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Verify MFA fails for nonexistent user
        boolean result = MultifactorAuthentication.startMFAProcess(scanner, "nonexistent");
        assertFalse(result);

        scanner.close();
    }

    @Test
    public void testMFAWithEmptyInput() throws IOException {
        // Generate code first (not used but needed for complete test)
        captureGeneratedMFACode("testuser");

        // Simulate empty inputs
        String simulatedInput = "\n\n\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Verify MFA fails with empty inputs
        boolean result = MultifactorAuthentication.startMFAProcess(scanner, "testuser");
        assertFalse(result);

        scanner.close();
    }

    @Test
    public void testDifferentCodeForDifferentUsers() throws IOException {
        // Generate codes for two different users
        String code1 = captureGeneratedMFACode("testuser");
        resetOutputCapture();
        String code2 = captureGeneratedMFACode("user2");

        // Verify different users get different codes
        assertNotEquals(code1, code2);
    }

    @Test
    public void testMFACodeFormat() throws IOException {
        // Generate code and verify its format
        String code = captureGeneratedMFACode("testuser");

        assertNotNull(code);
        assertTrue(code.matches("\\d+"));
        assertEquals(6, code.length());
    }

    /**
     * Helper method to generate and capture MFA code from output
     */
    private String captureGeneratedMFACode(String username) throws IOException {
        resetOutputCapture();

        // Call the method that generates and displays the code
        MultifactorAuthentication.startMFAProcess(new Scanner(new ByteArrayInputStream("\n\n\n".getBytes())), username);

        // Extract code from output
        String output = outputCapture.toString();
        int index = output.indexOf("Your MFA code is:");
        if (index == -1) {
            return null;
        }

        int colon = output.indexOf(":", index);
        if (colon == -1) {
            return null;
        }

        int endLine = output.indexOf("\n", colon);
        if (endLine == -1) {
            endLine = output.length();
        }

        return output.substring(colon + 1, endLine).trim();
    }

    /**
     * Helper method to reset the output capture
     */
    private void resetOutputCapture() {
        outputCapture.reset();
    }
}