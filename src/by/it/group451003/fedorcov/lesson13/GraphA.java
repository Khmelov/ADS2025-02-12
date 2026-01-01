package by.it.group451003.fedorcov.lesson13;

import java.util.*;

public class GraphA {

    private static class Graph {
        private final Map<String, List<String>> adjacencyList = new HashMap<>();
        private final Set<String> vertices = new TreeSet<>();

        public void addEdge(String from, String to) {
            adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            vertices.add(from);
            vertices.add(to);
        }

        private void topologicalSortUtil(String vertex, Set<String> visited, Stack<String> stack) {
            visited.add(vertex);

            List<String> neighbors = adjacencyList.getOrDefault(vertex, new ArrayList<>());
            neighbors.sort(Collections.reverseOrder());
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    topologicalSortUtil(neighbor, visited, stack);
                }
            }
            stack.push(vertex);
        }

        public List<String> topologicalSort() {
            Stack<String> stack = new Stack<>();
            Set<String> visited = new HashSet<>();

            List<String> sortedVertices = new ArrayList<>(vertices);
            sortedVertices.sort(Collections.reverseOrder());

            for (String vertex : sortedVertices) {
                if (!visited.contains(vertex)) {
                    topologicalSortUtil(vertex, visited, stack);
                }
            }

            List<String> result = new ArrayList<>();
            while (!stack.isEmpty()) {
                result.add(stack.pop());
            }
            return result;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Graph graph = new Graph();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();
                graph.addEdge(from, to);
            }
        }

        List<String> sorted = graph.topologicalSort();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sorted.size(); i++) {
            result.append(sorted.get(i));
            if (i < sorted.size() - 1) {
                result.append(" ");
            }
        }
        System.out.println(result);
    }
}