package by.it.group451003.rashchenya.lesson03;

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
        Node zero;
        Node one;
        Character character;
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        Integer k = scanner.nextInt();
        Integer l = scanner.nextInt();
        scanner.nextLine();;
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        //тут запишите ваше решение

        Node root = new Node();

        for (int i = 0; i < k; i++) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split(": ");
            char symbol = parts[0].charAt(0);
            String code = parts[1];

            Node currentNode = root;
            for (int j = 0; j < code.length(); j++) {
                char bit = code.charAt(j);
                if (bit == '0') {
                    if (currentNode.zero == null) {
                        currentNode.zero = new Node();
                    }
                    currentNode = currentNode.zero;
                } else {
                    if (currentNode.one == null) {
                        currentNode.one = new Node();
                    }
                    currentNode = currentNode.one;
                }
            }
            currentNode.character = symbol; // Устанавливаем символ в листе
        }

        String encodedString = scanner.nextLine();

        // Декодирование строки с использованием дерева
        Node currentNode = root;
        for (char bit : encodedString.toCharArray()) {
            if (bit == '0') {
                currentNode = currentNode.zero;
            } else {
                currentNode = currentNode.one;
            }

            if (currentNode.character != null) {
                result.append(currentNode.character);
                currentNode = root; // Возврат к корню для следующего символа
            }
        }

        scanner.close();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }


}
