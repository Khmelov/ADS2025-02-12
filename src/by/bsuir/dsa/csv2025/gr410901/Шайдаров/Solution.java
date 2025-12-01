package by.bsuir.dsa.csv2025.gr410901.Шайдаров;


import java.util.*;
import org.junit.*;
import java.io.*;
import static org.junit.Assert.assertEquals;

public class Solution {

    static class Edge {
        int to, weight;

        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    static class Node implements Comparable<Node> {
        int vertex, distance;

        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    public static int dijkstra(List<List<Edge>> graph, int start, int end, int n) {
        int[] distances = new int[n + 1];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[start] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(start, 0));

        boolean[] visited = new boolean[n + 1];

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex;

            if (visited[u]) continue;
            visited[u] = true;

            if (u == end) {
                return distances[end];
            }

            for (Edge edge : graph.get(u)) {
                int v = edge.to;
                int newDist = distances[u] + edge.weight;

                if (newDist < distances[v]) {
                    distances[v] = newDist;
                    pq.offer(new Node(v, newDist));
                }
            }
        }

        return distances[end] == Integer.MAX_VALUE ? -1 : distances[end];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int m = scanner.nextInt();

        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();

            graph.get(u).add(new Edge(v, w));
            graph.get(v).add(new Edge(u, w));
        }

        int start = scanner.nextInt();
        int end = scanner.nextInt();

        int result = dijkstra(graph, start, end, n);
        System.out.println(result);

        scanner.close();
    }

    // --------------------------- JUnit TESTS ---------------------------

    @Test
    public void test1() {
        String input = "4 5\n1 2 10\n1 3 20\n2 4 50\n3 4 20\n2 3 10\n1 4";
        String expected = "40";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test2() {
        String input = "3 2\n1 2 10\n2 3 20\n1 3";
        String expected = "30";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test3() {
        String input = "4 2\n1 2 10\n3 4 10\n1 4";
        String expected = "-1";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test4() {
        String input = "1 0\n1 1";
        String expected = "0";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test5() {
        String input = "5 8\n1 2 7\n1 3 9\n1 5 14\n2 3 10\n2 4 15\n3 4 11\n3 5 2\n4 5 6\n1 5";
        String expected = "11";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test6() {
        String input = "2 1\n1 2 15\n1 2";
        String expected = "15";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test7() {
        String input = "4 5\n1 2 5\n2 3 5\n3 4 5\n4 1 5\n1 3 20\n1 3";
        String expected = "10";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test8() {
        String input = "10 9\n1 2 3\n2 3 3\n3 4 3\n4 5 3\n5 6 3\n6 7 3\n7 8 3\n8 9 3\n9 10 3\n1 10";
        String expected = "27";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test9() {
        String input = "5 5\n1 2 1\n2 3 1\n3 4 1\n4 5 1\n1 5 10\n1 5";
        String expected = "4";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }

    @Test
    public void test10() {
        String input = "6 5\n1 2 5\n1 3 10\n1 4 15\n1 5 20\n1 6 25\n2 6";
        String expected = "30";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});

        assertEquals(expected, out.toString().replace("\r\n", "\n").trim());
    }
}
