package by.it.group451003.plyushchevich.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

//Lesson 3. A_Huffman.
//Разработайте метод encode(File file) для кодирования строки (код Хаффмана)

// По данным файла (непустой строке ss длины не более 104104),
// состоящей из строчных букв латинского алфавита,
// постройте оптимальный по суммарной длине беспрефиксный код.

// Используйте Алгоритм Хаффмана — жадный алгоритм оптимального
// безпрефиксного кодирования алфавита с минимальной избыточностью.

// В первой строке выведите количество различных букв kk,
// встречающихся в строке, и размер получившейся закодированной строки.
// В следующих kk строках запишите коды букв в формате "letter: code".
// В последней строке выведите закодированную строку. Примеры ниже

//        Sample Input 1:
//        a
//
//        Sample Output 1:
//        1 1
//        a: 0
//        0

//        Sample Input 2:
//        abacabad
//
//        Sample Output 2:
//        4 14
//        a: 0
//        b: 10
//        c: 110
//        d: 111
//        01001100100111

public class A_Huffman {

    // сюда будут складываться коды символов
    static private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();
        String result = instance.encode(inputStream);
        // выводим количество символов и длину закодированной строки
        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

    String encode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        // 1. Считаем частоты символов
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // 2. Создаём приоритетную очередь листьев
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> e : freq.entrySet()) {
            pq.add(new LeafNode(e.getValue(), e.getKey()));
        }

        // Особый случай: если в строке только один уникальный символ,
        // ему по условию присваиваем код "0"
        if (pq.size() == 1) {
            LeafNode lone = (LeafNode) pq.poll();
            codes.put(lone.symbol, "0");
            // и результат — просто повторение "0" столько раз, сколько длина s
            return "0".repeat(s.length());
        }

        // 3. Строим дерево Хаффмана
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            pq.add(new InternalNode(left, right));
        }

        // Корень дерева — последний узел в очереди
        Node root = pq.poll();

        // 4. Обходим дерево, заполняем коды
        root.fillCodes("");

        // 5. Строим результирующую закодированную строку
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(codes.get(c));
        }
        return sb.toString();
    }

    // --- Классы узлов дерева ---

    abstract class Node implements Comparable<Node> {
        private final int frequence;
        private Node(int frequence) { this.frequence = frequence; }
        abstract void fillCodes(String code);
        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.frequence, o.frequence);
        }
    }

    private class InternalNode extends Node {
        Node left, right;
        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence);
            this.left = left;
            this.right = right;
        }
        @Override
        void fillCodes(String code) {
            left.fillCodes(code + '0');
            right.fillCodes(code + '1');
        }
    }

    private class LeafNode extends Node {
        char symbol;
        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }
        @Override
        void fillCodes(String code) {
            codes.put(symbol, code);
        }
    }
}
