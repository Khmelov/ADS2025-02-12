package by.bsuir.dsa.csv2025.gr410902.Гавлев;

import org.junit.Test;


import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class Solution {
    static class Edge {
        int to;
        int w;
        Edge(int to, int w) { this.to = to; this.w = w; }
    }

    public static void main(String[] args) throws Exception {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        FastScanner fs = new FastScanner(System.in);

        Integer nI = fs.nextInt();
        Integer mI = fs.nextInt();
        Integer sI = fs.nextInt();
        Integer tI = fs.nextInt();

        if (nI == null || mI == null || sI == null || tI == null) {
            return;
        }

        int n = nI, m = mI, s = sI - 1, t = tI - 1;

        String[] names = new String[n];
        for (int i = 0; i < n; i++) {
            String name = fs.nextToken();
            Integer idx = fs.nextInt();
            if (name == null || idx == null) {
                return;
            }
            names[idx - 1] = name;
        }

        List<List<Edge>> g = new ArrayList<>(n);
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());

        for (int i = 0; i < m; i++) {
            Integer uI = fs.nextInt();
            Integer vI = fs.nextInt();
            Integer wI = fs.nextInt();
            if (uI == null || vI == null || wI == null) {
                return; // некорректный ввод
            }
            int u = uI - 1, v = vI - 1, w = wI;
            g.get(u).add(new Edge(v, w));
            g.get(v).add(new Edge(u, w));
        }

        long threshold = (long) n * (n - 1) / 4;
        Result res = (m > threshold) ? dijkstraDense(g, n, s, t) : dijkstraSparse(g, n, s, t);

        if (res.dist == INF) {
            out.println("Маршрут отсутствует");
            out.println(0);
            return;
        }

        List<Integer> path = rebuildPath(res.parent, s, t);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) sb.append(" -> ");
            sb.append(names[path.get(i)]);
        }
        out.println(sb);
        out.println(res.dist);
    }

    static final int INF = 1_000_000_000;

    static class Result {
        int dist;
        int[] parent;
        Result(int dist, int[] parent) { this.dist = dist; this.parent = parent; }
    }

    static Result dijkstraDense(List<List<Edge>> g, int n, int s, int t) {
        int[] dist = new int[n];
        boolean[] used = new boolean[n];
        int[] parent = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);
        dist[s] = 0;

        for (int i = 0; i < n; i++) {
            int v = -1;
            for (int u = 0; u < n; u++) {
                if (!used[u] && (v == -1 || dist[u] < dist[v])) v = u;
            }
            if (v == -1 || dist[v] == INF) break;
            used[v] = true;
            if (v == t) break;

            for (Edge e : g.get(v)) {
                int u = e.to, w = e.w;
                if (dist[u] > dist[v] + w) {
                    dist[u] = dist[v] + w;
                    parent[u] = v;
                }
            }
        }
        return new Result(dist[t], parent);
    }

    static Result dijkstraSparse(List<List<Edge>> g, int n, int s, int t) {
        int[] dist = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);
        dist[s] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.d));
        pq.offer(new Node(s, 0));

        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            int v = cur.v;
            if (cur.d != dist[v]) continue;
            if (v == t) break;

            for (Edge e : g.get(v)) {
                int u = e.to, w = e.w;
                if (dist[u] > dist[v] + w) {
                    dist[u] = dist[v] + w;
                    parent[u] = v;
                    pq.offer(new Node(u, dist[u]));
                }
            }
        }
        return new Result(dist[t], parent);
    }

    static class Node {
        int v, d;
        Node(int v, int d) { this.v = v; this.d = d; }
    }

    static List<Integer> rebuildPath(int[] parent, int s, int t) {
        List<Integer> rev = new ArrayList<>();
        int cur = t;
        while (cur != -1) {
            rev.add(cur);
            if (cur == s) break;
            cur = parent[cur];
        }
        Collections.reverse(rev);
        if (rev.isEmpty() || rev.get(0) != s) return Collections.emptyList();
        return rev;
    }

    static class FastScanner {
        BufferedInputStream in;
        byte[] buffer = new byte[1 << 16];
        int ptr = 0, len = 0;
        FastScanner(InputStream is) { in = new BufferedInputStream(is); }

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        String nextToken() throws IOException {
            StringBuilder sb = new StringBuilder();
            int c;
            while (true) {
                c = read();
                if (c == -1) return null;
                if (!Character.isWhitespace(c)) break;
            }
            while (c != -1 && !Character.isWhitespace(c)) {
                sb.append((char) c);
                c = read();
            }
            return sb.toString();
        }

        Integer nextInt() throws IOException {
            String tok = nextToken();
            if (tok == null) return null;
            return Integer.parseInt(tok);
        }
    }
    private String runProgram(String input) throws Exception {
        InputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bout, true, StandardCharsets.UTF_8);

        System.setIn(in);
        System.setOut(out);

        Solution.main(new String[0]);

        return bout.toString(StandardCharsets.UTF_8).trim();
    }

    private void check(String input, String expectedOutput) throws Exception {
        String actual = runProgram(input);
        actual = actual.replace("\r\n", "\n").trim();
        expectedOutput = expectedOutput.replace("\r\n", "\n").trim();
        assertEquals(expectedOutput, actual);
    }


    @Test
    public void testExample1() throws Exception {
        String input = """
            6 7 1 6
            Station 1
            Victory_Square 2
            Nemiga 3
            Academy_of_Sciences 4
            Stone_Hill 5
            Chizhovka 6
            1 2 4
            1 3 2
            2 3 1
            2 4 7
            3 5 3
            4 6 1
            5 6 5
            """;
        String expected = """
            Station -> Nemiga -> Stone_Hill -> Chizhovka
            10
            """;
        check(input, expected);
    }

    @Test
    public void testExample2() throws Exception {
        String input = """
            9 22 4 9
            Station 1
            Victory_Square 2
            Nemiga 3
            Academy_of_Sciences 4
            Stone_Hill 5
            Chizhovka 6
            Uruchye 7
            Malinovka 8
            Serebryanka 9
            1 2 3
            1 3 4
            1 4 6
            1 5 7
            1 6 8
            1 7 9
            1 8 5
            1 9 12
            2 3 2
            2 4 5
            2 5 4
            2 6 6
            2 7 7
            2 8 3
            3 4 1
            3 5 2
            3 6 5
            3 8 2
            4 5 3
            4 6 4
            5 7 3
            6 9 3
            8 9 4
            """;
        String expected = """
            Academy_of_Sciences -> Chizhovka -> Serebryanka
            7
            """;
        check(input, expected);
    }

    @Test
    public void testExample3() throws Exception {
        String input = """
            5 5 1 5
            Station 1
            Victory_Square 2
            Nemiga 3
            Academy_of_Sciences 4
            Stone_Hill 5
            1 2 3
            2 3 4
            3 5 2
            1 4 10
            4 5 3
            """;
        String expected = """
            Station -> Victory_Square -> Nemiga -> Stone_Hill
            9
            """;
        check(input, expected);
    }

    @Test
    public void testExample4() throws Exception {
        String input = """
            8 9 8 5
            Station 1
            Victory_Square 2
            Nemiga 3
            Academy_of_Sciences 4
            Stone_Hill 5
            Chizhovka 6
            Uruchye 7
            Malinovka 8
            8 7 2
            7 6 2
            6 5 2
            8 1 10
            1 2 3
            2 3 3
            3 4 3
            4 5 10
            2 6 5
            """;
        String expected = """
            Malinovka -> Uruchye -> Chizhovka -> Stone_Hill
            6
            """;
        check(input, expected);
    }

    @Test
    public void testExample5() throws Exception {
        String input = """
            9 22 1 9
            Station 1
            Victory_Square 2
            Nemiga 3
            Academy_of_Sciences 4
            Stone_Hill 5
            Chizhovka 6
            Uruchye 7
            Malinovka 8
            Serebryanka 9
            1 3 1
            3 5 1
            5 2 1
            2 4 1
            4 7 1
            7 6 1
            6 8 1
            8 9 1
            1 2 4
            1 4 5
            1 5 6
            2 3 4
            2 6 5
            3 4 3
            3 6 4
            3 8 10
            4 5 3
            4 8 4
            5 7 3
            5 8 4
            6 7 2
            6 9 4
            7 9 4
            8 1 7
            """;
        String expected = """
            Station -> Nemiga -> Stone_Hill -> Malinovka -> Serebryanka
            7
            """;
        check(input, expected);
    }
}