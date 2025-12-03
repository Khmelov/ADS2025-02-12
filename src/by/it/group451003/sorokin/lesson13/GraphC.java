package by.it.group451003.sorokin.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0];
            String to = parts[1];

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            allVertices.add(from);
            allVertices.add(to);
        }

        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                fillOrder(graph, vertex, visited, stack);
            }
        }

        visited.clear();
        List<List<String>> sccs = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> scc = new ArrayList<>();
                dfs(reverseGraph, vertex, visited, scc);
                Collections.sort(scc);
                sccs.add(scc);
            }
        }

        for (List<String> scc : sccs) {
            for (String vertex : scc) {
                System.out.print(vertex);
            }
            System.out.println();
        }
    }

    private static void fillOrder(Map<String, List<String>> graph, String vertex,
                                  Set<String> visited, Stack<String> stack) {
        visited.add(vertex);
        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    fillOrder(graph, neighbor, visited, stack);
                }
            }
        }
        stack.push(vertex);
    }

    private static void dfs(Map<String, List<String>> graph, String vertex,
                            Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);
        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfs(graph, neighbor, visited, component);
                }
            }
        }
    }
}
