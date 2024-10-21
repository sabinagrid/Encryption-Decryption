package guru.springframework.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Map<String, String> argMap = parseArgs(args);

        String mode = argMap.getOrDefault("mode", "enc");
        String data = argMap.getOrDefault("data", "");
        int key = Integer.parseInt(argMap.getOrDefault("key", "0"));
        String inFile = argMap.get("in");
        String outFile = argMap.get("out");
        String algorithm = argMap.getOrDefault("alg", "shift");

        if (data.isEmpty() && inFile != null) {
            try {
                data = Files.readString(Path.of(inFile));
            } catch (IOException e) {
                System.out.println("Error");
                return;
            }
        }

        String result;
        if ("unicode".equals(algorithm)) {
            result = processUnicode(data, key, mode);
        } else {
            result = processShift(data, key, mode);
        }

        if (outFile != null) {
            try {
                Files.writeString(Path.of(outFile), result);
            } catch (IOException e) {
                System.out.println("Error");
            }
        } else {
            System.out.println(result);
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> argMap = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            if (i + 1 < args.length) {
                argMap.put(args[i].substring(1), args[i + 1]);
            }
        }
        return argMap;
    }

    private static String processShift(String data, int key, String mode) {
        StringBuilder result = new StringBuilder();
        int shift = "dec".equals(mode) ? -key : key;
        for (char c : data.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                char shifted = (char) ((c - base + shift + 26) % 26 + base);
                result.append(shifted);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static String processUnicode(String data, int key, String mode) {
        StringBuilder result = new StringBuilder();
        int shift = "dec".equals(mode) ? -key : key;
        for (char c : data.toCharArray()) {
            result.append((char) (c + shift));
        }
        return result.toString();
    }
}
