package by.it.group451003.halubionak.lesson03;

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
        System.out.println(result); // Печать расшифрованной строки
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);

        // Считываем количество символов и длину закодированной строки
        int symbolCount = scanner.nextInt(); // Сколько разных символов
        int encodedLength = scanner.nextInt(); // Длина закодированной строки
        scanner.nextLine(); // Переход на новую строку

        // Создаем карту: код -> символ
        Map<String, Character> codeMap = new HashMap<>();

        // Считываем строки вида: "a: 0", "b: 10" и т.д.
        for (int i = 0; i < symbolCount; i++) {
            String line = scanner.nextLine();       // пример: a: 0
            String[] parts = line.split(": ");      // разделяем по ": "
            char character = parts[0].charAt(0);    // символ (например 'a')
            String code = parts[1];                 // код (например "0")
            codeMap.put(code, character);           // кладём в карту
        }

        // Читаем всю закодированную строку (например "01001100100111")
        String encoded = scanner.nextLine();

        // Строим расшифровку по кусочкам
        StringBuilder current = new StringBuilder(); // Текущий кусочек кода
        StringBuilder decoded = new StringBuilder(); // Здесь будет результат

        // Перебираем каждый бит из закодированной строки
        for (char bit : encoded.toCharArray()) {
            current.append(bit); // Добавляем бит к текущему коду

            // Если собранный код уже есть в карте — расшифровываем
            if (codeMap.containsKey(current.toString())) {
                char decodedChar = codeMap.get(current.toString()); // Получаем символ
                decoded.append(decodedChar); // Добавляем его в результат
                current.setLength(0); // Очищаем текущий код для следующего символа
            }
        }

        // Возвращаем расшифрованную строку
        return decoded.toString();
    }
}
