package by.it.group410901.galitskiy.lesson03;

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

        // Чтение количества символов и длины закодированной строки
        int count = scanner.nextInt();
        int length = scanner.nextInt();
        scanner.nextLine(); // Переход на следующую строку

        // Создание map для хранения кодов символов
        Map<String, Character> codeToChar = new HashMap<>();

        // Чтение кодов символов
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(": ");
            char character = parts[0].charAt(0);
            String code = parts[1];
            codeToChar.put(code, character);
        }

        // Чтение закодированной строки
        String encodedString = scanner.nextLine();

        // Декодирование строки
        StringBuilder currentCode = new StringBuilder();
        for (int i = 0; i < encodedString.length(); i++) {
            currentCode.append(encodedString.charAt(i));
            if (codeToChar.containsKey(currentCode.toString())) {
                result.append(codeToChar.get(currentCode.toString()));
                currentCode.setLength(0); // Сброс текущего кода
            }
        }

        scanner.close();
        return result.toString();
    }
}