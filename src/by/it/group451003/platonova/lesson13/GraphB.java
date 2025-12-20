package by.it.group451003.platonova.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] vertices = edge.split(" -> ");
            String from = vertices[0].trim();
            String to = vertices[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            allVertices.add(from);
            allVertices.add(to);
        }

        boolean hasCycle = hasCycle(graph, allVertices);

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> allVertices) {
        Map<String, Integer> visited = new HashMap<>(); // 0 - не посещена, 1 - в процессе, 2 - обработана

        for (String vertex : allVertices) {
            visited.put(vertex, 0);
        }

        for (String vertex : allVertices) {
            if (visited.get(vertex) == 0) {
                if (dfs(vertex, graph, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean dfs(String current, Map<String, List<String>> graph,
                               Map<String, Integer> visited) {
        visited.put(current, 1);

        if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                if (visited.get(neighbor) == 0) {
                    if (dfs(neighbor, graph, visited)) {
                        return true;
                    }
                } else if (visited.get(neighbor) == 1) {
                    return true;
                }
            }
        }

        visited.put(current, 2);
        return false;
    }
}
