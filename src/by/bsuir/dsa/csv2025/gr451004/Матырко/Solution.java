package by.bsuir.dsa.csv2025.gr451004.Матырко;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class Solution {

    /* ================= BST ================= */

    static class Node {
        int value;
        Node left, right;

        Node(int v) {
            value = v;
        }
    }

    static class BST {
        Node root;

        void add(int x) {
            root = insert(root, x);
        }

        private Node insert(Node n, int x) {
            if (n == null) return new Node(x);
            if (x < n.value) n.left = insert(n.left, x);
            else n.right = insert(n.right, x);
            return n;
        }

        boolean find(int x) {
            return search(root, x);
        }

        private boolean search(Node n, int x) {
            if (n == null) return false;
            if (x == n.value) return true;
            return x < n.value ? search(n.left, x) : search(n.right, x);
        }

        void del(int x) {
            root = delete(root, x);
        }

        private Node delete(Node n, int x) {
            if (n == null) return null;

            if (x < n.value) n.left = delete(n.left, x);
            else if (x > n.value) n.right = delete(n.right, x);
            else {
                if (n.left == null) return n.right;
                if (n.right == null) return n.left;

                Node min = getMin(n.right);
                n.value = min.value;
                n.right = delete(n.right, min.value);
            }
            return n;
        }

        private Node getMin(Node n) {
            while (n.left != null) n = n.left;
            return n;
        }

        Integer min() {
            if (root == null) return null;
            Node n = root;
            while (n.left != null) n = n.left;
            return n.value;
        }

        Integer max() {
            if (root == null) return null;
            Node n = root;
            while (n.right != null) n = n.right;
            return n.value;
        }

        int sum() {
            return sumRec(root);
        }

        private int sumRec(Node n) {
            if (n == null) return 0;
            return n.value + sumRec(n.left) + sumRec(n.right);
        }
    }

    /* ================= MAIN ================= */

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;

        String[] t = sc.nextLine().split(" ");
        BST tree = new BST();

        int i = 0;
        while (i < t.length) {
            switch (t[i]) {
                case "add" -> {
                    tree.add(Integer.parseInt(t[i + 1]));
                    i += 2;
                }
                case "del" -> {
                    tree.del(Integer.parseInt(t[i + 1]));
                    i += 2;
                }
                case "find" -> {
                    boolean r = tree.find(Integer.parseInt(t[i + 1]));
                    System.out.println(r ? "TRUE" : "FALSE");
                    i += 2;
                }
                case "min" -> {
                    Integer m = tree.min();
                    System.out.println(m == null ? "null" : m);
                    i++;
                }
                case "max" -> {
                    Integer m = tree.max();
                    System.out.println(m == null ? "null" : m);
                    i++;
                }
                case "sum" -> {
                    System.out.println(tree.sum());
                    i++;
                }
                default -> i++;
            }
        }
    }

    /* ================= TEST INFRA ================= */

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Before
    public void setup() {
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
    }

    @After
    public void restore() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private String run(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        out.reset();
        Solution.main(null);
        return out.toString(StandardCharsets.UTF_8)
                .replace("\r\n", "\n")
                .trim();

    }

    /* ================= TESTS ================= */

    @Test
    public void in0() {
        assertEquals("28\n24", run("add 8 add 4 add 12 add 4 sum del 4 sum"));
    }

    @Test
    public void in1() {
        assertEquals("TRUE\nFALSE", run("add 10 add 5 add 15 find 5 find 11"));
    }

    @Test
    public void in2() {
        assertEquals("1\n9", run("add 7 add 3 add 9 add 1 add 4 min max"));
    }

    @Test
    public void in3() {
        assertEquals("350", run("add 50 add 30 add 70 add 20 add 40 add 60 add 80 sum"));
    }

    @Test
    public void in4() {
        assertEquals("FALSE", run("find 10"));
    }

    @Test
    public void in5() {
        assertEquals("15", run("add 5 add 5 add 5 sum"));
    }

    @Test
    public void in6() {
        assertEquals("9\n10", run("add 9 add 8 add 10 del 8 min max"));
    }

    @Test
    public void in7() {
        assertEquals("250", run("add 100 add 50 add 200 del 100 sum"));
    }

    @Test
    public void in8() {
        assertEquals("21\n16", run("add 3 add 1 add 2 add 5 add 4 add 6 sum del 5 sum"));
    }

    @Test
    public void in9() {
        assertEquals("45", run("add 10 add 20 add 5 add 15 add 25 del 20 del 10 sum"));
    }
}
