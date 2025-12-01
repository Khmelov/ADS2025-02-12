package by.bsuir.dsa.csv2025.gr451001.Соболь;

import java.io.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    static class Node {
        long minVal;
        long lazy;
        int r, g, b;
    }

    static Node[] tree;
    static int[] initialBrightness;
    static char[] initialColors;
    static int n;

    public static void main(String[] args) throws IOException {
        FastScanner sc = new FastScanner(System.in);
        PrintWriter out = new PrintWriter(System.out);

        n = sc.nextInt();
        int q = sc.nextInt();

        initialBrightness = new int[n];
        for (int i = 0; i < n; i++) {
            initialBrightness[i] = sc.nextInt();
        }

        initialColors = new char[n];
        for (int i = 0; i < n; i++) {
            String s = sc.next();
            initialColors[i] = s.charAt(0);
        }

        tree = new Node[4 * n];
        for (int i = 0; i < 4 * n; i++) {
            tree[i] = new Node();
        }

        build(0, 0, n - 1);

        for (int i = 0; i < q; i++) {
            int type = sc.nextInt();
            if (type == 1) {
                int l = sc.nextInt() - 1;
                int r = sc.nextInt() - 1;
                int v = sc.nextInt();
                updateBrightness(0, 0, n - 1, l, r, v);
            } else if (type == 2) {
                int idx = sc.nextInt() - 1;
                char color = sc.next().charAt(0);
                updateColor(0, 0, n - 1, idx, color);
            } else if (type == 3) {
                int l = sc.nextInt() - 1;
                int r = sc.nextInt() - 1;
                long min = queryMin(0, 0, n - 1, l, r);
                out.println(min <= 0 ? "YES" : "NO");
            } else if (type == 4) {
                int l = sc.nextInt() - 1;
                int r = sc.nextInt() - 1;
                char color = sc.next().charAt(0);
                out.println(queryColor(0, 0, n - 1, l, r, color));
            }
        }

        out.flush();
    }

    static void pull(int v) {
        int left = 2 * v + 1;
        int right = 2 * v + 2;
        tree[v].minVal = Math.min(tree[left].minVal, tree[right].minVal);
        tree[v].r = tree[left].r + tree[right].r;
        tree[v].g = tree[left].g + tree[right].g;
        tree[v].b = tree[left].b + tree[right].b;
    }

    static void push(int v) {
        if (tree[v].lazy != 0) {
            int left = 2 * v + 1;
            int right = 2 * v + 2;
            tree[left].lazy += tree[v].lazy;
            tree[left].minVal += tree[v].lazy;
            tree[right].lazy += tree[v].lazy;
            tree[right].minVal += tree[v].lazy;
            tree[v].lazy = 0;
        }
    }

    static void build(int v, int tl, int tr) {
        if (tl == tr) {
            tree[v].minVal = initialBrightness[tl];
            tree[v].r = (initialColors[tl] == 'r') ? 1 : 0;
            tree[v].g = (initialColors[tl] == 'g') ? 1 : 0;
            tree[v].b = (initialColors[tl] == 'b') ? 1 : 0;
        } else {
            int tm = (tl + tr) / 2;
            build(2 * v + 1, tl, tm);
            build(2 * v + 2, tm + 1, tr);
            pull(v);
        }
    }

    static void updateBrightness(int v, int tl, int tr, int l, int r, int add) {
        if (l > r) return;
        if (l == tl && r == tr) {
            tree[v].minVal += add;
            tree[v].lazy += add;
        } else {
            push(v);
            int tm = (tl + tr) / 2;
            updateBrightness(2 * v + 1, tl, tm, l, Math.min(r, tm), add);
            updateBrightness(2 * v + 2, tm + 1, tr, Math.max(l, tm + 1), r, add);
            pull(v);
        }
    }



    static void updateColor(int v, int tl, int tr, int pos, char newColor) {
        if (tl == tr) {
            tree[v].r = (newColor == 'r') ? 1 : 0;
            tree[v].g = (newColor == 'g') ? 1 : 0;
            tree[v].b = (newColor == 'b') ? 1 : 0;
        } else {
            push(v);
            int tm = (tl + tr) / 2;
            if (pos <= tm) {
                updateColor(2 * v + 1, tl, tm, pos, newColor);
            } else {
                updateColor(2 * v + 2, tm + 1, tr, pos, newColor);
            }
            pull(v);
        }
    }

    static long queryMin(int v, int tl, int tr, int l, int r) {
        if (l > r) return Long.MAX_VALUE;
        if (l == tl && r == tr) {
            return tree[v].minVal;
        }
        push(v);
        int tm = (tl + tr) / 2;
        return Math.min(
                queryMin(2 * v + 1, tl, tm, l, Math.min(r, tm)),
                queryMin(2 * v + 2, tm + 1, tr, Math.max(l, tm + 1), r)
        );
    }

    static int queryColor(int v, int tl, int tr, int l, int r, char color) {
        if (l > r) return 0;
        if (l == tl && r == tr) {
            if (color == 'r') return tree[v].r;
            if (color == 'g') return tree[v].g;
            if (color == 'b') return tree[v].b;
            return 0;
        }
        push(v);
        int tm = (tl + tr) / 2;
        return queryColor(2 * v + 1, tl, tm, l, Math.min(r, tm), color) +
                queryColor(2 * v + 2, tm + 1, tr, Math.max(l, tm + 1), r, color);
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        public FastScanner(InputStream is) {
            br = new BufferedReader(new InputStreamReader(is));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    String line = br.readLine();
                    if (line == null) return null;
                    st = new StringTokenizer(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
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

            String actual = baos.toString().replace("\r\n", "\n");
            String expected = expectedOutput.replace("\r\n", "\n");

            assertEquals(expected, actual);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }


    @Test
    public void test1() throws Exception {
        runTest("5 4\n10 20 30 40 50\nr r g g b\n3 1 5\n1 2 4 -30\n3 2 4\n4 1 5 g\n",
                "NO\nYES\n2\n");
    }

    @Test
    public void test2() throws Exception {
        runTest("5 12\n10 10 10 10 10\nr r r r r\n1 1 3 -10\n3 1 5\n3 4 5\n4 1 5 r\n2 3 g\n4 1 5 r\n4 1 5 g\n1 2 4 5\n3 1 1\n3 2 2\n3 3 3\n4 3 3 g\n",
                "YES\nNO\n5\n4\n1\nYES\nNO\nNO\n1\n");
    }

    @Test
    public void test3() throws Exception {
        runTest("6 10\n100 100 100 100 100 100\nr g b r g b\n4 1 6 r\n4 1 6 g\n4 1 6 b\n2 1 b\n2 6 r\n4 1 6 r\n4 1 6 g\n4 1 6 b\n4 1 3 b\n4 4 6 r\n",
                "2\n2\n2\n2\n2\n2\n2\n2\n");
    }

    @Test
    public void test4() throws Exception {
        runTest("3 9\n5 5 5\ng g g\n1 1 3 -5\n3 1 1\n3 2 2\n3 3 3\n1 2 2 1\n3 1 1\n3 2 2\n1 1 3 -100\n3 1 3\n",
                "YES\nYES\nYES\nYES\nNO\nYES\n");
    }

    @Test
    public void test5() throws Exception {
        runTest("10 8\n20 20 20 20 20 20 20 20 20 20\nr r r r r b b b b b\n1 1 5 -25\n3 1 10\n3 6 10\n4 1 10 r\n4 1 10 b\n1 6 10 10\n3 6 10\n4 1 5 r\n",
                "YES\nNO\n5\n5\nNO\n5\n");
    }

    @Test
    public void test6() throws Exception {
        runTest("1 10\n10\nr\n3 1 1\n1 1 1 -10\n3 1 1\n4 1 1 r\n2 1 b\n4 1 1 r\n4 1 1 b\n1 1 1 1\n3 1 1\n4 1 1 b\n",
                "NO\nYES\n1\n0\n1\nNO\n1\n");
    }

    @Test
    public void test7() throws Exception {
        runTest("2 6\n5 5\nr r\n3 1 2\n1 1 1 -5\n3 1 2\n2 1 g\n4 1 2 g\n4 1 2 r\n",
                "NO\nYES\n1\n1\n");
    }

    @Test
    public void test8() throws Exception {
        runTest("10 8\n0 0 0 0 0 0 0 0 0 0\nr r r r r r r r r r\n1 1 10 10\n1 2 9 10\n1 3 8 10\n1 4 7 10\n1 5 6 10\n3 1 10\n1 1 10 -25\n3 1 10\n",
                "NO\nYES\n");
    }

    @Test
    public void test9() throws Exception {
        runTest("15 7\n10 10 10 10 10 10 10 10 10 10 10 10 10 10 10\nr g b r g b r g b r g b r g b\n4 1 15 r\n2 4 b\n4 1 15 r\n4 1 15 b\n1 1 15 -1000\n3 1 15\n4 2 14 g\n",
                "5\n4\n6\nYES\n5\n");
    }

    @Test
    public void test10() throws Exception {
        runTest("10 10\n100 100 100 100 100 100 100 100 100 100\nr r r r r b b b b b\n1 1 2 -200\n1 3 4 -200\n1 5 6 -200\n3 1 2\n3 3 4\n3 5 6\n3 7 8\n4 1 10 r\n2 5 r\n4 1 10 r\n",
                "YES\nYES\nYES\nNO\n5\n5\n");
    }

}