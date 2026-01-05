package by.it.group410902.varava.lesson02.lesson01.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Lesson 3. B_Huffman.
// Восстановите строку по её коду и беспрефиксному коду символов.

// В первой строке входного файла заданы два целых числа
// k и l через пробел — количество различных букв, встречающихся в строке,
// и размер получившейся закодированной строки, соответственно.
//
// В следующих k строках записаны коды букв в формате "letter: code".
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
//
//        Sample Output 1:
//        a
//
//        Sample Input 2:
//        4 14
//        a: 0
//        b: 10
//        c: 110
//        d: 111
//        01001100100111
//
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
        Scanner scanner = new Scanner(inputStream);

        // Читаем количество символов и длину закодированной строки
        int count = scanner.nextInt();
        int length = scanner.nextInt();
        scanner.nextLine(); // Пропускаем оставшийся перевод строки

        // Создаем карту для хранения кодировок
        Map<String, Character> huffmanCodes = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String[] line = scanner.nextLine().split(": ");
            huffmanCodes.put(line[1], line[0].charAt(0)); // "код" -> "символ"
        }

        // Читаем закодированную строку
        String encodedString = scanner.nextLine();
        StringBuilder currentCode = new StringBuilder();

        // Декодируем строку
        for (char c : encodedString.toCharArray()) {
            currentCode.append(c);
            if (huffmanCodes.containsKey(currentCode.toString())) {
                result.append(huffmanCodes.get(currentCode.toString()));
                currentCode.setLength(0); // Очищаем код после обработки
            }
        }

        return result.toString();
    }
}
