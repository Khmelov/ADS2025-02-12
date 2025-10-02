package by.it.group451001.khomenkov.lesson03;

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

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        scanner.nextLine(); // Переход на следующую строку

        // Создаем корневой узел дерева
        Node root = new Node();

        // Построение дерева Хаффмана на основе кодов символов

        for (int i = 0; i < count; i++) {
            String[] parts = scanner.nextLine().split(": ");
            char ch = parts[0].charAt(0);
            String code = parts[1];

            Node current = root;
            for (char bit : code.toCharArray()) {
                if (bit == '0') {
                    if (current.left == null) {
                        current.left = new Node();
                    }
                    current = current.left;
                } else {
                    if (current.right == null) {
                        current.right = new Node();
                    }
                    current = current.right;
                }
            }
            current.ch = ch; // Сохраняем символ в листе
        }

        // Чтение закодированной строки
        String encoded = scanner.next();

        // Декодирование строки
        Node current = root;
        for (char bit : encoded.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;

            if (current.ch != null) {
                result.append(current.ch);
                current = root; // Возвращаемся к корню для следующего символа
            }
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }
    // Внутренний класс для представления узла дерева
    private static class Node {
        Node left;  // 0
        Node right; // 1
        Character ch; // Символ (для листьев)
    }

}
