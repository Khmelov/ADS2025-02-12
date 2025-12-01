package by.bsuir.dsa.csv2025.gr410901.Калач;

import java.util.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Solution {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int k = sc.nextInt();
        int m = sc.nextInt();

        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            edges.add(new int[]{ sc.nextInt(), sc.nextInt() });
        }

        boolean result = canColorGraph(n, k, edges);
        System.out.println(result ? "true" : "false");
    }

    static boolean canColorGraph(int n, int k, List<int[]> edges) {

        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());

        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
            g.get(e[1]).add(e[0]);
        }

        int[] color = new int[n];
        return backtrack(0, color, k, g);
    }

    private static boolean backtrack(int v, int[] color, int k, List<List<Integer>> g) {
        if (v == color.length) return true;

        for (int c = 1; c <= k; c++) {
            if (isValid(v, c, color, g)) {
                color[v] = c;
                if (backtrack(v + 1, color, k, g)) return true;
                color[v] = 0;
            }
        }
        return false;
    }

    private static boolean isValid(int v, int c, int[] color, List<List<Integer>> g) {
        for (int u : g.get(v)) {
            if (color[u] == c) return false;
        }
        return true;
    }

    public String runTest(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        Solution.main(new String[]{});

        return output.toString().trim();
    }

    @Test
    public void test1() {
        assertEquals("true", runTest("3 3 2\n0 1\n1 2\n"));
    }

    @Test
    public void test2() {
        assertEquals("false", runTest("3 1 3\n0 1\n1 2\n0 2\n"));
    }

    @Test
    public void test3() {
        assertEquals("true", runTest("4 2 4\n0 1\n1 2\n2 3\n3 0\n"));
    }

    @Test
    public void test4() {
        assertEquals("false", runTest("5 2 5\n0 1\n1 2\n2 3\n3 4\n4 0\n"));
    }

    @Test
    public void test5() {
        assertEquals("true", runTest("1 1 0\n"));
    }

    @Test
    public void test6() {
        assertEquals("true", runTest("4 3 3\n0 1\n1 2\n2 3\n"));
    }

    @Test
    public void test7() {
        assertEquals("true", runTest("6 2 5\n0 1\n1 2\n2 3\n3 4\n1 5\n"));
    }

    @Test
    public void test8() {
        assertEquals("false", runTest("4 2 6\n0 1\n0 2\n0 3\n1 2\n1 3\n2 3\n"));
    }

    @Test
    public void test9() {
        assertEquals("true", runTest("5 3 4\n0 1\n1 2\n2 3\n3 4\n"));
    }

    @Test
    public void test10() {
        assertEquals("true", runTest("6 3 6\n0 1\n1 2\n2 0\n3 4\n4 5\n5 3\n"));
    }

}