package by.bsuir.dsa.csv2025.gr410901.Гуцева;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.StringTokenizer;

@SuppressWarnings("all")
public class Solution {

    static class Node {
        long sum;
        Node left, right;

        Node(long sum, Node left, Node right) {
            this.sum = sum;
            this.left = left;
            this.right = right;
        }
    }

    static int N, Q;
    static Node[] versions;
    static int versionCount;

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner(System.in);
        StringBuilder out = new StringBuilder();

        N = fs.nextInt();
        long[] a = new long[N];
        for (int i = 0; i < N; i++) a[i] = fs.nextLong();

        Q = fs.nextInt();
        versions = new Node[Q + 1];
        versions[0] = build(a, 0, N - 1);
        versionCount = 0;

        for (int qi = 0; qi < Q; qi++) {
            String op = fs.next();
            if (op == null) break;

            switch (op) {
                case "ADD" -> {
                    int v = fs.nextInt();
                    int i = fs.nextInt() - 1;
                    long x = fs.nextLong();
                    versions[++versionCount] = update(versions[v], 0, N - 1, i, x);
                }
                case "MOVE" -> {
                    int v = fs.nextInt();
                    int i = fs.nextInt() - 1;
                    int j = fs.nextInt() - 1;
                    long x = fs.nextLong();
                    Node tmp = update(versions[v], 0, N - 1, i, -x);
                    versions[++versionCount] = update(tmp, 0, N - 1, j, x);
                }
                case "CLONE" -> {
                    int v = fs.nextInt();
                    versions[++versionCount] = versions[v];
                }
                case "SUM" -> {
                    int v = fs.nextInt();
                    int l = fs.nextInt() - 1;
                    int r = fs.nextInt() - 1;
                    out.append(query(versions[v], 0, N - 1, l, r)).append("\n");
                }
                case "DELTA" -> {
                    int v1 = fs.nextInt();
                    int v2 = fs.nextInt();
                    int l = fs.nextInt() - 1;
                    int r = fs.nextInt() - 1;
                    long s1 = query(versions[v1], 0, N - 1, l, r);
                    long s2 = query(versions[v2], 0, N - 1, l, r);
                    out.append(s2 - s1).append("\n");
                }
            }
        }

        System.out.print(out);
    }

    static Node build(long[] a, int l, int r) {
        if (l == r) return new Node(a[l], null, null);
        int m = (l + r) / 2;
        Node L = build(a, l, m);
        Node R = build(a, m + 1, r);
        return new Node(L.sum + R.sum, L, R);
    }

    static Node update(Node node, int l, int r, int pos, long delta) {
        Node n = new Node(node.sum, node.left, node.right);
        if (l == r) {
            n.sum += delta;
            return n;
        }
        int m = (l + r) / 2;
        if (pos <= m) n.left = update(node.left, l, m, pos, delta);
        else n.right = update(node.right, m + 1, r, pos, delta);
        n.sum = n.left.sum + n.right.sum;
        return n;
    }

    static long query(Node node, int l, int r, int ql, int qr) {
        if (node == null || qr < l || ql > r) return 0;
        if (ql <= l && r <= qr) return node.sum;
        int m = (l + r) / 2;
        return query(node.left, l, m, ql, qr) +
                query(node.right, m + 1, r, ql, qr);
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream is) {
            br = new BufferedReader(new InputStreamReader(is));
        }

        String next() throws IOException {
            while (st == null || !st.hasMoreTokens()) {
                String line = br.readLine();
                if (line == null) return null;
                st = new StringTokenizer(line);
            }
            return st.nextToken();
        }

        int nextInt() throws IOException { return Integer.parseInt(next()); }
        long nextLong() throws IOException { return Long.parseLong(next()); }
    }


    private String run(String input) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        System.setIn(in);
        System.setOut(new PrintStream(out));

        try {
            main(new String[0]);
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }

        return out.toString().trim().replace("\r\n", "\n");
    }

    @Test
    public void testExample1() throws Exception {
        String input = """
                5
                10 20 30 40 50
                7
                SUM 0 1 5
                ADD 0 3 5
                SUM 1 1 5
                CLONE 1
                MOVE 2 5 1 10
                SUM 2 1 5
                DELTA 1 3 1 5
                """;

        assertEquals("""
                150
                155
                155
                0""", run(input));
    }

    @Test
    public void testExample2() throws Exception {
        String input = """
                3
                5 0 10
                6
                SUM 0 1 3
                ADD 0 2 7
                SUM 1 1 3
                CLONE 1
                MOVE 2 1 3 4
                SUM 2 1 3
                """;

        assertEquals("""
                15
                22
                22""", run(input));
    }

    @Test
    public void testExample3() throws Exception {
        String input = """
                4
                0 0 0 0
                8
                ADD 0 1 10
                ADD 1 4 5
                SUM 2 1 4
                CLONE 2
                MOVE 3 1 2 3
                SUM 3 1 4
                DELTA 2 3 1 2
                SUM 0 1 4
                """;

        assertEquals("""
                15
                15
                0
                0""", run(input));
    }

    @Test
    public void testExample4() throws Exception {
        String input = """
                2
                100 200
                6
                SUM 0 1 2
                ADD 0 1 -50
                SUM 1 1 2
                CLONE 1
                DELTA 0 2 1 1
                SUM 2 1 2
                """;

        assertEquals("""
                300
                250
                -50
                250""", run(input));
    }

    @Test
    public void testExample5() throws Exception {
        String input = """
                4
                5 5 5 5
                9
                SUM 0 1 4
                ADD 0 2 10
                ADD 1 4 -3
                SUM 2 1 4
                CLONE 2
                MOVE 3 2 1 4
                SUM 3 1 4
                DELTA 1 3 1 2
                DELTA 0 3 3 4
                """;

        assertEquals("""
                20
                27
                27
                0
                -3""", run(input));
    }

    @Test
    public void testExample6() throws Exception {
        String input = """
                3
                1 2 3
                8
                SUM 0 1 3
                ADD 0 1 5
                CLONE 1
                MOVE 2 3 2 4
                SUM 1 1 3
                SUM 2 1 3
                DELTA 0 1 1 1
                DELTA 1 3 2 3
                """;

        assertEquals("""
                6
                11
                11
                5
                0""", run(input));
    }

    @Test
    public void testExample7() throws Exception {
        String input = """
                4
                100 0 50 0
                9
                SUM 0 1 4
                ADD 0 2 25
                SUM 1 1 4
                MOVE 1 2 4 10
                SUM 2 1 4
                CLONE 2
                ADD 3 1 -15
                SUM 3 1 4
                DELTA 1 3 1 2
                """;

        assertEquals("""
                150
                175
                175
                175
                -10""", run(input));
    }

    @Test
    public void testExample8() throws Exception {
        String input = """
                3
                0 0 0
                9
                ADD 0 1 10
                ADD 1 2 20
                ADD 2 3 30
                SUM 3 1 3
                CLONE 3
                MOVE 4 3 1 5
                SUM 4 1 3
                DELTA 3 4 1 1
                DELTA 0 3 2 3
                """;

        assertEquals("""
                60
                60
                0
                50""", run(input));
    }

    @Test
    public void testExample9() throws Exception {
        String input = """
                4
                1 2 3 4
                10
                SUM 0 1 4
                ADD 0 4 6
                SUM 1 2 4
                CLONE 1
                MOVE 2 2 3 2
                SUM 2 1 4
                DELTA 0 1 4 4
                DELTA 1 2 2 3
                ADD 2 1 -1
                SUM 3 1 4
                """;

        assertEquals("""
                10
                15
                16
                6
                0
                16""", run(input));
    }

    @Test
    public void testExample10() throws Exception {
        String input = """
                5
                3 3 3 3 3
                10
                SUM 0 1 5
                ADD 0 2 4
                ADD 1 4 -1
                SUM 2 2 4
                CLONE 2
                MOVE 3 1 5 2
                SUM 3 1 5
                DELTA 0 2 1 3
                DELTA 1 3 4 5
                SUM 0 2 4
                """;

        assertEquals("""
                15
                12
                18
                4
                -1
                9""", run(input));
    }
}