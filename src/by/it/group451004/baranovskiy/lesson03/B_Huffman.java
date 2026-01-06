package by.it.group451004.baranovskiy.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

// Урок 3. Алгоритм Хаффмана (декодирование)
// Восстановление исходной строки по её коду и таблице беспрефиксных кодов символов

// Формат входных данных:
// В первой строке - два числа: kk (количество различных букв) и ll (длина закодированной строки)
// В следующих kk строках - коды букв в формате "letter: code"
// В последней строке - закодированная строка

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение входного файла
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        // Декодирование и вывод результата
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        // Карта для хранения соответствий кодов символам (код -> символ)
        Map<String, Character> codes = new TreeMap<>();
        // Результирующая строка
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);

        // Чтение количества символов и длины закодированной строки
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();

        // Пропуск оставшейся части строки после чисел
        scanner.nextLine();

        // Чтение таблицы кодов
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            // Разделение строки на символ и его код
            String[] parts = line.split(": ");
            char ch = parts[0].charAt(0);  // Символ
            String code = parts[1];         // Его код

            // Добавление в карту соответствия кода символу
            codes.put(code, ch);
        }

        // Чтение закодированной строки
        StringBuilder codedString = new StringBuilder(scanner.nextLine());

        // Декодирование строки
        while (!codedString.isEmpty()) {
            StringBuilder subString = new StringBuilder();
            // Посимвольное чтение закодированной строки до нахождения валидного кода
            while (!codes.containsKey(subString.toString())) {
                subString.append(codedString.charAt(0));
                codedString.deleteCharAt(0);
            }
            // Добавление найденного символа в результат
            result.append(codes.get(subString.toString()));
        }

        return result.toString();
    }
}