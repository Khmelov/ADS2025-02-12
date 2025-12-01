package by.bsuir.dsa.csv2025.gr451002.Багузов;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Solution {

    public static class Flow {
        int n;
        int[][] capacity;

        public Flow(int n) {
            this.n = n;
            this.capacity = new int[n][n];
        }

        public void addEdge(int u, int v, int c) {
            capacity[u][v] = c;
        }

        private boolean bfs(int s, int t, int[] parent) {
            boolean[] used = new boolean[n];
            Arrays.fill(parent, -1);
            Queue<Integer> q = new LinkedList<>();
            q.add(s);
            used[s] = true;

            while (!q.isEmpty()) {
                int u = q.poll();
                for (int v = 0; v < n; v++) {
                    if (!used[v] && capacity[u][v] > 0) {
                        used[v] = true;
                        parent[v] = u;
                        q.add(v);
                        if (v == t) return true;
                    }
                }
            }
            return false;
        }

        public int maxFlow(int s, int t) {
            int flow = 0;
            int[] parent = new int[n];

            while (bfs(s, t, parent)) {
                int path = Integer.MAX_VALUE;
                for (int v = t; v != s; v = parent[v]) {
                    int u = parent[v];
                    path = Math.min(path, capacity[u][v]);
                }
                for (int v = t; v != s; v = parent[v]) {
                    int u = parent[v];
                    capacity[u][v] -= path;
                    capacity[v][u] += path;
                }
                flow += path;
            }
            return flow;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();

        int s = 0;
        int t = n - 1;

        Flow flow = new Flow(n);

        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int c = sc.nextInt();
            edges.add(new int[]{u, v, c});
            flow.addEdge(u, v, c);
        }

        int maxFlow = flow.maxFlow(s, t);
        int target = maxFlow / 2;

        int k = m;
        for (int mask = 0; mask < (1 << m); mask++) {
            int cnt = Integer.bitCount(mask);
            if (cnt >= k) continue;

            Flow f2 = new Flow(n);
            for (int i = 0; i < m; i++) {
                if ((mask & (1 << i)) == 0) {
                    int[] e = edges.get(i);
                    f2.addEdge(e[0], e[1], e[2]);
                }
            }

            int newFlow = f2.maxFlow(s, t);
            if (newFlow <= target) k = cnt;
        }

        System.out.println(k);
    }

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @Before
    public void setup() {
        System.setOut(new PrintStream(outputStreamCaptor, true, StandardCharsets.UTF_8));
    }

    @After
    public void reset() {
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
                4 5
                0 1 3
                1 3 3
                0 2 3
                2 3 3
                1 2 1
                """;
        assertEquals("1", runTest(input));
    }

    @Test
    public void test2() {
        String input =
                """
                4 4
                0 1 2
                1 3 2
                0 2 2
                2 3 2
                """;
        assertEquals("2", runTest(input));
    }

    @Test
    public void test3() {
        String input =
                """
                3 3
                0 1 5
                1 2 5
                0 2 1
                """;
        assertEquals("1", runTest(input));
    }

    @Test
    public void test4() {
        String input =
                """
                5 6
                0 1 4
                1 4 4
                0 2 4
                2 4 4
                1 3 1
                3 4 1
                """;
        assertEquals("1", runTest(input));
    }

    @Test
    public void test5() {
        String input =
                """
                2 1
                0 1 10
                """;
        assertEquals("1", runTest(input));
    }
}
