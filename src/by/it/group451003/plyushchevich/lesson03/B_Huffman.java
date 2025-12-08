package by.it.group451003.plyushchevich.lesson03;

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
        Scanner scanner = new Scanner(inputStream);
        int k = scanner.nextInt();       // число различных букв
        int l = scanner.nextInt();       // длина закодированной строки
        scanner.nextLine();              // перейдем на начало следующей строки

        // 1) читаем k строк вида "a: 010"
        Map<String, Character> codeToChar = new HashMap<>();
        for (int i = 0; i < k; i++) {
            String line = scanner.nextLine().trim();
            // разделяем по ": "
            // line[0] = буква, line.substring(3) = код
            char ch = line.charAt(0);
            String code = line.substring(3);
            codeToChar.put(code, ch);
        }

        // 2) читаем закодированную строку (длина l)
        String encoded = scanner.nextLine().trim();

        // 3) декодирование
        StringBuilder result = new StringBuilder();
        StringBuilder buffer = new StringBuilder();
        for (char bit : encoded.toCharArray()) {
            buffer.append(bit);
            String bufStr = buffer.toString();
            // если текущий буфер совпал с одним из кодов
            if (codeToChar.containsKey(bufStr)) {
                result.append(codeToChar.get(bufStr));
                buffer.setLength(0);  // очищаем буфер
            }
        }

        return result.toString();
    }
}