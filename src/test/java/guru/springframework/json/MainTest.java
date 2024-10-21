package guru.springframework.json;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testShiftEncryption() {
        String data = "hello";
        int key = 2;
        String expected = "jgnnq";
        String result = Main.processShift(data, key, "enc");
        assertEquals(expected, result);
    }

    @Test
    void testShiftDecryption() {
        String data = "jgnnq";
        int key = 2;
        String expected = "hello";
        String result = Main.processShift(data, key, "dec");
        assertEquals(expected, result);
    }

    @Test
    void testUnicodeEncryption() {
        String data = "hello";
        int key = 1;
        String expected = "ifmmp";
        String result = Main.processUnicode(data, key, "enc");
        assertEquals(expected, result);
    }

    @Test
    void testUnicodeDecryption() {
        String data = "ifmmp";
        int key = 1;
        String expected = "hello";
        String result = Main.processUnicode(data, key, "dec");
        assertEquals(expected, result);
    }

    @Test
    void testParseArgs() {
        String[] args = {"-mode", "enc", "-key", "5", "-data", "hello"};
        Map<String, String> argMap = Main.parseArgs(args);
        assertEquals("enc", argMap.get("mode"));
        assertEquals("5", argMap.get("key"));
        assertEquals("hello", argMap.get("data"));
    }

    @Test
    void testNoKeyProvided() {
        String[] args = {"-mode", "enc", "-data", "Hello"};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        Main.main(args);

        System.setOut(originalOut);
        assertEquals("Hello\n", outputStream.toString());  // No key means default shift by 0
    }

    @Test
    void testInvalidMode() {
        String[] args = {"-mode", "invalid", "-key", "5", "-data", "Hello"};
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        Main.main(args);

        System.setOut(originalOut);
        assertEquals("Error\n", outputStream.toString());
    }

}