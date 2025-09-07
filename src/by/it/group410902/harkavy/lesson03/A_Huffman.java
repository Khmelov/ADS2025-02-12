package by.it.group410902.harkavy.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

public class A_Huffman {

    // Индекс для хранения кодов символов (отсортированный по символу)
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

    // Реализация метода кодирования строки с помощью алгоритма Хаффмана
    String encode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();
        scanner.close();

        // 1. Подсчитаем частоты встречаемости каждого символа
        Map<Character, Integer> count = new HashMap<>();
        for (char ch : s.toCharArray()) {
            count.put(ch, count.getOrDefault(ch, 0) + 1);
        }

        // 2. Поместим все символы в приоритетную очередь в виде листьев
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // 3. Особый случай: если строка состоит из одного уникального символа,
        // назначим ему код "0"
        if (priorityQueue.size() == 1) {
            Node only = priorityQueue.poll();
            only.fillCodes("");
        } else {
            // 4. Построим дерево Хаффмана, объединяя по два узла с минимальной частотой
            while (priorityQueue.size() > 1) {
                Node left = priorityQueue.poll();
                Node right = priorityQueue.poll();
                Node parent = new InternalNode(left, right);
                priorityQueue.add(parent);
            }
            // Последний оставшийся узел – корень дерева
            Node root = priorityQueue.poll();
            root.fillCodes("");
        }

        // 5. Получим закодированную строку, заменяя каждый символ его кодом
        StringBuilder sb = new StringBuilder();
        for (char ch : s.toCharArray()) {
            sb.append(codes.get(ch));
        }
        return sb.toString();
    }

    // Абстрактный класс для узлов дерева Хаффмана
    abstract class Node implements Comparable<Node> {
        private final int frequence; // частота символа(ов)

        private Node(int frequence) {
            this.frequence = frequence;
        }

        // Рекурсивное заполнение кодов для каждого символа
        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.frequence, o.frequence);
        }
    }

    // Класс для внутренних узлов дерева
    private class InternalNode extends Node {
        Node left;
        Node right;

        InternalNode(Node left, Node right) {
            // Суммируем частоты левого и правого ребёнка
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

    // Класс для листьев дерева (содержащих сами символы)
    private class LeafNode extends Node {
        char symbol;

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            // Если код пустой (случай единственного символа), задаём "0"
            if (code.equals("")) {
                code = "0";
            }
            codes.put(this.symbol, code);
        }
    }
}