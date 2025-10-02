package by.it.group410902.andala.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение входных данных из файла
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        // Декодирование и вывод результата
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    /**
     * Метод для декодирования строки, закодированной алгоритмом Хаффмана
     * @param inputStream поток ввода с данными
     * @return раскодированная строка
     */
    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder(); // Для накопления результата
        Scanner scanner = new Scanner(inputStream);

        // Чтение количества уникальных символов и длины закодированной строки
        int count = scanner.nextInt();    // Количество уникальных символов
        int length = scanner.nextInt();   // Длина закодированной строки
        scanner.nextLine();               // Переход на следующую строку

        // 1. ПОСТРОЕНИЕ ТАБЛИЦЫ КОДОВ (code -> symbol)
        Map<String, Character> codeTable = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();       // Чтение строки вида "a: 0"
            String[] parts = line.split(": ");     // Разделение на символ и код
            char symbol = parts[0].charAt(0);      // Извлечение символа
            String code = parts[1];               // Извлечение кода
            codeTable.put(code, symbol);          // Добавление в таблицу
        }

        // 2. ДЕКОДИРОВАНИЕ СТРОКИ
        String encodedString = scanner.nextLine();  // Чтение закодированной строки
        StringBuilder currentCode = new StringBuilder(); // Текущий накапливаемый код

        for (char bit : encodedString.toCharArray()) {
            currentCode.append(bit); // Добавляем очередной бит к текущему коду

            // Проверяем, есть ли такой код в таблице
            if (codeTable.containsKey(currentCode.toString())) {
                // Если найден - добавляем соответствующий символ к результату
                result.append(codeTable.get(currentCode.toString()));
                currentCode.setLength(0); // Сбрасываем текущий код
            }
        }

        return result.toString();
    }
}