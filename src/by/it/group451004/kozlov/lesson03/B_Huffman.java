package by.it.group451004.kozlov.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

        Map<String, Character> codeToChar = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String line = scanner.next();
            if (line.endsWith(":")) {
                char letter = line.charAt(0);
                String code = scanner.next();
                codeToChar.put(code, letter);
            } else {

                int colonIndex = line.indexOf(':');
                char letter = line.substring(0, colonIndex).charAt(0);
                String code = line.substring(colonIndex + 1);
                codeToChar.put(code, letter);
            }
        }

        String encodedString = scanner.next();
        StringBuilder currentCode = new StringBuilder();
        for (char bit : encodedString.toCharArray()) {
            currentCode.append(bit);
            if (codeToChar.containsKey(currentCode.toString())) {
                result.append(codeToChar.get(currentCode.toString()));
                currentCode.setLength(0);
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString();
    }
}