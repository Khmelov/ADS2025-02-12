package by.it.group451002.jasko.lesson03;

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

    // Индекс данных из листьев
    static private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) {
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();
        String result = instance.encode(inputStream);
        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    String encode(InputStream inputStream) {
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();
        scanner.close();

        //все комментарии от тестового решения были оставлены т.к. это задание A.
        //если они вам мешают их можно удалить

        Map<Character, Integer> count = new HashMap<>();
        //1. переберем все символы по очереди и рассчитаем их частоту в Map count
        //для каждого символа добавим 1 если его в карте еще нет или инкремент если есть.
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        //2. перенесем все символы в приоритетную очередь в виде листьев
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        //3. вынимая по два узла из очереди (для сборки родителя)
        //и возвращая этого родителя обратно в очередь
        //построим дерево кодирования Хаффмана.
        //У родителя частоты детей складываются.
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            if (right != null) {
                priorityQueue.add(new InternalNode(left, right));
            }
        }

        //4. последний из родителей будет корнем этого дерева
        //это будет последний и единственный элемент оставшийся в очереди priorityQueue.
        Node root = priorityQueue.poll();
        if (root != null) {
            // Генерация кодов
            // Рекурсивно обходим дерево, накапливая коды (0 для левого поддерева, 1 для правого)
            root.fillCodes("");
        }
        //        [8]
        //       /   \
        //    0 /     \1
        //   a(4)     [4]
        //           /   \
        //        0 /     \1
        //       b(2)     [2]
        //               /   \
        //            0 /     \1
        //           c(1)    d(1)

        //5. Кодирование строки
        // Заменяем каждый символ исходной строки соответствующим кодом из карты codes
        StringBuilder encodedString = new StringBuilder();
        for (char c : s.toCharArray()) {
            encodedString.append(codes.get(c));
        }

        return encodedString.toString();
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    //Изучите классы Node InternalNode LeafNode
    static abstract class Node implements Comparable<Node> {
        //абстрактный класс элемент дерева
        //(сделан abstract, чтобы нельзя было использовать его напрямую)
        //а только через его версии InternalNode и LeafNode
        private final int frequency; //частота символов

        //конструктор по умолчанию
        Node(int frequency) {
            this.frequency = frequency;
        }

        //генерация кодов (вызывается на корневом узле
        //один раз в конце, т.е. после построения дерева)
        abstract void fillCodes(String code);

        //метод нужен для корректной работы узла в приоритетной очереди
        //или для сортировок
        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequency, o.frequency);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //расширение базового класса до внутреннего узла дерева
    private static class InternalNode extends Node {
        //внутренный узел дерева
        Node left;  //левый ребенок бинарного дерева
        Node right; //правый ребенок бинарного дерева

        //для этого дерева не существует внутренних узлов без обоих детей
        //поэтому вот такого конструктора будет достаточно
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

    ////////////////////////////////////////////////////////////////////////////////////
    //расширение базового класса до листа дерева
    private static class LeafNode extends Node {
        //лист
        char symbol; //символы хранятся только в листах

        LeafNode(int frequency, char symbol) {
            super(frequency);
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