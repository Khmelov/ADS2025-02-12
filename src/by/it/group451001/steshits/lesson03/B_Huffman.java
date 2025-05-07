package by.it.group451001.steshits.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
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

    private static class Node {
        Node left;
        Node right;
        Character character;
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        scanner.nextLine();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение
        Node a = new Node();
        for (int i = 0; i < count; i++) {
            String s = scanner.nextLine().trim();
            String[] parts = s.split(": ");
            char letter = parts[0].charAt(0);
            String code = parts[1];
            Node curr = a;
            for (int j = 0; j < code.length(); j++) {
                char bit = code.charAt(j);
                if (bit == '0') {
                    if (curr.left == null) {
                        curr.left = new Node();
                    }
                    curr = curr.left;
                } else {
                    if (curr.right == null) {
                        curr.right = new Node();
                    }
                    curr = curr.right;
                }
            }
            curr.character = letter;
        }
        String encode = scanner.nextLine();
        Node curr = a;
        for (char bit : encode.toCharArray()) {
            if (bit == '0') {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
            if (curr.character != null) {
                result.append(curr.character);
                curr = a;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }


}
