package by.it.group451002.mitskevich.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class A_Huffman {

    // индекс данных из листьев
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

    // Метод для кодирования строки с использованием алгоритма Хаффмана
    String encode(InputStream inputStream) throws FileNotFoundException {
        // Прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        // Подсчитываем частоты каждого символа
        Map<Character, Integer> count = new HashMap<>();
        for (char ch : s.toCharArray()) {
            count.put(ch, count.getOrDefault(ch, 0) + 1);
        }

        // Переносим символы в приоритетную очередь в виде листьев
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // Строим дерево Хаффмана
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            priorityQueue.add(new InternalNode(left, right));
        }

        // Заполняем коды символов
        Node root = priorityQueue.poll();
        root.fillCodes("");

        // Генерируем закодированную строку
        StringBuilder encodedString = new StringBuilder();
        for (char ch : s.toCharArray()) {
            encodedString.append(codes.get(ch));
        }

        return encodedString.toString();
    }

    // Абстрактный класс для узлов дерева
    abstract class Node implements Comparable<Node> {
        private final int frequence; // частота символов

        // Конструктор по умолчанию
        private Node(int frequence) {
            this.frequence = frequence;
        }

        // Генерация кодов
        abstract void fillCodes(String code);

        // Метод для корректной работы узла в приоритетной очереди
        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence);
        }
    }

    // Внутренний узел дерева
    private class InternalNode extends Node {
        Node left;  // левый ребенок бинарного дерева
        Node right; // правый ребенок бинарного дерева

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

    // Лист дерева (символы)
    private class LeafNode extends Node {
        char symbol; // символы хранятся только в листах

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            // Добрались до листа, код уже готов
            codes.put(this.symbol, code);
        }
    }
}


