package by.it.group410901.papou.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();

        // Read the letter-to-code mappings
        Map<String, Character> codeToLetter = new HashMap<>();
        scanner.nextLine(); // Consume the newline
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            // Split on ": " to separate letter and code
            String[] parts = line.split(": ");
            char letter = parts[0].charAt(0);
            String code = parts[1];
            codeToLetter.put(code, letter);
        }

        // Read the encoded string
        String encoded = scanner.nextLine();

        // Decode the string
        StringBuilder currentCode = new StringBuilder();
        for (char bit : encoded.toCharArray()) {
            currentCode.append(bit);
            if (codeToLetter.containsKey(currentCode.toString())) {
                result.append(codeToLetter.get(currentCode.toString()));
                currentCode.setLength(0); // Reset the current code
            }
        }

        return result.toString();
    }
}