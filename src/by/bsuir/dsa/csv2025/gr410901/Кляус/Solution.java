package by.bsuir.dsa.csv2025.gr410901.Кляус;

import java.util.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import static org.junit.Assert.*;

public class Solution {

    static class Edge {
        String to;
        int weight;

        Edge(String to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    static class State implements Comparable<State> {
        String node;
        int dist;
        int removals;

        State(String node, int dist, int removals) {
            this.node = node;
            this.dist = dist;
            this.removals = removals;
        }

        @Override
        public int compareTo(State other) {
            if (this.dist != other.dist) {
                return Integer.compare(this.dist, other.dist);
            }
            return Integer.compare(this.removals, other.removals);
        }
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("test")) {
            Result result = JUnitCore.runClasses(Solution.class);

            System.out.println("Всего тестов: " + result.getRunCount());
            System.out.println("Успешных: " + (result.getRunCount() - result.getFailureCount()));
            System.out.println("Провалено: " + result.getFailureCount());

            if (result.getFailureCount() > 0) {
                for (Failure failure : result.getFailures()) {
                    System.out.println(failure.toString());
                }
            }

            return;
        }

        Scanner scanner = new Scanner(System.in);
        String graphInput = scanner.nextLine();
        String start = scanner.nextLine().trim();
        String end = scanner.nextLine().trim();
        int k = Integer.parseInt(scanner.nextLine().trim());

        System.out.println(solve(graphInput, start, end, k));
    }

    public static int solve(String graphInput, String start, String end, int k) {
        Map<String, List<Edge>> graph = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        String[] edges = graphInput.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            int arrowPos = edge.indexOf("->");
            int bracketPos = edge.indexOf("(");

            String from = edge.substring(0, arrowPos).trim();
            String to = edge.substring(arrowPos + 2, bracketPos).trim();
            int weight = Integer.parseInt(edge.substring(bracketPos + 1, edge.length() - 1));

            allNodes.add(from);
            allNodes.add(to);
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(new Edge(to, weight));
        }

        for (String node : allNodes) {
            graph.putIfAbsent(node, new ArrayList<>());
        }

        if (!allNodes.contains(start) || !allNodes.contains(end)) {
            return -1;
        }

        Map<String, int[]> distances = new HashMap<>();
        for (String node : allNodes) {
            distances.put(node, new int[k + 1]);
            Arrays.fill(distances.get(node), Integer.MAX_VALUE);
        }
        distances.get(start)[0] = 0;

        PriorityQueue<State> pq = new PriorityQueue<>();
        pq.offer(new State(start, 0, 0));

        while (!pq.isEmpty()) {
            State current = pq.poll();

            if (current.dist > distances.get(current.node)[current.removals]) {
                continue;
            }

            for (Edge edge : graph.get(current.node)) {
                int newDist1 = current.dist + edge.weight;
                if (newDist1 < distances.get(edge.to)[current.removals]) {
                    distances.get(edge.to)[current.removals] = newDist1;
                    pq.offer(new State(edge.to, newDist1, current.removals));
                }

                if (current.removals < k) {
                    int newDist2 = current.dist;
                    if (newDist2 < distances.get(edge.to)[current.removals + 1]) {
                        distances.get(edge.to)[current.removals + 1] = newDist2;
                        pq.offer(new State(edge.to, newDist2, current.removals + 1));
                    }
                }
            }
        }

        int result = Integer.MAX_VALUE;
        for (int i = 0; i <= k; i++) {
            result = Math.min(result, distances.get(end)[i]);
        }

        return result == Integer.MAX_VALUE ? -1 : result;
    }

    @Test
    public void test1() {
        int result = solve("A->B(5), B->C(3), A->C(10)", "A", "C", 0);
        assertEquals(8, result);
    }

    @Test
    public void test2() {
        int result = solve("A->B(5), B->C(3), A->C(10)", "A", "C", 1);
        assertEquals(0, result);
    }

    @Test
    public void test3() {
        int result = solve("A->B(5), B->C(3), C->D(7), A->D(20)", "A", "D", 2);
        assertEquals(0, result);
    }

    @Test
    public void test4() {
        int result = solve("A->B(10), B->C(10), C->D(10), D->E(10)", "A", "E", 3);
        assertEquals(10, result);
    }

    @Test
    public void test5() {
        int result = solve("A->B(5), C->D(3)", "A", "D", 5);
        assertEquals(-1, result);
    }

    @Test
    public void test6() {
        int result = solve("A->B(1), B->C(1), C->D(1), A->D(5)", "A", "D", 0);
        assertEquals(3, result);
    }

    @Test
    public void test7() {
        int result = solve("A->B(100), B->C(1), A->C(50)", "A", "C", 1);
        assertEquals(0, result);
    }

    @Test
    public void test8() {
        int result = solve("A->B(5)", "A", "A", 0);
        assertEquals(0, result);
    }

    @Test
    public void test9() {
        int result = solve("A->B(100), B->C(100), C->D(100)", "A", "D", 3);
        assertEquals(0, result);
    }

    @Test
    public void test10() {
        int result = solve("A->B(10), B->C(5), A->C(20), C->D(3), B->D(15)", "A", "D", 1);
        assertEquals(3, result);
    }
}
