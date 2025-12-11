package by.bsuir.dsa.csv2025.gr410901.Евтуховская;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class Solution {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    static class Edge {
        int to;
        long w;
        Edge(int t, long w) { this.to = t; this.w = w; }
    }
     static class Dijkstra {
        final List<List<Edge>> g;
        final int n;

        Dijkstra(int n) {
            this.n = n;
            g = new ArrayList<>(n + 1);
            for (int i = 0; i <= n; i++) g.add(new ArrayList<>());
        }

        void addEdge(int u, int v, long w) {
            g.get(u).add(new Edge(v, w));
            g.get(v).add(new Edge(u, w));
        }

        long[] full(int start) {
            long[] dist = new long[n + 1];
            Arrays.fill(dist, Long.MAX_VALUE);
            dist[start] = 0;

            PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
            pq.add(new long[]{start, 0});

            while (!pq.isEmpty()) {
                long[] cur = pq.poll();
                int v = (int) cur[0];
                long d = cur[1];
                if (d != dist[v]) continue;

                for (Edge e : g.get(v)) {
                    long nd = d + e.w;
                    if (nd < dist[e.to]) {
                        dist[e.to] = nd;
                        pq.add(new long[]{e.to, nd});
                    }
                }
            }
            return dist;
        }

        long toTarget(int start, int target) {
            long[] dist = new long[n + 1];
            Arrays.fill(dist, Long.MAX_VALUE);
            dist[start] = 0;

            PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
            pq.add(new long[]{start, 0});

            while (!pq.isEmpty()) {
                long[] cur = pq.poll();
                int v = (int) cur[0];
                long d = cur[1];

                if (v == target) return d;
                if (d != dist[v]) continue;

                for (Edge e : g.get(v)) {
                    long nd = d + e.w;
                    if (nd < dist[e.to]) {
                        dist[e.to] = nd;
                        pq.add(new long[]{e.to, nd});
                    }
                }
            }
            return -1;
        }
    }

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);

        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        Dijkstra d = new Dijkstra(n);

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            long w = Long.parseLong(st.nextToken());
            d.addEdge(u, v, w);
        }

        int q = Integer.parseInt(br.readLine());

        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            int s = Integer.parseInt(st.nextToken());
            int t = Integer.parseInt(st.nextToken());

            if (t == -1) {
                long[] dist = d.full(s);
                StringBuilder sb = new StringBuilder();
                for (int v = 1; v <= n; v++) {
                    if (dist[v] == Long.MAX_VALUE) sb.append("-1 ");
                    else sb.append(dist[v]).append(" ");
                }
                out.println(sb.toString().trim());
            } else {
                long ans = d.toTarget(s, t);
                out.println(ans);
            }
        }

        out.flush();
    }
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private List<String> runMainAndCaptureOutput(String input) throws Exception {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Solution.main(new String[0]);
        String output = outContent.toString().trim();
        if (output.isEmpty()) return List.of();
        return List.of(output.split(System.lineSeparator()));
    }
    @Test
    public void testProvidedExample() throws Exception {
        String input =
                "5 6\n" +
                "1 2 4\n" +
                "1 3 2\n" +
                "2 3 1\n" +
                "2 4 5\n" +
                "3 4 8\n" +
                "4 5 3\n" +
                "3\n" +
                "1 -1\n" +
                "1 5\n" +
                "5 1\n";

        List<String> expected = List.of(
                "0 3 2 8 11",
                "11",
                "-1"
        );
        assertEquals(expected, runMainAndCaptureOutput(input));
    }

    @Test
    public void testDisconnected() throws Exception {
        String input =
                "4 1\n" +
                "1 2 10\n" +
                "3\n" +
                "1 -1\n" +
                "1 3\n" +
                "3 4\n";

        List<String> expected = List.of(
                "0 10 -1 -1",
                "-1",
                "-1"
        );
        assertEquals(expected, runMainAndCaptureOutput(input));
    }

    @Test
    public void testSingleNode() throws Exception {
        String input =
                "1 0\n" +
                "1\n" +
                "1 -1\n";

        List<String> expected = List.of("0");
        assertEquals(expected, runMainAndCaptureOutput(input));
    }

    @Test
    public void testTriangleGraph() throws Exception {
        String input =
                "3 3\n" +
                "1 2 5\n" +
                "2 3 1\n" +
                "1 3 10\n" +
                "2\n" +
                "1 3\n" +
                "1 -1\n";

        List<String> expected = List.of(
                "6",
                "0 5 6"
        );
        assertEquals(expected, runMainAndCaptureOutput(input));
    }

    @Test
    public void testHeavyWeights() throws Exception {
        String input =
                "3 2\n" +
                "1 2 1000000000000\n" +
                "2 3 1000000000000\n" +
                "2\n" +
                "1 3\n" +
                "1 -1\n";

        List<String> expected = List.of(
                "2000000000000",
                "0 1000000000000 2000000000000"
        );
        assertEquals(expected, runMainAndCaptureOutput(input));
    }

    @Test
    public void testEarlyStopSimple() throws Exception {
        String input =
                "4 3\n" +
                "1 2 5\n" +
                "2 3 5\n" +
                "3 4 5\n" +
                "1\n" +
                "1 4\n";

        List<String> expected = List.of("15");
        assertEquals(expected, runMainAndCaptureOutput(input));
    }

    @Test
    public void testMultipleQueries() throws Exception {
        String input =
                "5 4\n" +
                "1 2 1\n" +
                "2 3 1\n" +
                "3 4 1\n" +
                "4 5 1\n" +
                "4\n" +
                "1 5\n" +
                "5 1\n" +
                "2 -1\n" +
                "3 1\n";

        List<String> expected = List.of(
                "4",
                "4",
                "1 0 1 2 3",
                "2"
        );
        assertEquals(expected, runMainAndCaptureOutput(input));
    }

    @Test
    public void testUnreachableTarget() throws Exception {
        String input =
                "5 2\n" +
                "1 2 7\n" +
                "4 5 9\n" +
                "2\n" +
                "1 5\n" +
                "4 -1\n";

        List<String> expected = List.of(
                "-1",
                "-1 -1 -1 0 9"
        );
        assertEquals(expected, runMainAndCaptureOutput(input));
    }

    @Test
    public void testStarTopology() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("6 5\n");
        for (int i = 2; i <= 6; i++) sb.append("1 ").append(i).append(" ").append(i).append("\n");
        sb.append("3\n1 6\n1 -1\n6 1\n");

        List<String> expected = List.of(
                "6",
                "0 2 3 4 5 6",
                "6"
        );
        assertEquals(expected, runMainAndCaptureOutput(sb.toString()));
    }

    @Test
    public void testPerformanceOnLargeInput() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("100 99\n");
        for (int i = 1; i < 100; i++) sb.append(i).append(" ").append(i + 1).append(" 1\n");
        sb.append("1\n1 100\n");

        List<String> expected = List.of("99");
        assertEquals(expected, runMainAndCaptureOutput(sb.toString()));
    }
}