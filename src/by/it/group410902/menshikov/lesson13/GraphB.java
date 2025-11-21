package by.it.group410902.menshikov.lesson13;

import java.util.*;

public class GraphB {

    private static final int WHITE = 0;
    private static final int GRAY = 1;
    private static final int BLACK = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();

        String[] edges = input.split(",");

        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.putIfAbsent(from, new ArrayList<>());
                graph.get(from).add(to);

                graph.putIfAbsent(to, new ArrayList<>());
            }
        }
        boolean hasCycle = detectCycle(graph);
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean detectCycle(Map<String, List<String>> graph) {
        Map<String, Integer> colors = new HashMap<>();

        for (String vertex : graph.keySet()) colors.put(vertex, WHITE);

        for (String vertex : graph.keySet()) {
            if (colors.get(vertex) == WHITE) {
                if (dfs(vertex, graph, colors)) return true;
            }
        }
        return false;
    }

    private static boolean dfs(String vertex, Map<String, List<String>> graph, Map<String, Integer> colors) {
        colors.put(vertex, GRAY);

        List<String> neighbors = graph.get(vertex);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (colors.get(neighbor) == GRAY) return true;

                if (colors.get(neighbor) == WHITE) {
                    if (dfs(neighbor, graph, colors)) return true;
                }
            }
        }
        colors.put(vertex, BLACK);
        return false;
    }
}