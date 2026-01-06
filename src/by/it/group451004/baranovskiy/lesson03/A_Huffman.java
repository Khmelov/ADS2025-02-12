package by.it.group451004.baranovskiy.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

// Урок 3. Алгоритм Хаффмана.
// Разработан метод encode(File file) для кодирования строки с использованием кода Хаффмана

// Для входного файла (непустой строки ss длиной не более 10^4 символов),
// состоящей из строчных букв латинского алфавита,
// строится оптимальный беспрефиксный код с минимальной избыточностью.

// Используется алгоритм Хаффмана — жадный алгоритм оптимального
// беспрефиксного кодирования алфавита.

// В первой строке вывода — количество различных букв kk,
// встречающихся в строке, и размер закодированной строки.
// В следующих kk строках — коды букв в формате "letter: code".
// В последней строке — закодированная строка.

public class A_Huffman {

    // Карта для хранения кодов символов (отсортированная по ключу)
    static private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        // Чтение входного файла
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();
        long startTime = System.currentTimeMillis();
        // Кодирование строки
        String result = instance.encode(inputStream);
        long finishTime = System.currentTimeMillis();
        // Вывод результатов
        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

    // Основной метод кодирования
    String encode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        // 1. Подсчет частоты встречаемости каждого символа
        Map<Character, Integer> count = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (count.containsKey(c)) {
                count.put(c, count.get(c) + 1); // Увеличиваем счетчик, если символ уже встречался
            } else {
                count.put(c, 1); // Иначе добавляем новый символ
            }
        }

        // 2. Создание приоритетной очереди из узлов (листьев) для каждого символа
        // Очередь сортируется по частоте символов (наименьшая частота — первая)
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // 3. Построение дерева Хаффмана:
        // Берем два узла с наименьшей частотой и объединяем их в новый узел
        // Повторяем, пока не останется один корневой узел
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();  // Узел с меньшей частотой
            Node right = priorityQueue.poll(); // Следующий узел с меньшей частотой
            InternalNode parentNode = new InternalNode(left, right); // Создаем родительский узел
            priorityQueue.add(parentNode);     // Добавляем новый узел в очередь
        }

        // 4. Генерация кодов для каждого символа:
        // Начинаем с корня дерева (последний оставшийся узел в очереди)
        // Рекурсивно обходим дерево, строя коды (0 — левая ветвь, 1 — правая)
        priorityQueue.poll().fillCodes("");

        // 5. Кодирование исходной строки:
        // Для каждого символа строки добавляем его код в результирующую строку
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(codes.get(s.charAt(i)));
        }

        return sb.toString();
    }

    // Абстрактный класс узла дерева Хаффмана
    abstract class Node implements Comparable<Node> {
        private final int frequence; // Частота встречаемости символа/суммарная частота поддерева

        private Node(int frequence) {
            this.frequence = frequence;
        }

        // Абстрактный метод для построения кодов символов
        abstract void fillCodes(String code);

        // Сравнение узлов по частоте (для приоритетной очереди)
        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence);
        }
    }

    // Класс внутреннего узла дерева (не лист)
    private class InternalNode extends Node {
        Node left;  // Левое поддерево
        Node right; // Правое поддерево

        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence); // Частота — сумма частот поддеревьев
            this.left = left;
            this.right = right;
        }

        // Рекурсивное построение кодов:
        // При движении влево добавляем "0", вправо — "1"
        @Override
        void fillCodes(String code) {
            left.fillCodes(code + "0");
            right.fillCodes(code + "1");
        }
    }

    // Класс листового узла (содержит символ)
    private class LeafNode extends Node {
        char symbol; // Символ

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        // Добавление кода символа в карту codes
        @Override
        void fillCodes(String code) {
            codes.put(this.symbol, code);
        }
    }
}