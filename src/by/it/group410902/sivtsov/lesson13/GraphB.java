package by.it.group410902.sivtsov.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<Integer, List<Integer>> graph = parseGraph(input);

        boolean hasCycle = hasCycle(graph);

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static Map<Integer, List<Integer>> parseGraph(String input) {
        Map<Integer, List<Integer>> graph = new HashMap<>();

        if (input == null || input.trim().isEmpty()) {
            return graph;
        }

        String[] edges = input.split(",");

        for (String edge : edges) {

            String cleanedEdge = edge.trim();
            String[] parts = cleanedEdge.split("->");

            if (parts.length == 2) {
                try {
                    int from = Integer.parseInt(parts[0].trim());
                    int to = Integer.parseInt(parts[1].trim());

                    graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

                    graph.putIfAbsent(to, new ArrayList<>());
                } catch (NumberFormatException e) {

                }
            }
        }

        return graph;
    }

    private static boolean hasCycle(Map<Integer, List<Integer>> graph) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recursionStack = new HashSet<>();

        for (Integer node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (dfsHasCycle(node, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean dfsHasCycle(Integer node, Map<Integer, List<Integer>> graph, Set<Integer> visited, Set<Integer> recursionStack) {

        visited.add(node);
        recursionStack.add(node);

        for (Integer neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (dfsHasCycle(neighbor, graph, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {

                return true;
            }
        }

        recursionStack.remove(node);
        return false;
    }
}
