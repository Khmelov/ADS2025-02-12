package by.it.group451003.yepanchuntsau.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/*
 Lesson 3. B_Huffman.
 Восстановите строку по её коду и беспрефиксному коду символов.

 В первой строке входного файла заданы два целых числа
 k и l через пробел — количество различных букв, встречающихся в строке,
 и размер получившейся закодированной строки, соответственно.

 В следующих k строках записаны коды букв в формате "letter: code".
 Ни один код не является префиксом другого.
 Буквы могут быть перечислены в любом порядке.
 В качестве букв могут встречаться лишь строчные буквы латинского алфавита;
 каждая из этих букв встречается в строке хотя бы один раз.
 Наконец, в последней строке записана закодированная строка.
 Исходная строка и коды всех букв непусты.
 Заданный код таков, что закодированная строка имеет минимальный возможный размер.

        Sample Input 1:
        1 1
        a: 0
        0

        Sample Output 1:
        a

        Sample Input 2:
        4 14
        a: 0
        b: 10
        c: 110
        d: 111
        01001100100111

        Sample Output 2:
        abacabad
*/

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение

        // 1. Считываем k и l (уже сделали выше: count и length)
        // 2. Читаем k строк с кодами в map: code→letter
        Map<String, Character> codes = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String letterToken = scanner.next();
            char letter = letterToken.charAt(0);
            String code = scanner.next();
            codes.put(code, letter);
        }

        // 3. Считываем закодированную строку
        String encoded = scanner.next();

        // 4. Проходим по битам, накапливаем в current, пока не найдём соответствие в map
        StringBuilder current = new StringBuilder();
        for (char bit : encoded.toCharArray()) {
            current.append(bit);
            String curStr = current.toString();
            if (codes.containsKey(curStr)) {
                result.append(codes.get(curStr));
                current.setLength(0);
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString();
    }

}
