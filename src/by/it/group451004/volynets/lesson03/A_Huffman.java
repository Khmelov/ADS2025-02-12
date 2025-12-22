package by.it.group451004.volynets.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class A_Huffman {

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
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        Map<Character, Integer> count = new HashMap<>();
        //1. переберем все символы по очереди и рассчитаем их частоту в Map count
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        //2. перенесем все символы в приоритетную очередь в виде листьев
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        //3. вынимая по два узла из очереди (для сборки родителя)
        //и возвращая этого родителя обратно в очередь
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            Node parent = new InternalNode(left, right);
            priorityQueue.add(parent);
        }

        //4. последний из родителей будет корнем этого дерева
        Node root = priorityQueue.poll();
        codes.clear();
        if (root != null) {
            if (count.size() == 1) {
                root.fillCodes("0");
            } else {
                root.fillCodes("");
            }
        }

        StringBuilder sb = new StringBuilder();
        //кодируем строку используя построенные коды
        for (int i = 0; i < s.length(); i++) {
            sb.append(codes.get(s.charAt(i)));
        }

        return sb.toString();
        //01001100100111
        //01001100100111
    }

    abstract class Node implements Comparable<Node> {
        private final int frequence;

        private Node(int frequence) {
            this.frequence = frequence;
        }

        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node o) {
            return Integer.compare(frequence, o.frequence);
        }
    }

    private class InternalNode extends Node {
        Node left;
        Node right;

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

    private class LeafNode extends Node {
        char symbol;

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            codes.put(this.symbol, code);
        }
    }

}