package by.bsuir.dsa.csv2025.gr451001.Жинко;

import java.io.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    // -------- NODE STRUCTURE FOR AVL ----------
    static class Node {
        int rating;
        int id;

        Node left, right;
        int height;
        int size;

        Node(int id, int rating) {
            this.id = id;
            this.rating = rating;
            this.height = 1;
            this.size = 1;
        }
    }

    // ---------- AVL TREE IMPLEMENTATION ----------
    static class AVLTree {

        Node root;

        int height(Node n) { return n == null ? 0 : n.height; }
        int size(Node n) { return n == null ? 0 : n.size; }

        void update(Node n) {
            if (n == null) return;
            n.height = Math.max(height(n.left), height(n.right)) + 1;
            n.size = size(n.left) + size(n.right) + 1;
        }

        int balance(Node n) { return n == null ? 0 : height(n.left) - height(n.right); }

        Node rotateRight(Node y) {
            Node x = y.left;
            Node t2 = x.right;

            x.right = y;
            y.left = t2;

            update(y);
            update(x);
            return x;
        }

        Node rotateLeft(Node x) {
            Node y = x.right;
            Node t2 = y.left;

            y.left = x;
            x.right = t2;

            update(x);
            update(y);
            return y;
        }

        // compare by rating ASC, then id ASC
        int cmp(int rating1, int id1, int rating2, int id2) {
            if (rating1 != rating2) return rating1 - rating2;
            return id1 - id2;
        }

        Node insert(Node n, int id, int rating) {
            if (n == null) return new Node(id, rating);

            int c = cmp(rating, id, n.rating, n.id);
            if (c < 0)
                n.left = insert(n.left, id, rating);
            else
                n.right = insert(n.right, id, rating);

            update(n);

            int bf = balance(n);

            if (bf > 1 && cmp(rating, id, n.left.rating, n.left.id) < 0)
                return rotateRight(n);

            if (bf < -1 && cmp(rating, id, n.right.rating, n.right.id) > 0)
                return rotateLeft(n);

            if (bf > 1 && cmp(rating, id, n.left.rating, n.left.id) > 0) {
                n.left = rotateLeft(n.left);
                return rotateRight(n);
            }

            if (bf < -1 && cmp(rating, id, n.right.rating, n.right.id) < 0) {
                n.right = rotateRight(n.right);
                return rotateLeft(n);
            }

            return n;
        }

        Node minNode(Node n) {
            while (n.left != null) n = n.left;
            return n;
        }

        Node delete(Node n, int id, int rating) {
            if (n == null) return null;

            int c = cmp(rating, id, n.rating, n.id);
            if (c < 0)
                n.left = delete(n.left, id, rating);
            else if (c > 0)
                n.right = delete(n.right, id, rating);
            else {
                if (n.left == null) return n.right;
                if (n.right == null) return n.left;

                Node t = minNode(n.right);
                n.id = t.id;
                n.rating = t.rating;
                n.right = delete(n.right, t.id, t.rating);
            }

            update(n);

            int bf = balance(n);

            if (bf > 1 && balance(n.left) >= 0)
                return rotateRight(n);

            if (bf > 1 && balance(n.left) < 0) {
                n.left = rotateLeft(n.left);
                return rotateRight(n);
            }

            if (bf < -1 && balance(n.right) <= 0)
                return rotateLeft(n);

            if (bf < -1 && balance(n.right) > 0) {
                n.right = rotateRight(n.right);
                return rotateLeft(n);
            }

            return n;
        }

        void insert(int id, int rating) {
            root = insert(root, id, rating);
        }

        void delete(int id, int rating) {
            root = delete(root, id, rating);
        }

        // -------- FIND THE NODE THAT IS k-th LARGEST --------
        Node findKth(Node n, int k) {
            if (n == null) return null;

            int rightSize = size(n.right);

            if (k <= rightSize)
                return findKth(n.right, k);

            else if (k == rightSize + 1)
                return n;

            else
                return findKth(n.left, k - rightSize - 1);
        }

        // collect all ids having this rating
        void collectRating(Node n, int rating, List<Integer> out) {
            if (n == null) return;

            if (n.rating == rating) {
                out.add(n.id);
                collectRating(n.left, rating, out);
                collectRating(n.right, rating, out);
            } else if (rating < n.rating) {
                collectRating(n.left, rating, out);
            } else {
                collectRating(n.right, rating, out);
            }
        }

        List<Integer> kthPlayer(int k) {
            List<Integer> ans = new ArrayList<>();
            Node kth = findKth(root, k);
            if (kth == null) return ans;

            int targetRating = kth.rating;

            collectRating(root, targetRating, ans);

            Collections.sort(ans); // ids must be sorted
            return ans;
        }

        // -------- count players in rating range [L, R] ----------
        int countLE(Node n, int rating) {
            if (n == null) return 0;

            if (n.rating <= rating)
                return size(n.left) + 1 + countLE(n.right, rating);
            else
                return countLE(n.left, rating);
        }

        int countPlayersInRange(int L, int R) {
            return countLE(root, R) - countLE(root, L - 1);
        }
    }

    static HashMap<Integer, Integer> idToRating = new HashMap<>();
    static AVLTree tree = new AVLTree();

    static void addPlayer(int id, int rating) {
        if (rating < 0) return;

        if (idToRating.containsKey(id)) {
            int old = idToRating.get(id);
            tree.delete(id, old);
        }

        idToRating.put(id, rating);
        tree.insert(id, rating);
    }

    static void removePlayer(int id) {
        if (!idToRating.containsKey(id)) return;
        int rating = idToRating.remove(id);
        tree.delete(id, rating);
    }

    static void updateRating(int id, int newRating) {
        if (!idToRating.containsKey(id)) return;
        removePlayer(id);
        addPlayer(id, newRating);
    }

    static List<Integer> kthPlayer(int k) {
        return tree.kthPlayer(k);
    }

    static int countPlayersInRange(int L, int R) {
        return tree.countPlayersInRange(L, R);
    }

    public static void main(String[] args) throws Exception {
        idToRating = new HashMap<>();
        tree = new AVLTree();

        FastScanner fs = new FastScanner(System.in);
        StringBuilder out = new StringBuilder();

        int Q = fs.nextInt();

        while (Q-- > 0) {
            int t = fs.nextInt();

            if (t == 1) {
                addPlayer(fs.nextInt(), fs.nextInt());

            } else if (t == 2) {
                removePlayer(fs.nextInt());

            } else if (t == 3) {
                updateRating(fs.nextInt(), fs.nextInt());

            } else if (t == 4) {
                int k = fs.nextInt();
                List<Integer> res = kthPlayer(k);

                if (!res.isEmpty()) {   // ← выводим только если есть что выводить
                    for (int i = 0; i < res.size(); i++) {
                        out.append(res.get(i));
                        if (i < res.size() - 1) out.append(" ");
                    }
                    out.append("\n");
                }
            } else if (t == 5) {
                out.append(countPlayersInRange(fs.nextInt(), fs.nextInt())).append("\n");
            }
        }

        System.out.print(out);
    }

    // FAST SCANNER
    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        FastScanner(InputStream is) { in = is; }

        int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            while ((c = read()) <= ' ') if (c == -1) return -1;
            int sign = 1;
            if (c == '-') { sign = -1; c = read(); }
            int x = c - '0';
            while ((c = read()) > ' ') x = x * 10 + (c - '0');
            return x * sign;
        }
    }

    private void runTest(String input, String expectedOutput) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            Solution.main(new String[0]);

            String actual = baos.toString().replace("\r\n", "\n").trim();
            String expected = expectedOutput.replace("\r\n", "\n").trim();

            assertEquals(expected, actual);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    public void test0() throws Exception {
        String input = "9\n1 1 1500\n1 2 1700\n1 3 1200\n4 2\n3 3 1500\n4 2\n5 1400 1600\n2 2\n4 1\n";
        String expectedOutput = "1\n1 3\n2\n1 3";
        runTest(input, expectedOutput);
    }

    @Test
    public void test1() throws Exception {
        String input = "7\n1 1 1000\n1 2 2000\n1 3 3000\n2 2\n4 2\n4 1\n5 1500 4500\n";
        String expectedOutput = "1\n3\n1";
        runTest(input, expectedOutput);
    }

    @Test
    public void test2() throws Exception {
        String input = "3\n1 1 800\n1 1 900\n5 700 1000\n";
        String expectedOutput = "1";
        runTest(input, expectedOutput);
    }

    @Test
    public void test3() throws Exception {
        String input = "4\n1 1 5000\n1 2 1000\n1 3 3000\n5 1000 5000\n";
        String expectedOutput = "3";
        runTest(input, expectedOutput);
    }

    @Test
    public void test4() throws Exception {
        String input = "4\n1 1 600\n1 2 700\n3 2 600\n5 500 600\n";
        String expectedOutput = "2";
        runTest(input, expectedOutput);
    }

    @Test
    public void test5() throws Exception {
        String input = "6\n1 1 300\n1 2 400\n1 3 200\n1 2 300\n3 3 300\n4 1\n";
        String expectedOutput = "1 2 3";
        runTest(input, expectedOutput);
    }

    @Test
    public void test6() throws Exception {
        String input = "3\n4 1\n1 1 200\n4 1\n";
        String expectedOutput = "1";
        runTest(input, expectedOutput);
    }

    @Test
    public void test7() throws Exception {
        String input = "7\n1 1 1200\n1 2 1300\n1 3 1400\n1 4 1500\n4 1\n4 4\n5 1201 1501\n";
        String expectedOutput = "4\n1\n3";
        runTest(input, expectedOutput);
    }

    @Test
    public void test8() throws Exception {
        String input = "8\n1 1 1000\n1 2 2000\n1 3 3000\n4 4\n1 4 4000\n4 4\n2 1\n4 4\n";
        String expectedOutput = "1";
        runTest(input, expectedOutput);
    }

    @Test
    public void test9() throws Exception {
        String input = "8\n1 1 100\n1 2 200\n1 3 300\n4 1\n1 1 400\n4 1\n2 1\n4 1\n";
        String expectedOutput = "3\n1\n3";
        runTest(input, expectedOutput);
    }
}
