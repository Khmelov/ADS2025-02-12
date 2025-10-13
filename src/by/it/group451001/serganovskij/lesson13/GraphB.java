package by.it.group451001.serganovskij.lesson13;
import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("\\s*->\\s*");

            if (parts.length < 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        // Добавляем изолированные вершины
        for (String vertex : vertices) {
            graph.putIfAbsent(vertex, new ArrayList<>());
        }

        boolean hasCycle = hasCycle(graph, vertices);
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> vertices) {
        Map<String, Integer> visited = new HashMap<>();
        for (String vertex : vertices) {
            visited.put(vertex, 0);
        }

        for (String vertex : vertices) {
            if (visited.get(vertex) == 0) {
                if (dfsHasCycle(vertex, graph, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean dfsHasCycle(String vertex, Map<String, List<String>> graph, Map<String, Integer> visited) {
        visited.put(vertex, 1);

        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (visited.get(neighbor) == 0) {
                    if (dfsHasCycle(neighbor, graph, visited)) {
                        return true;
                    }
                } else if (visited.get(neighbor) == 1) {
                    return true;
                }
            }
        }

        visited.put(vertex, 2);
        return false;
    }
}