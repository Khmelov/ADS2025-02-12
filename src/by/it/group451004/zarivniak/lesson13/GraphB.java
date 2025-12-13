package by.it.group451004.zarivniak.lesson13;

import java.util.Scanner;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        Map<String, List<String>> graph = parseGraph(input);
        boolean hasCycle = hasCycle(graph);
        System.out.println(hasCycle ? "yes" : "no");
        scanner.close();
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();

        input = input.replace(", ", ",").replace(" ,", ",");
        String[] edges = input.split(",");

        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            if (!graph.containsKey(to)) {
                graph.put(to, new ArrayList<>());
            }
        }

        return graph;
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                if (hasCycleDFS(vertex, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean hasCycleDFS(String vertex, Map<String, List<String>> graph,
                                       Set<String> visited, Set<String> recursionStack) {
        visited.add(vertex);
        recursionStack.add(vertex);
        for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (hasCycleDFS(neighbor, graph, visited, recursionStack)) {
                    return true;
                }
            }
            else if (recursionStack.contains(neighbor)) {
                return true;
            }
        }
        recursionStack.remove(vertex);
        return false;
    }
}
