package by.it.group410902.harkavy.lesson03;

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
        Scanner scanner = new Scanner(inputStream);
        // Считываем количество символов и длину закодированной строки
        int k = scanner.nextInt();
        int length = scanner.nextInt();
        scanner.nextLine(); // переход на новую строку после чисел

        // Построим дерево для декодирования на основе заданных кодов
        Node root = new Node();
        for (int i = 0; i < k; i++) {
            String line = scanner.nextLine();
            // Формат строки: "a: 0"
            String[] parts = line.split(": ");
            char letter = parts[0].charAt(0);
            String code = parts[1];

            // Вставляем код для символа в дерево
            Node current = root;
            for (int j = 0; j < code.length(); j++) {
                char bit = code.charAt(j);
                if (bit == '0') {
                    if (current.left == null) {
                        current.left = new Node();
                    }
                    current = current.left;
                } else { // bit == '1'
                    if (current.right == null) {
                        current.right = new Node();
                    }
                    current = current.right;
                }
            }
            // Достигнув листа, записываем символ
            current.letter = letter;
        }

        // Считываем закодированную строку
        String encoded = scanner.next();
        scanner.close();

        // Декодируем строку, проходя по дереву
        StringBuilder result = new StringBuilder();
        Node current = root;
        for (int i = 0; i < encoded.length(); i++) {
            char bit = encoded.charAt(i);
            current = (bit == '0') ? current.left : current.right;
            // Если достигли листа (нет потомков), добавляем символ и возвращаемся к корню
            if (current.left == null && current.right == null) {
                result.append(current.letter);
                current = root;
            }
        }
        return result.toString();
    }

    // Класс узла дерева для декодирования
    class Node {
        char letter;
        Node left;
        Node right;
    }
}
