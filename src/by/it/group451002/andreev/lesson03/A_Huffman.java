package by.it.group451002.andreev.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * Класс для кодирования строки по алгоритму Хаффмана.
 * Кодирование основано на частотном анализе символов и построении дерева Хаффмана.
 */
public class A_Huffman {

    // Хранение кодов Хаффмана для символов
    static private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();
        long startTime = System.currentTimeMillis(); // Засекаем время выполнения
        String result = instance.encode(inputStream);
        long finishTime = System.currentTimeMillis();

        // Вывод информации о кодировке
        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

    /**
     * Метод кодирования строки по алгоритму Хаффмана.
     * @param inputStream входной поток данных
     * @return закодированная строка
     * @throws FileNotFoundException если файл не найден
     */
    String encode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        // Шаг 1: Подсчет частоты символов
        Map<Character, Integer> count = new HashMap<>();
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        // Шаг 2: Создание приоритетной очереди с листовыми узлами
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

        // Шаг 4: Генерация кодов Хаффмана
        Node root = priorityQueue.poll();
        if (count.size() == 1) {
            // Специальный случай, если в строке один уникальный символ
            root.fillCodes("0");
        } else {
            root.fillCodes("");
        }

        // Шаг 5: Кодирование входной строки
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(codes.get(c)); // Используем сохраненные коды
        }

        return sb.toString();
    }

    /**
     * Абстрактный класс узла дерева Хаффмана.
     */
    abstract class Node implements Comparable<Node> {
        private final int frequence; // Частота символа или суммы частот дочерних узлов

        private Node(int frequence) {
            this.frequence = frequence;
        }

        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence); // Сортировка узлов по частоте
        }
    }

    /**
     * Класс внутреннего узла (объединяет два узла).
     */
    private class InternalNode extends Node {
        Node left;
        Node right;

        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence); // Суммируем частоты
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            left.fillCodes(code + "0"); // Левый узел получает "0"
            right.fillCodes(code + "1"); // Правый узел получает "1"
        }
    }

    /**
     * Класс листового узла (хранит символ).
     */
    private class LeafNode extends Node {
        char symbol;

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            codes.put(this.symbol, code); // Сохраняем код для символа
        }
    }
}
