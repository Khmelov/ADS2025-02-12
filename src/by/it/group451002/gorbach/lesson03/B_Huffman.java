package by.it.group451002.gorbach.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Lesson 3. B_Huffman.
// Восстановите строку по её коду и беспрефиксному коду символов.

// В первой строке входного файла заданы два целых числа
// kk и ll через пробел — количество различных букв, встречающихся в строке,
// и размер получившейся закодированной строки, соответственно.
//
// В следующих kk строках записаны коды букв в формате "letter: code".
// Ни один код не является префиксом другого.
// Буквы могут быть перечислены в любом порядке.
// В качестве букв могут встречаться лишь строчные буквы латинского алфавита;
// каждая из этих букв встречается в строке хотя бы один раз.
// Наконец, в последней строке записана закодированная строка.
// Исходная строка и коды всех букв непусты.
// Заданный код таков, что закодированная строка имеет минимальный возможный размер.
//
//        Sample Input 1:
//        1 1
//        a: 0
//        0

//        Sample Output 1:
//        a


//        Sample Input 2:
//        4 14
//        a: 0
//        b: 10
//        c: 110
//        d: 111
//        01001100100111

//        Sample Output 2:
//        abacabad

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение

        try {
            // Проверка наличия данных
            if (!scanner.hasNext()) {
                throw new IllegalArgumentException("Файл пуст");
            }

            // Чтение количества символов и длины закодированной строки
            int count = scanner.nextInt();
            int length = scanner.nextInt();
            scanner.nextLine(); // Переход на следующую строку

            // Карта для хранения соответствия кодов символам
            Map<String, Character> codeToChar = new HashMap<>();

            // Чтение кодов символов
            for (int i = 0; i < count; i++) {
                if (!scanner.hasNextLine()) {
                    throw new IllegalArgumentException("Недостаточно строк с кодами символов");
                }
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(": ");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Некорректный формат строки с кодом: " + line);
                }

                char character = parts[0].charAt(0);
                String code = parts[1];
                codeToChar.put(code, character);
            }

            // Проверка наличия закодированной строки
            if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException("Отсутствует закодированная строка");
            }
            String encodedString = scanner.nextLine().trim();

            // Проверка соответствия длины закодированной строки
            if (encodedString.length() != length) {
                throw new IllegalArgumentException(
                        "Ошибка: закодированная строка имеет длину " + encodedString.length() +
                                ", но ожидалось " + length
                );
            }

            // Декодирование строки
            StringBuilder currentCode = new StringBuilder();
            for (int i = 0; i < encodedString.length(); i++) {
                currentCode.append(encodedString.charAt(i));
                Character character = codeToChar.get(currentCode.toString());
                if (character != null) {
                    result.append(character);
                    currentCode.setLength(0); // Сброс текущего кода
                }
            }

            // Проверка, что вся строка была декодирована
            if (currentCode.length() > 0) {
                throw new IllegalArgumentException(
                        "Ошибка: не удалось декодировать часть строки '" + currentCode + "'"
                );
            }

        } finally {
            scanner.close();
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }


}
