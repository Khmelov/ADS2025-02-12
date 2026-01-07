package by.it.group451003.fedorcov.lesson13;

import java.util.*;

public class GraphB {

    private static class Graph {
        private final Map<String, List<String>> adjacencyList = new HashMap<>();
        private final Set<String> vertices = new HashSet<>();

        public void addEdge(String from, String to) {
            adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            vertices.add(from);
            vertices.add(to);
        }

        private boolean hasCycleUtil(String vertex, Set<String> visited, Set<String> recursionStack) {
            visited.add(vertex);
            recursionStack.add(vertex);

            if (adjacencyList.containsKey(vertex)) {
                for (String neighbor : adjacencyList.get(vertex)) {
                    if (!visited.contains(neighbor)) {
                        if (hasCycleUtil(neighbor, visited, recursionStack)) {
                            return true;
                        }
                    } else if (recursionStack.contains(neighbor)) {
                        return true;
                    }
                }
            }

            recursionStack.remove(vertex);
            return false;
        }

        public boolean hasCycle() {
            Set<String> visited = new HashSet<>();
            Set<String> recursionStack = new HashSet<>();

            for (String vertex : vertices) {
                if (!visited.contains(vertex)) {
                    if (hasCycleUtil(vertex, visited, recursionStack)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Graph graph = new Graph();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.addEdge(from, to);
        }

        if (graph.hasCycle()) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }
}