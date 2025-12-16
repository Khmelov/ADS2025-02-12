package by.bsuir.dsa.csv2025.gr451003.Бернат;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class Solution {

    static class Edge {
        int to;
        boolean used;

        Edge(int to) { this.to = to; this.used = false; }
    }

    static Map<Integer, List<Edge>> adj;
    static List<Integer> cycle;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;
        String input = sc.nextLine();
        System.out.println(solve(input));
    }

    static String solve(String input) {
        adj = new HashMap<>();
        cycle = new ArrayList<>();

        String trimmed = input.trim();
        if (trimmed.isEmpty()) return "NO";

        String[] edges = trimmed.split(" +");
        Map<Integer, Integer> degree = new HashMap<>();

        for (String e : edges) {
            String[] parts = e.split("-");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);

            adj.putIfAbsent(u, new ArrayList<>());
            adj.putIfAbsent(v, new ArrayList<>());

            adj.get(u).add(new Edge(v));
            adj.get(v).add(new Edge(u));

            degree.put(u, degree.getOrDefault(u, 0) + 1);
            degree.put(v, degree.getOrDefault(v, 0) + 1);
        }

        for (int deg : degree.values()) {
            if (deg % 2 != 0) return "NO";
        }

        int start = adj.keySet().iterator().next();
        dfs(start);

        for (List<Edge> list : adj.values())
            for (Edge e : list)
                if (!e.used) return "NO";

        Collections.reverse(cycle);
        StringBuilder sb = new StringBuilder("YES");
        for (int v : cycle) sb.append(' ').append(v);
        return sb.toString();
    }

    static void dfs(int v) {
        List<Edge> edges = adj.get(v);
        if (edges == null) return;
        for (Edge e : edges) {
            if (!e.used) {
                e.used = true;
                for (Edge back : adj.get(e.to)) {
                    if (back.to == v && !back.used) {
                        back.used = true;
                        break;
                    }
                }
                dfs(e.to);
            }
        }
        cycle.add(v);
    }


    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void setup() {
        System.setOut(new PrintStream(outputStreamCaptor, true, StandardCharsets.UTF_8));
    }

    @After
    public void restore() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        outputStreamCaptor.reset();
    }

    private String runTest(String input) {
        ByteArrayInputStream inStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(inStream);
        Solution.main(null);
        return outputStreamCaptor.toString(StandardCharsets.UTF_8).trim();
    }


    @Test public void test1() { assertEquals("YES 1 2 3 1", runTest("1-2 2-3 3-1")); }
    @Test public void test2() { assertEquals("NO", runTest("1-2 2-3 3-4")); }
    @Test public void test3() { assertEquals("NO", runTest("1-2 2-3 3-4 4-1 2-4 1-3")); }
    @Test public void test4() { assertEquals("NO", runTest("1-2 2-3 3-4 4-5")); }
    @Test public void test5() { assertEquals("NO", runTest("1-2 2-3 3-4 4-1 1-3 2-4")); }
    @Test public void test6() { assertEquals("NO", runTest("1-2 2-3 3-1 1-4 4-2")); }
    @Test public void test7() { assertEquals("YES 1 2 3 4 5 6 1", runTest("1-2 2-3 3-4 4-5 5-6 6-1")); }
    @Test public void test8() { assertEquals("NO", runTest("1-2 2-3 3-4 4-1 1-3")); }
    @Test public void test9() { assertEquals("YES 1 2 3 1 2 3 1", runTest("1-2 2-3 3-1 1-2 2-3 3-1")); }
    @Test public void test10() { assertEquals("YES 1 2 3 4 5 1", runTest("1-2 2-3 3-4 4-5 5-1")); }

}
