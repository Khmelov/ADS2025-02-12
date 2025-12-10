package by.it.group451003.qtwix.lesson01.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Map<String, List<String>> graph = parseGraph(input);
        boolean hasCycle = hasCycle(graph);
        System.out.println(hasCycle ? "yes" : "no");
        scanner.close();
    }//

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] vertices = edge.split("\\s*->\\s*");
            String from = vertices[0].trim();
            String to = vertices[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }
        return graph;
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        for (String vertex : graph.keySet()) {
            if (hasCycleUtil(vertex, graph, visited, recStack)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasCycleUtil(String vertex, Map<String, List<String>> graph,
                                        Set<String> visited, Set<String> recStack) {
        if (recStack.contains(vertex)) {
            return true;
        }
        if (visited.contains(vertex)) {
            return false;
        }

        visited.add(vertex);
        recStack.add(vertex);

        List<String> neighbors = graph.getOrDefault(vertex, new ArrayList<>());
        for (String neighbor : neighbors) {
            if (hasCycleUtil(neighbor, graph, visited, recStack)) {
                return true;
            }
        }

        recStack.remove(vertex);
        return false;
    }
}