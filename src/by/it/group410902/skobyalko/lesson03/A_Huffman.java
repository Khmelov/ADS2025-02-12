package by.it.group410902.skobyalko.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

// Lesson 3. A_Huffman.
// Разработайте метод encode(File file) для кодирования строки (код Хаффмана)

public class A_Huffman {

    // Индекс данных из листьев
    static private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();
        long startTime = System.currentTimeMillis();
        String result = instance.encode(inputStream);
        long finishTime = System.currentTimeMillis();

        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }
    /// //////////////////////////////////
    String encode(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        if (s.length() == 0) return "";

        // Шаг 1: Подсчет частот символов
        Map<Character, Integer> count = new HashMap<>();
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        // Шаг 2: Построение приоритетной очереди
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // Шаг 3: Построение дерева Хаффмана
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            priorityQueue.add(new InternalNode(left, right));
        }

        // Шаг 4: Генерация кодов
        Node root = priorityQueue.peek();
        if (count.size() == 1) {
            codes.put(s.charAt(0), "0");  // если один символ
        } else {
            root.fillCodes("");
        }

        // Шаг 5: Закодировать строку
        StringBuilder encoded = new StringBuilder();
        for (char c : s.toCharArray()) {
            encoded.append(codes.get(c));
        }

        return encoded.toString();
    }

    // Абстрактный класс элемента дерева
    abstract class Node implements Comparable<Node> {
        final int frequence;

        Node(int frequence) {
            this.frequence = frequence;
        }

        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.frequence, o.frequence);
        }
    }

    // Внутренний узел дерева
    private class InternalNode extends Node {
        Node left;
        Node right;

        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence);
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            left.fillCodes(code + "0");
            right.fillCodes(code + "1");
        }
    }
/// /////////////////////////////.
    // Лист дерева
    private class LeafNode extends Node {
        char symbol;

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            codes.put(this.symbol, code);
        }
    }
}
