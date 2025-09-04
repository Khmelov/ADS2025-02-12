package by.it.group451004.akbulatov.lesson03;

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
        Map<String, Character> codeSet = new HashMap<>();
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);

        int count = scanner.nextInt();
        int length = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < count; ++i) {
            char letter = scanner.next().charAt(0);
            String code = scanner.next();
            codeSet.put(code, letter);
            scanner.nextLine();
        }
        StringBuilder code = new StringBuilder();
        String secretStr = scanner.next();
        for(char i : secretStr.toCharArray()) {
            code.append(i);
            if (codeSet.containsKey(code.toString())){
                result.append(codeSet.get(code.toString()));
                code.delete(0, code.length());
            }
        }

        return result.toString();
    }
}
