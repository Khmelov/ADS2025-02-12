package by.it.group451002.kravtsov.lesson03;

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

    //индекс данных из листьев
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

    //!!!!!!!!!!!!!!!!!!!!!!!!!  НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
    String encode(InputStream inputStream) throws FileNotFoundException {
        // Создаём Scanner для считывания данных из входного потока
        Scanner scanner = new Scanner(inputStream);

        // Считываем строку для кодирования
        String s = scanner.next();

        // Считаем частоту появления каждого символа
        Map<Character, Integer> count = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            // Если символ новый, добавляем его с частотой 1, иначе увеличиваем частоту
            count.compute(s.charAt(i), (key, value) -> value == null ? 1 : value + 1);
        }

        // Создаём приоритетную очередь для узлов дерева Хаффмана
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

        // Заполняем очередь листовыми узлами, где содержатся символы и их частоты
        count.forEach((key, value) -> priorityQueue.add(new LeafNode(value, key)));

        // Строим дерево Хаффмана
        while (priorityQueue.size() > 1) {
            // Извлекаем два узла с минимальной частотой
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            assert right != null; // Убедимся, что второй узел не null

            // Создаём родительский узел и добавляем его в очередь
            InternalNode parent = new InternalNode(left, right);
            priorityQueue.add(parent);
        }

        // Если очередь не пуста, создаём корневой узел и заполняем коды символов
        if (!priorityQueue.isEmpty()) {
            Node root = priorityQueue.poll();
            root.fillCodes(""); // Генерация кодов для символов
        }

        // Кодируем строку, используя сгенерированные коды символов
        StringBuilder encodedStr = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            encodedStr.append(codes.get(s.charAt(i)));
        }

        // Возвращаем закодированную строку
        return encodedStr.toString();
    }

    // Абстрактный класс для представления узла дерева Хаффмана
    abstract static class Node implements Comparable<Node> {
        private final int frequence; // Частота символа или суммы частот

        // Конструктор узла
        private Node(int frequence) {
            this.frequence = frequence;
        }

        // Метод для генерации кодов символов
        abstract void fillCodes(String code);

        // Метод для корректного сравнения узлов по их частоте
        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence);
        }
    }

    // Внутренний узел дерева Хаффмана
    private class InternalNode extends Node {
        Node left;  // Левый ребёнок узла
        Node right; // Правый ребёнок узла

        // Конструктор, принимает два дочерних узла и суммирует их частоты
        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence);
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            // Рекурсивно вызываем для левого ребёнка с добавлением "0"
            left.fillCodes(code + "0");
            // Рекурсивно вызываем для правого ребёнка с добавлением "1"
            right.fillCodes(code + "1");
        }
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!  КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

    ////////////////////////////////////////////////////////////////////////////////////
    //расширение базового класса до листа дерева
    private class LeafNode extends Node {
        //лист
        char symbol; //символы хранятся только в листах

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            //добрались до листа, значит рекурсия закончена, код уже готов
            //и можно запомнить его в индексе для поиска кода по символу.
            codes.put(this.symbol, code);
        }
    }

}
