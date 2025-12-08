package by.bsuir.dsa.csv2025.gr451003.Епанчинцев;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class Solution {
    
    public static String solve(String input) {
        StringTokenizer st = new StringTokenizer(input);
        if (!st.hasMoreTokens()) {
            return "";
        }

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int s = Integer.parseInt(st.nextToken());

        // списки смежности
        List<List<Integer>> graph = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        int[] dist = new int[n + 1];
        Arrays.fill(dist, -1);
        dist[s] = 0;

        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(s);

        while (!q.isEmpty()) {
            int v = q.removeFirst();
            for (int to : graph.get(v)) {
                if (dist[to] == -1) {
                    dist[to] = dist[v] + 1;
                    q.addLast(to);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            if (i > 1) sb.append(' ');
            sb.append(dist[i]);
        }
        return sb.toString();
    }

    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder inputBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            inputBuilder.append(line).append('\n');
        }
        String input = inputBuilder.toString();
        String output = solve(input);
        if (!output.isEmpty()) {
            System.out.println(output);
        }
    }


    @Test
    public void test_1_chain() {
        String inp =
                "4 3 1\n" +
                        "1 2\n" +
                        "2 3\n" +
                        "3 4\n";
        String expected = "0 1 2 3";
        String actual = Solution.solve(inp).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void test_2_isolated_start() {
        String inp =
                "3 1 1\n" +
                        "2 3\n";
        String expected = "0 -1 -1";
        String actual = Solution.solve(inp).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void test_3_star() {
        String inp =
                "5 4 1\n" +
                        "1 2\n" +
                        "1 3\n" +
                        "1 4\n" +
                        "1 5\n";
        String expected = "0 1 1 1 1";
        String actual = Solution.solve(inp).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void test_4_cycle() {
        String inp =
                "4 4 2\n" +
                        "1 2\n" +
                        "2 3\n" +
                        "3 4\n" +
                        "4 1\n";
        String expected = "1 0 1 2";
        String actual = Solution.solve(inp).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void test_5_tree() {
        String inp =
                "7 6 4\n" +
                        "1 2\n" +
                        "1 3\n" +
                        "2 4\n" +
                        "3 5\n" +
                        "5 6\n" +
                        "5 7\n";
        String expected = "2 1 3 0 4 5 5";
        String actual = Solution.solve(inp).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void test_6_two_components_start_first() {
        String inp =
                "6 4 3\n" +
                        "1 2\n" +
                        "2 3\n" +
                        "4 5\n" +
                        "5 6\n";
        String expected = "2 1 0 -1 -1 -1";
        String actual = Solution.solve(inp).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void test_7_single_vertex() {
        String inp =
                "1 0 1\n";
        String expected = "0";
        String actual = Solution.solve(inp).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void test_8_chain_middle_start() {
        String inp =
                "5 4 3\n" +
                        "1 2\n" +
                        "2 3\n" +
                        "3 4\n" +
                        "4 5\n";
        String expected = "2 1 0 1 2";
        String actual = Solution.solve(inp).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void test_9_two_components_start_second() {
        String inp =
                "5 3 4\n" +
                        "1 2\n" +
                        "2 3\n" +
                        "4 5\n";
        String expected = "-1 -1 -1 0 1";
        String actual = Solution.solve(inp).trim();
        assertEquals(expected, actual);
    }

    @Test
    public void test_10_complex_cycle() {
        String inp =
                "8 9 5\n" +
                        "1 2\n" +
                        "2 3\n" +
                        "3 4\n" +
                        "4 1\n" +
                        "3 5\n" +
                        "5 6\n" +
                        "6 7\n" +
                        "7 8\n" +
                        "8 3\n";
        String expected = "3 2 1 2 0 1 2 2";
        String actual = Solution.solve(inp).trim();
        assertEquals(expected, actual);
    }
} 