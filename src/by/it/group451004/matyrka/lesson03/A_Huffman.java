package by.it.group451004.matyrka.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class A_Huffman {

    static private final Map<Character, String> codes = new TreeMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();
        String result = instance.encode(inputStream);

        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> e : codes.entrySet()) {
            System.out.printf("%s: %s\n", e.getKey(), e.getValue());
        }
        System.out.println(result);
    }

    String encode(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray())
            freq.put(c, freq.getOrDefault(c, 0) + 1);

        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> e : freq.entrySet())
            pq.add(new LeafNode(e.getValue(), e.getKey()));

        if (pq.size() == 1) {
            Node single = pq.poll();
            single.fillCodes("0");
        } else {
            while (pq.size() > 1) {
                Node a = pq.poll();
                Node b = pq.poll();
                pq.add(new InternalNode(a, b));
            }
            pq.poll().fillCodes("");
        }

        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray())
            sb.append(codes.get(c));

        return sb.toString();
    }

    abstract class Node implements Comparable<Node> {
        int frequence;

        Node(int frequence) {
            this.frequence = frequence;
        }

        abstract void fillCodes(String code);

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.frequence, o.frequence);
        }
    }

    class InternalNode extends Node {
        Node left, right;

        InternalNode(Node l, Node r) {
            super(l.frequence + r.frequence);
            left = l;
            right = r;
        }

        @Override
        void fillCodes(String code) {
            left.fillCodes(code + "0");
            right.fillCodes(code + "1");
        }
    }

    class LeafNode extends Node {
        char symbol;

        LeafNode(int f, char s) {
            super(f);
            symbol = s;
        }

        @Override
        void fillCodes(String code) {
            codes.put(symbol, code);
        }
    }
}
