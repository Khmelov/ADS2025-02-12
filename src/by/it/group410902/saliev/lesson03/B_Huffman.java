package by.it.group410902.saliev.lesson03;

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
//
//        Sample Output 1:
//        a
//
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
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //тут запишите ваше решение

        scanner.nextLine(); // переход на новую строку после чисел

        // 1. Сохраняем коды букв (code -> symbol)
        Map<String, Character> codeMap = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine(); // например: "a: 0"
            String[] parts = line.split(": ");
            codeMap.put(parts[1], parts[0].charAt(0));
        }

        // 2. Считываем закодированную строку
        String encoded = scanner.nextLine();

        // 3. Постепенно собираем символы по коду
        StringBuilder currentCode = new StringBuilder();
        for (char bit : encoded.toCharArray()) {
            currentCode.append(bit);
            if (codeMap.containsKey(currentCode.toString())) {
                result.append(codeMap.get(currentCode.toString()));
                currentCode.setLength(0); // очищаем буфер
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return result.toString(); //01001100100111
    }

}
