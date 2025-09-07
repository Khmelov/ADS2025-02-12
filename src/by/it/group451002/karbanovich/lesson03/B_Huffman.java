package by.it.group451002.karbanovich.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

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
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        scanner.nextLine();

        // Если длина дешифруемой строки нулевая, то возвращаем нулевой результат
        if (length == 0) return "";

        // Создаем Map и добавляем туда символы и их коды
        Map<String, Character> map = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String[] parts = scanner.nextLine().split(": ");
            map.put(parts[1], parts[0].charAt(0));
        }

        // Считываем строку с кодом
        String code = scanner.nextLine();

        // Проходимся по дешифруемой строке и проверяем есть ли код/коды в Map, если есть, то добавляем символ
        // к расшифрованной строке
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            temp.append(code.charAt(i));
            if (map.containsKey(temp.toString())) {
                result.append(map.get(temp.toString()));
                temp.setLength(0);
            }
        }
        return result.toString();
    }
}