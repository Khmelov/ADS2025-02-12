package by.it.group410902.latipov.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class A_Huffman {

    // Статическая карта для хранения кодов символов (из листьев дерева)
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

    String encode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        // Шаг 1: Подсчет частоты каждого символа
        Map<Character, Integer> count = new HashMap<>();
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        // Шаг 2: Создание очереди с приоритетом из листовых узлов
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // Шаг 3: Построение дерева Хаффмана путем объединения узлов
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            priorityQueue.add(new InternalNode(left, right));
        }

        // Шаг 4: Генерация кодов из корневого узла
        Node root = priorityQueue.poll();
        root.fillCodes("");

        // Шаг 5: Кодирование исходной строки с использованием созданных кодов
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(codes.get(c));
        }

        return sb.toString();
    }

    // Абстрактный класс узел дерева
    abstract class Node implements Comparable<Node> {
        private final int frequence; // частота символов

        private Node(int frequence) {
            this.frequence = frequence;
        }

        // Генерация кодов (вызывается на корневом узле)
        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence);
        }
    }

    // Внутренний узел дерева
    private class InternalNode extends Node {
        Node left;  // левый потомок
        Node right; // правый потомок

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

    // Листовой узел дерева
    private class LeafNode extends Node {
        char symbol; // символы хранятся только в листьях

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            // Дошли до листа, рекурсия завершена, код готов
            codes.put(this.symbol, code);
        }
    }
}