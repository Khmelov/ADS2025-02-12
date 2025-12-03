package by.it.group451003.burshtyn.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = parseGraph(input);

        boolean hasCycle = hasCycle(graph);

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();

        if (input.isEmpty()) {
            return graph;
        }

        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String vertex = parts[0].trim();

            graph.putIfAbsent(vertex, new ArrayList<>());

            if (parts.length > 1) {
                String[] adjacentVertices = parts[1].split(", ");
                for (String adj : adjacentVertices) {
                    String adjacent = adj.trim();
                    graph.get(vertex).add(adjacent);
                    graph.putIfAbsent(adjacent, new ArrayList<>());
                }
            }
        }

        return graph;
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Map<String, Integer> visited = new HashMap<>();

        for (String vertex : graph.keySet()) {
            visited.put(vertex, 0);
        }

        for (String vertex : graph.keySet()) {
            if (visited.get(vertex) == 0) {
                if (dfsHasCycle(vertex, graph, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean dfsHasCycle(String current, Map<String, List<String>> graph,
                                       Map<String, Integer> visited) {
        visited.put(current, 1);

        for (String neighbor : graph.get(current)) {
            if (visited.get(neighbor) == 0) {
                if (dfsHasCycle(neighbor, graph, visited)) {
                    return true;
                }
            } else if (visited.get(neighbor) == 1) {
                return true;
            }
        }

        visited.put(current, 2);
        return false;
    }
}