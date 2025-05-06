package by.it.group451002.shandr.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;


//Алгоритм:
//
//Считывает строку.
//Подсчитывает частоты символов.
//Строит дерево Хаффмана.
//Генерирует коды символов.
//Кодирует входную строку с помощью этих кодов.
//Результат — строка, закодированная по алгоритму Хаффмана, и карта кодов символов.

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

    //Открывается файл dataA.txt с текстом для кодирования.
    //Создается объект A_Huffman, который запускает метод encode для кодирования текста.
    //Вычисляется время выполнения алгоритма.
    //Выводится количество уникальных символов (размер словаря) и длина закодированной строки.
    //Печатаются коды каждого символа и закодированная строка.
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

    //Читает входной текст из файла в строку s.
    //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    String encode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        //Создается count, где каждому символу строки сопоставляется его частота.
        //Метод compute обновляет значение для каждого символа: если символ встречается впервые, его
        // частота устанавливается в 1; иначе — увеличивается на 1.
        Map<Character, Integer> count = new HashMap<>();
        for ( int i = 0; i < s.length(); i++ )
            count.compute(s.charAt(i), (key, value) -> value == null ? 1 : value + 1);

        //Создается приоритетная очередь priorityQueue, которая упорядочивает узлы на основе частоты символов.
        //Для каждого символа из карты count создается лист дерева (LeafNode) и добавляется в очередь.
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        count.forEach((key, value) -> priorityQueue.add(new LeafNode(value, key)));

        //Пока в очереди больше одного узла:
        //Извлекаются два узла с наименьшей частотой (left и right).
        //Создается внутренний узел (InternalNode), суммирующий частоты этих узлов.
        //Новый узел добавляется обратно в очередь.
        //Итогом работы цикла становится корневой узел дерева Хаффмана.
        while ( priorityQueue.size() > 1){
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            assert right != null;
            InternalNode parent = new InternalNode(left, right);
            priorityQueue.add(parent);
        }

        //Извлекается корневой узел дерева.
        //Метод fillCodes рекурсивно обходит дерево, генерируя коды для каждого символа (листьев).
        if ( !priorityQueue.isEmpty() ){
            Node root = priorityQueue.poll();
            root.fillCodes("");
        }
        //Для каждого символа строки извлекается соответствующий код из карты codes.
        //Коды объединяются в результирующую строку encodedStr.
        StringBuilder encodedStr = new StringBuilder();
        for ( int i = 0; i < s.length(); i++)
            encodedStr.append(codes.get(s.charAt(i)));


        return encodedStr.toString();
    }
    //Node хранит частоту символов и служит базовым классом для узлов дерева.
    //Реализует интерфейс Comparable<Node> для упорядочивания узлов по частоте.
    //Изучите классы Node InternalNode LeafNode
    abstract static class Node implements Comparable<Node> {
        //абстрактный класс элемент дерева
        //(сделан abstract, чтобы нельзя было использовать его напрямую)
        //а только через его версии InternalNode и LeafNode
        private final int frequence; //частота символов

        //конструктор по умолчанию
        private Node(int frequence) {
            this.frequence = frequence;
        }

        //генерация кодов (вызывается на корневом узле
        //один раз в конце, т.е. после построения дерева)
        abstract void fillCodes(String code);

        //метод нужен для корректной работы узла в приоритетной очереди
        //или для сортировок
        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////
    //расширение базового класса до внутреннего узла дерева
    private class InternalNode extends Node {
        //внутренный узел дерева
        Node left;  //левый ребенок бинарного дерева
        Node right; //правый ребенок бинарного дерева

        //для этого дерева не существует внутренних узлов без обоих детей
        //поэтому вот такого конструктора будет достаточно
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
    //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

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
