package by.bsuir.dsa.csv2025.gr410902.Скобялко;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

public class Solution {

    @Test
    public void test1_SimpleGraphWithTwoPaths() {
        String input = "3 3 0 2\n0 1 10 1\n1 2 10 1\n0 2 10 2";
        int[] result = runMinCostMaxFlow(input);
        assertArrayEquals("Test 1 Failed", new int[]{20, 40}, result);
    }

    @Test
    public void test2_SinglePath() {
        String input = "3 2 0 2\n0 1 10 2\n1 2 10 3";
        int[] result = runMinCostMaxFlow(input);
        assertArrayEquals("Test 2 Failed", new int[]{10, 50}, result);
    }

    @Test
    public void test3_TwoPathsSameCost() {
        String input = "4 4 0 3\n0 1 10 1\n0 2 10 1\n1 3 10 1\n2 3 10 1";
        int[] result = runMinCostMaxFlow(input);
        assertArrayEquals("Test 3 Failed", new int[]{20, 40}, result);
    }

    @Test
    public void test4_GraphWithThreePaths() {
        String input = "5 6 0 4\n0 1 10 1\n0 2 10 2\n1 3 10 1\n2 3 10 1\n3 4 10 1\n0 4 5 10";
        int[] result = runMinCostMaxFlow(input);
        assertArrayEquals("Test 4 Failed", new int[]{15, 80}, result);
    }

    @Test
    public void test5_GraphWithBottleneck() {
        String input = "4 4 0 3\n0 1 15 1\n1 2 10 2\n2 3 15 1\n0 3 5 10";
        int[] result = runMinCostMaxFlow(input);
        assertArrayEquals("Test 5 Failed", new int[]{15, 90}, result);
    }

    @Test
    public void test6_GraphWithParallelEdges() {
        String input = "3 4 0 2\n0 1 5 1\n0 1 10 2\n1 2 15 3\n1 2 5 2";
        int[] result = runMinCostMaxFlow(input);
        assertArrayEquals("Test 6 Failed", new int[]{15, 65}, result);
    }

    @Test
    public void test7_GraphWithCycle() {
        String input = "5 7 0 4\n0 1 10 1\n1 2 5 2\n2 3 5 1\n3 1 5 1\n1 4 10 3\n2 4 5 2\n3 4 5 1";
        int[] result = runMinCostMaxFlow(input);
        assertArrayEquals("Test 7 Failed", new int[]{10, 40}, result);
    }

    @Test
    public void test8_SimpleGraphWithDifferentCosts() {
        String input = "4 4 0 3\n0 1 10 1\n0 2 5 10\n1 3 5 1\n2 3 10 1";
        int[] result = runMinCostMaxFlow(input);
        assertArrayEquals("Test 8 Failed", new int[]{10, 65}, result);
    }

    @Test
    public void test9_VerySimpleGraph() {
        String input = "3 2 0 2\n0 1 100 1\n1 2 100 1";
        int[] result = runMinCostMaxFlow(input);
        assertArrayEquals("Test 9 Failed", new int[]{100, 200}, result);
    }

    @Test
    public void test10_ComplexGraph() {
        String input = "4 5 0 3\n0 1 10 1\n0 2 5 2\n1 2 15 1\n1 3 10 2\n2 3 10 1";
        int[] result = runMinCostMaxFlow(input);
        assertArrayEquals("Test 10 Failed", new int[]{15, 45}, result);
    }

    private int[] runMinCostMaxFlow(String input) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            // Redirect System.in to read from our input string
            ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            System.setIn(inputStream);

            // Redirect System.out to capture output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            System.setOut(printStream);

            // Run the main method
            MinCostMaxFlow.main(new String[0]);

            // Get the output
            String output = outputStream.toString().trim();
            String[] parts = output.split(" ");

            return new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};

        } catch (Exception e) {
            fail("Exception during test execution: " + e.getMessage());
            return new int[]{0, 0};
        } finally {
            // Restore original System.in and System.out
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(Solution.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        if (result.wasSuccessful()) {
            System.out.println("All tests passed!");
        } else {
            System.out.println("Some tests failed!");
        }
    }
}

class MinCostMaxFlow {
    private static class Edge {
        int to, capacity, cost, flow, reverseIndex;

        Edge(int to, int capacity, int cost, int reverseIndex) {
            this.to = to;
            this.capacity = capacity;
            this.cost = cost;
            this.flow = 0;
            this.reverseIndex = reverseIndex;
        }
    }

    private List<Edge>[] graph;
    private int n;
    private int[] dist, parent, parentEdgeIndex;
    private boolean[] inQueue;

    @SuppressWarnings("unchecked")
    public MinCostMaxFlow(int vertices) {
        this.n = vertices;
        this.graph = new ArrayList[vertices];
        for (int i = 0; i < vertices; i++) {
            graph[i] = new ArrayList<>();
        }
        this.dist = new int[vertices];
        this.parent = new int[vertices];
        this.parentEdgeIndex = new int[vertices];
        this.inQueue = new boolean[vertices];
    }

    public void addEdge(int from, int to, int capacity, int cost) {
        int forwardIndex = graph[from].size();
        int reverseIndex = graph[to].size();

        graph[from].add(new Edge(to, capacity, cost, reverseIndex));
        graph[to].add(new Edge(from, 0, -cost, forwardIndex));
    }

    private boolean bellmanFord(int source, int sink) {
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(inQueue, false);

        Queue<Integer> queue = new LinkedList<>();
        dist[source] = 0;
        queue.add(source);
        inQueue[source] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            inQueue[u] = false;

            for (int i = 0; i < graph[u].size(); i++) {
                Edge edge = graph[u].get(i);

                if (edge.flow < edge.capacity && dist[u] != Integer.MAX_VALUE &&
                        dist[u] + edge.cost < dist[edge.to]) {

                    dist[edge.to] = dist[u] + edge.cost;
                    parent[edge.to] = u;
                    parentEdgeIndex[edge.to] = i;

                    if (!inQueue[edge.to]) {
                        queue.add(edge.to);
                        inQueue[edge.to] = true;
                    }
                }
            }
        }

        return dist[sink] != Integer.MAX_VALUE;
    }

    public int[] findMinCostMaxFlow(int source, int sink) {
        int totalFlow = 0;
        int totalCost = 0;

        while (bellmanFord(source, sink)) {
            int flow = Integer.MAX_VALUE;

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                int edgeIndex = parentEdgeIndex[v];
                Edge edge = graph[u].get(edgeIndex);
                flow = Math.min(flow, edge.capacity - edge.flow);
            }

            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                int edgeIndex = parentEdgeIndex[v];
                Edge edge = graph[u].get(edgeIndex);
                Edge reverseEdge = graph[v].get(edge.reverseIndex);

                edge.flow += flow;
                reverseEdge.flow -= flow;
                totalCost += flow * edge.cost;
            }

            totalFlow += flow;
        }

        return new int[]{totalFlow, totalCost};
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int source = scanner.nextInt();
        int sink = scanner.nextInt();

        MinCostMaxFlow flow = new MinCostMaxFlow(n);

        for (int i = 0; i < m; i++) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            int capacity = scanner.nextInt();
            int cost = scanner.nextInt();
            flow.addEdge(from, to, capacity, cost);
        }

        int[] result = flow.findMinCostMaxFlow(source, sink);
        System.out.println(result[0] + " " + result[1]);

        scanner.close();
    }
}