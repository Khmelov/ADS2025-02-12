package by.it.group410902.plekhova.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

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
        // прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);

        // Чтение количества букв и длины закодированной строки
        int count = scanner.nextInt();
        int length = scanner.nextInt();

        // Создание карты для хранения кодов символов
        Map<String, Character> codeMap = new HashMap<>();

        // Чтение кодов символов
        scanner.nextLine(); // Переход на следующую строку после считывания чисел
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine(); // Читаем всю строку
            char letter = line.charAt(0); // первая буква
            String code = line.substring(3); // код после ": "
            codeMap.put(code, letter); // добавляем в карту
        }

        // Чтение закодированной строки
        String encodedString = scanner.nextLine();

        // Декодирование закодированной строки
        StringBuilder currentCode = new StringBuilder();

        for (char bit : encodedString.toCharArray()) {
            currentCode.append(bit); // добавляем бит к текущему коду

            // Проверяем, есть ли текущий код в карте
            if (codeMap.containsKey(currentCode.toString())) {
                result.append(codeMap.get(currentCode.toString())); // добавляем соответствующий символ
                currentCode.setLength(0); // сбрасываем текущий код
            }
        }

        return result.toString(); //01001100100111
    }


}
