package by.it.group451004.ivanov.lesson03;

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
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        //1. Частотность символов
        Map<Character, Integer> count = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (count.containsKey(c)) {
                count.put(c, count.get(c) + 1);
            } else {
                count.put(c, 1);
            }
        }

        //2. перенесем все символы в приоритетную очередь в виде листьев
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        //3. построение дерева кодирования Хаффмана
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            InternalNode NewNode = new InternalNode(left, right);
            priorityQueue.add(NewNode);
        }

        //4. последний из родителей будет корнем этого дерева, от него запускаем fillCodes
        priorityQueue.poll().fillCodes("");

        //5. обход дерева+добавление символа в карту, если лист (моё) - уже есть...

        //6. кодирование
        StringBuilder sb = new StringBuilder();
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