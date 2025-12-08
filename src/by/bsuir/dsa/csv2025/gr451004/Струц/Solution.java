package by.bsuir.dsa.csv2025.gr451004.Струц;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.junit.After;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Solution {
    public static class Flow {
        int[][] capacity;
        int count;

        public Flow(int count) {
            this.count = count;
            this.capacity = new int[count][count];
        }

        public void addEdge(int u, int v, int cap) {
            capacity[u][v] = cap;
        }

        private boolean bfs(int s, int t, int[] parent) {
            boolean[] visited = new boolean[count];
            Arrays.fill(visited, false);

            Queue<Integer> q = new LinkedList<>();
            q.add(s);
            visited[s] = true;
            parent[s] = -1;

            while (!q.isEmpty()) {
                int u = q.poll();

                for (int v = 0; v < count; v++) {
                    if (!visited[v] && capacity[u][v] > 0) {
                        parent[v] = u;
                        visited[v] = true;
                        q.add(v);
                        if (v == t) return true;
                    }
                }
            }
            return false;
        }

        public int maxFlow(int s, int t) {
            int maxFlow = 0;
            int[] parent = new int[count];

            while (bfs(s, t, parent)) {
                int pathFlow = Integer.MAX_VALUE;

                for (int v = t; v != s; v = parent[v]) {
                    int u = parent[v];
                    pathFlow = Math.min(pathFlow, capacity[u][v]);
                }

                for (int v = t; v != s; v = parent[v]) {
                    int u = parent[v];
                    capacity[u][v] -= pathFlow;
                    capacity[v][u] += pathFlow;
                }

                maxFlow += pathFlow;
            }
            return maxFlow;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int gifts = scanner.nextInt();
        int students = scanner.nextInt();
        int wishlistSize = scanner.nextInt();

        int source = 0;
        int sink = students + gifts + 1;
        int totalNodes = sink + 1;

        Flow solver = new Flow(totalNodes);

        for (int i = 1; i <= students; i++) {
            solver.addEdge(source, i, 1);
        }

        for (int j = 1; j <= gifts; j++) {
            int giftNodeIndex = students + j;
            solver.addEdge(giftNodeIndex, sink, 1);
        }

        for (int i = 1; i <= students; i++) {
            for (int k = 0; k < wishlistSize; k++) {
                int giftId = scanner.nextInt();

                if (giftId >= 1 && giftId <= gifts) {
                    int giftNodeIndex = students + giftId;
                    solver.addEdge(i, giftNodeIndex, 1);
                }
            }
        }

        System.out.println(solver.maxFlow(source, sink));
    }

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void setup() {
        System.setOut(new PrintStream(outputStreamCaptor, true, StandardCharsets.UTF_8));
    }

    @After
    public void returnAllStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private String runTest(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);
        Solution.main(null);
        return outputStreamCaptor.toString(StandardCharsets.UTF_8).trim();
    }

    @Test
    public void test1() {
        String input =
                """
                        2
                        3 1
                        1
                        1
                        1""";
        assertEquals("Test Case 1", "1", runTest(input));
    }

    @Test
    public void test2() {
        String input =
                """
                        3
                        3 2
                        1 2
                        2 3
                        1 3""";
        assertEquals("Test Case 2", "3", runTest(input));
    }

    @Test
    public void test3() {
        String input =
                """
                        4
                        4 3
                        1 2 3
                        2 3 1
                        4 3 2
                        1 4 2""";
        assertEquals("Test Case 3", "4", runTest(input));
    }

    @Test
    public void test4() {
        String input =
                """
                        3
                        3 1
                        1
                        2
                        3""";
        assertEquals("Test Case 4", "3", runTest(input));
    }

    @Test
    public void test5() {
        String input =
                """
                        0
                        3 1
                        1
                        2
                        3""";
        assertEquals("Test Case 5", "0", runTest(input));
    }
}