package by.it.group410902.skobyalko.lesson03;

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
        int k = scanner.nextInt();  // количество различных букв
        int l = scanner.nextInt();  // длина закодированной строки
        scanner.nextLine(); // перейти на следующую строку
        /// //////////////////////////////

        Map<String, Character> codeToChar = new HashMap<>();

        // Читаем k строк с кодами символов
        for (int i = 0; i < k; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(": ");
            char ch = parts[0].charAt(0);
            String code = parts[1];
            codeToChar.put(code, ch);
        }

        // Считываем закодированную строку
        String encoded = scanner.nextLine();

        // Раскодируем строку
        StringBuilder currentCode = new StringBuilder();
        for (char bit : encoded.toCharArray()) {
            currentCode.append(bit);
            if (codeToChar.containsKey(currentCode.toString())) {
                result.append(codeToChar.get(currentCode.toString()));
                currentCode.setLength(0); // сбрасываем временный буфер
            }
        }

        return result.toString();
        /// //////////////////.
    }
}

