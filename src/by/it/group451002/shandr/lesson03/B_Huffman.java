package by.it.group451002.shandr.lesson03;

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

//Класс B_Huffman содержит метод main, который является точкой входа в программу.
//Он считывает входные данные из файла dataB.txt и вызывает метод decode.
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
        int count = scanner.nextInt();
        int length = scanner.nextInt();
        innerNode root = new innerNode(null, null);

        while (count > 0) {
            char character = scanner.next().charAt(0);
            String code = scanner.next();

            innerNode tmp = root;
            for (int i = 0; i < code.length(); i++) {
                assert tmp != null : "Tmp is null";
                if (code.charAt(i) == '0') {
                    if (tmp.left == null)
                        tmp.left = (i == code.length() - 1) ? new leafNode(character) : new innerNode(null, null);
                    tmp = (i != code.length() - 1) ? (innerNode) tmp.left : null;
                } else {
                    if (tmp.right == null)
                        tmp.right = (i == code.length() - 1) ? new leafNode(character) : new innerNode(null, null);
                    tmp = (i != code.length() - 1) ? (innerNode) tmp.right : null;
                }
            }

            count--;
        }

        String code = scanner.next();


        Node tmp = root;
        for (int i = 0; i < code.length(); i++) {
            char bit = code.charAt(i);
            if (bit == '0')
                tmp = ((innerNode) tmp).left;
            else if (bit == '1')
                tmp = ((innerNode) tmp).right;

            if (tmp instanceof leafNode leaf) {
                result.append(leaf.symbol);
                tmp = root; // Reset to root
            }
        }

        return result.toString();
    }


    abstract static class Node {
        Node(){}
    }
    private static class innerNode extends Node{
        Node left, right;

        innerNode(Node left, Node right){
            this.left = left;
            this.right = right;
        }
    }
    private static class leafNode extends Node{
        char symbol;

        leafNode(char symbol){
            this.symbol = symbol;
        }
    }
}
