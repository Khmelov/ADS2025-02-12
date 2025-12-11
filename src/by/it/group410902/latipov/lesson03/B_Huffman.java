package by.it.group410902.latipov.lesson03;

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

        // Читаем количество символов и длину закодированной строки
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

        // Создаем карту для хранения кодов символов (код -> символ)
        Map<String, Character> codeToChar = new HashMap<>();

        // Читаем коды символов
        scanner.nextLine(); // переходим на следующую строку
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            // Разбираем строку формата "letter: code"
            String[] parts = line.split(": ");
            char character = parts[0].charAt(0);
            String code = parts[1];
            codeToChar.put(code, character);
        }

        // Читаем закодированную строку
        String encodedString = scanner.nextLine();

        // Декодируем строку
        StringBuilder currentCode = new StringBuilder();
        for (int i = 0; i < encodedString.length(); i++) {
            currentCode.append(encodedString.charAt(i));
            String code = currentCode.toString();

            // Если текущая последовательность битов соответствует какому-то коду
            if (codeToChar.containsKey(code)) {
                result.append(codeToChar.get(code));
                currentCode.setLength(0); // сбрасываем текущий код
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString();
    }
}