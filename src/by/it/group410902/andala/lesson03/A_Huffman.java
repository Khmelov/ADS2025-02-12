package by.it.group410902.andala.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class A_Huffman {
    // Карта для хранения кодов символов (символ -> код)
    static private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение входных данных из файла
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();

        // Замер времени выполнения
        long startTime = System.currentTimeMillis();
        String result = instance.encode(inputStream);
        long finishTime = System.currentTimeMillis();

        // Вывод результатов
        System.out.printf("%d %d\n", codes.size(), result.length()); // Количество символов и длина кода
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue()); // Коды символов
        }
        System.out.println(result); // Закодированная строка
    }

    /**
     * Метод для кодирования строки алгоритмом Хаффмана
     * @param inputStream поток ввода с данными
     * @return закодированная строка
     */
    String encode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();
        scanner.close();

        // 1. ПОДСЧЕТ ЧАСТОТ СИМВОЛОВ
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : s.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        // 2. СОЗДАНИЕ ЛИСТЬЕВ ДЕРЕВА (ПРИОРИТЕТНАЯ ОЧЕРЕДЬ)
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // 3. ПОСТРОЕНИЕ ДЕРЕВА ХАФФМАНА
        while (priorityQueue.size() > 1) {
            // Берем два узла с наименьшей частотой
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            // Создаем новый внутренний узел
            priorityQueue.add(new InternalNode(left, right));
        }

        // 4. ГЕНЕРАЦИЯ КОДОВ
        Node root = priorityQueue.poll();
        if (root != null) {
            if (freqMap.size() == 1) {
                // Особый случай: если символ всего один
                root.fillCodes("0");
            } else {
                root.fillCodes("");
            }
        }

        // 5. КОДИРОВАНИЕ СТРОКИ
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(codes.get(c));
        }

        return sb.toString();
    }

    /**
     * Абстрактный класс узла дерева Хаффмана
     */
    abstract class Node implements Comparable<Node> {
        private final int frequence; // Частота символа/узла

        private Node(int frequence) {
            this.frequence = frequence;
        }

        // Абстрактный метод для генерации кодов
        abstract void fillCodes(String code);

        // Сравнение узлов по частоте
        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence);
        }
    }

    /**
     * Внутренний узел дерева (не лист)
     */
    private class InternalNode extends Node {
        Node left;  // Левый потомок
        Node right; // Правый потомок

        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence); // Частота = сумма частот потомков
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            // Рекурсивно генерируем коды для потомков
            left.fillCodes(code + "0"); // Левый - 0
            right.fillCodes(code + "1"); // Правый - 1
        }
    }

    /**
     * Лист дерева (содержит символ)
     */
    private class LeafNode extends Node {
        char symbol; // Символ

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            // Сохраняем код для символа
            codes.put(this.symbol, code);
        }
    }
}