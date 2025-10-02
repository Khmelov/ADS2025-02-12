package by.it.group451001.bolshakova.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

// Lesson 3. A_Huffman.
// Задача: По данным файла (непустой строке s длины не более 10^4), состоящей из строчных букв латинского алфавита,
//        постройте оптимальный по суммарной длине беспрефиксный код.
// Используйте алгоритм Хаффмана — жадный алгоритм оптимального безпрефиксного кодирования алфавита с минимальной избыточностью.

public class A_Huffman {

    // Карта для хранения сгенерированных кодов для каждого символа (используем TreeMap для упорядоченности по символу)
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

    //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    String encode(InputStream inputStream) throws FileNotFoundException {
        // Читаем строку для кодирования из файла
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        // 1. Подсчитаем частоту появления каждого символа в строке.
        // Пробегаем по каждому символу и записываем его частоту в Map.
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            freq.put(ch, freq.getOrDefault(ch, 0) + 1);
        }

        // 2. Создадим приоритетную очередь (min-heap) для построения дерева Хаффмана.
        // Каждый элемент очереди — это узел дерева (листовой или внутренний).
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            pq.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        // Особый случай: если строка содержит только один уникальный символ.
        if (pq.size() == 1) {
            // Если только один символ, то добавляем фиктивный узел (например, с кодом для символа 'null')
            pq.add(new LeafNode(0, '\0'));
        }

        // 3. Построим дерево Хаффмана:
        // Извлекаем два узла с наименьшей частотой, создаем для них родительский узел, добавляем его обратно.
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new InternalNode(left, right);
            pq.add(parent);
        }
        // Последний оставшийся узел — корень дерева.
        Node root = pq.poll();

        // 4. Запустим генерацию кодов. Рекурсивный обход дерева.
        // Если узел-лист и переданный сгенерированный код пустой, задаём в качестве кода "0".
        root.fillCodes("");

        // 5. Кодируем исходную строку, заменяя каждый символ на его Хаффман-код.
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            encoded.append(codes.get(s.charAt(i)));
        }
        return encoded.toString();
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    // Абстрактный класс Node представляет элемент дерева.
    abstract class Node implements Comparable<Node> {
        protected final int frequence; // Частота появления символа (или сумма частот для внутреннего узла)

        private Node(int frequence) {
            this.frequence = frequence;
        }

        // Рекурсивный метод для генерации кодов.
        abstract void fillCodes(String code);

        // Определение порядка узлов по частоте (необходимо для корректной работы приоритетной очереди)
        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.frequence, o.frequence);
        }
    }

    // Внутренний узел дерева Хаффмана (имеет двух детей)
    private class InternalNode extends Node {
        Node left;  // Левый ребёнок
        Node right; // Правый ребёнок

        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence);
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            // При обходе дерева добавляем "0" для левого ребёна и "1" для правого.
            left.fillCodes(code + "0");
            right.fillCodes(code + "1");
        }
    }

    // Листовой узел дерева, содержащий символ.
    private class LeafNode extends Node {
        char symbol;

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            // Если полученный код пустой (случай, когда буква одна в строке),
            // то задаем код как "0". Это специальное правило для обработки случая с одним символом.
            if (code.isEmpty()) {
                code = "0";
            }
            codes.put(this.symbol, code);
        }
    }
}
