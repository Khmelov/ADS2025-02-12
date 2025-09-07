package by.it.group451004.akbulatov.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;

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

    String encode(InputStream inputStream) throws FileNotFoundException {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        Map<Character, Integer> count = new HashMap<>();
        for (char c : s.toCharArray()) {
            if (count.containsKey(c))
                count.put(c, count.get(c) + 1);
            else
                count.put(c, 1);
        }

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            LeafNode tmp = new LeafNode(entry.getValue(), entry.getKey());
            priorityQueue.add(tmp);
        }

        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.remove();
            Node right = priorityQueue.remove();
            InternalNode tmp = new InternalNode(left, right);
            priorityQueue.add(tmp);
        }

        Node tmp = priorityQueue.remove();
        tmp.fillCodes("");

        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(codes.get(c));
        }

        return sb.toString();
    }

    abstract static class Node implements Comparable<Node> {
        private final int frequency;

        private Node(int frequence) {
            this.frequency = frequence;
        }

        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node other) {
            return Integer.compare(frequency, other.frequency);
        }
    }

    private class InternalNode extends Node {
        Node right;
        Node left;

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

    private class LeafNode extends Node {
        char symbol;

        LeafNode(int frequency, char symbol) {
            super(frequency);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            codes.put(this.symbol, code);
        }
    }

}
