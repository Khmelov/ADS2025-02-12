package by.it.group410902.kovalchuck.lesson01.lesson03;

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
        //тут запишите ваше решение

        Node root = new Node(' ');
// Создаётся корневой узел дерева. Он не содержит символа (пустой), а служит начальной точкой дерева Хаффмана.

        for (int i = 0; i < count; i++) {
            String temp_sym = scanner.next();
            // Считываем символ из входных данных.

            Node temp_no = root;
            // Устанавливаем временный узел на корень дерева. Используется для построения дерева.

            String code = Integer.toString(scanner.nextInt());
            // Считываем код символа (последовательность 0 и 1).

            for (int j = 0; j < code.length(); j++) {
                // Проходим по каждому символу кода (0 или 1).
                if (code.charAt(j) == '0') {
                    // Если текущий символ кода — '0', идём в левое поддерево.
                    if (temp_no.left == null) {
                        temp_no.left = new Node(' ');
                        // Если левого узла не существует, создаём новый пустой узел.
                    }
                    temp_no = temp_no.left;
                    // Перемещаемся в левый узел.
                } else {
                    // Если текущий символ кода — '1', идём в правое поддерево.
                    if (temp_no.right == null) {
                        temp_no.right = new Node(' ');
                        // Если правого узла не существует, создаём новый пустой узел.
                    }
                    temp_no = temp_no.right;
                    // Перемещаемся в правый узел.
                }
            }
            temp_no.symbol = temp_sym.charAt(0);
            // Когда мы достигли конца кода, сохраняем символ в узле дерева.
        }

// Декодирование закодированной строки
        String code = scanner.next();
// Считываем закодированную строку для декодирования.

        Node temp_node = root;
// Устанавливаем временный узел на корень дерева, чтобы начать декодирование.

        for (int i = 0; i < code.length(); i++) {
            // Проходим по каждому символу закодированной строки.
            if (temp_node.left == null && temp_node.right == null) {
                // Если узел является листом (у него нет детей), значит, мы нашли символ.
                result.append(temp_node.symbol);
                // Добавляем символ в результат.

                temp_node = root;
                // Возвращаемся в корень дерева, чтобы начать декодирование следующего символа.

                i--;
                // Уменьшаем индекс, чтобы текущий символ строки также обрабатывался в следующем шаге.
            } else {
                // Если узел не является листом, продолжаем движение по дереву.
                if (code.charAt(i) == '0') {
                    temp_node = temp_node.left;
                    // Если текущий символ — '0', идём в левое поддерево.
                } else {
                    temp_node = temp_node.right;
                    // Если текущий символ — '1', идём в правое поддерево.
                }
            }
        }
        result.append(temp_node.symbol);
// Добавляем последний символ в результат, когда мы закончили обработку строки.


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString(); //01001100100111
    }
    class Node {
        char symbol;
        Node left;
        Node right;

        Node(char symbol_in){
            this.symbol  = symbol_in;
        }

    }

}
