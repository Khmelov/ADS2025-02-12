package by.it.group410901.kliaus.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

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
        Integer count = scanner.nextInt(); // Количество пар символ-код
        Integer length = scanner.nextInt(); // Длина закодированной строки

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        // Создаём корень дерева
        InnerNode root = new InnerNode(null, null);

        // Построение дерева Хаффмана на основе символов и их кодов
        while (count > 0) {
            char symbol = scanner.next().charAt(0); // Читаем символ
            String code = scanner.next();           // Читаем его код

            InnerNode current = root;
            for (int i = 0; i < code.length(); i++) {
                char bit = code.charAt(i);
                if (bit == '0') {
                    if (current.left == null) {
                        // Если это последний бит, создаём лист, иначе внутренний узел
                        current.left = (i == code.length() - 1) ? new LeafNode(symbol) : new InnerNode(null, null);
                    }
                    // Переходим к следующему узлу, если не последний бит
                    if (i < code.length() - 1) {
                        current = (InnerNode) current.left;
                    }
                } else if (bit == '1') {
                    if (current.right == null) {
                        current.right = (i == code.length() - 1) ? new LeafNode(symbol) : new InnerNode(null, null);
                    }
                    if (i < code.length() - 1) {
                        current = (InnerNode) current.right;
                    }
                }
            }
            count--;
        }

        // Читаем закодированную строку и декодируем её
        String encodedString = scanner.next();
        Node current = root;
        for (int i = 0; i < encodedString.length(); i++) {
            char bit = encodedString.charAt(i);
            if (bit == '0') {
                current = ((InnerNode) current).left;
            } else if (bit == '1') {
                current = ((InnerNode) current).right;
            }

            // Если достигли листа, добавляем символ и возвращаемся к корню
            if (current instanceof LeafNode leaf) {
                result.append(leaf.symbol);
                current = root;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); // Возвращаем декодированную строку
    }
    abstract class Node {
        // Базовый класс для узлов дерева
    }

    class InnerNode extends Node {
        Node left;
        Node right;

        InnerNode(Node left, Node right) {
            this.left = left;
            this.right = right;
        }
    }

    class LeafNode extends Node {
        char symbol;

        LeafNode(char symbol) {
            this.symbol = symbol;
        }
    }

}
