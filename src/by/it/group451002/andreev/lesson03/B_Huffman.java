package by.it.group451002.andreev.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        // Создаем поток ввода из файла dataB.txt
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        // Декодируем строку и выводим результат
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);

        // Читаем количество различных букв и длину закодированной строки
        int count = scanner.nextInt();
        int length = scanner.nextInt();
        scanner.nextLine(); // Поглощаем оставшийся перевод строки

        // Создаем карту для хранения соответствия код -> символ
        Map<String, Character> codeToChar = new HashMap<>();

        // Считываем буквы и их коды
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(": ");
            char letter = parts[0].charAt(0); // Извлекаем букву
            String code = parts[1]; // Извлекаем код
            codeToChar.put(code, letter); // Добавляем соответствие в карту
        }

        // Читаем закодированную строку
        String encodedString = scanner.nextLine();

        // Декодируем строку
        StringBuilder currentCode = new StringBuilder();
        for (char bit : encodedString.toCharArray()) {
            currentCode.append(bit);
            // Проверяем, есть ли такой код в карте
            if (codeToChar.containsKey(currentCode.toString())) {
                result.append(codeToChar.get(currentCode.toString())); // Добавляем символ в результат
                currentCode.setLength(0); // Очищаем код для поиска следующего символа
            }
        }

        return result.toString(); // Возвращаем декодированную строку
    }
}
