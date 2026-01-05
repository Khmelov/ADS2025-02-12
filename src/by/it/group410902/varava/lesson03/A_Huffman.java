package by.it.group410902.varava.lesson03;

import java.io.InputStream;
import java.util.*;

/**
 * Lesson 3. A_Huffman.
 * Реализация кодирования строки алгоритмом Хаффмана.
 */
public class A_Huffman {

    // Словарь для хранения кодов символов
    private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) {
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();
        String result = instance.encode(inputStream);

        // Вывод результатов
        System.out.printf("%d %d\n", instance.codes.size(), result.length());
        for (Map.Entry<Character, String> entry : instance.codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

    /**
     * Кодирует строку из входного потока.
     */
    public String encode(InputStream inputStream) {
        // Читаем строку для кодирования
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();
        scanner.close();

        // Подсчитываем частоту символов
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : s.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Создаём приоритетную очередь для построения дерева Хаффмана
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // Строим дерево Хаффмана
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            priorityQueue.add(new InternalNode(left, right));
        }

        // Корневой узел дерева
        Node root = priorityQueue.poll();
        if (root != null) {
            root.fillCodes("");
        }

        // Кодируем строку
        StringBuilder encodedString = new StringBuilder();
        for (char c : s.toCharArray()) {
            encodedString.append(codes.get(c));
        }

        return encodedString.toString();
    }

    // Абстрактный класс узла дерева Хаффмана
    abstract class Node implements Comparable<Node> {
        private final int frequency;

        protected Node(int frequency) {
            this.frequency = frequency;
        }

        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.frequency, o.frequency);
        }
    }

    // Внутренний узел дерева Хаффмана
    private class InternalNode extends Node {
        private final Node left;
        private final Node right;

        InternalNode(Node left, Node right) {
            super(left.frequency + right.frequency);
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            left.fillCodes(code + "0");
            right.fillCodes(code + "1");
        }
    }

    // Листовой узел дерева Хаффмана
    private class LeafNode extends Node {
        private final char symbol;

        LeafNode(int frequency, char symbol) {
            super(frequency);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            codes.put(symbol, code);
        }
    }
}
