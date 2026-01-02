package by.it.group451003.burshtyn.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Map<String, List<String>> graph = parseGraph(input);

        List<String> result = topologicalSort(graph);

        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
        System.out.println();
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();

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

    private static List<String> topologicalSort(Map<String, List<String>> graph) {
        List<String> result = new ArrayList<>();

        Map<String, Integer> inDegree = new HashMap<>();

        for (String vertex : graph.keySet()) {
            inDegree.put(vertex, 0);
        }

        for (String vertex : graph.keySet()) {
            for (String neighbor : graph.get(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        PriorityQueue<String> queue = new PriorityQueue<>();

        for (String vertex : inDegree.keySet()) {
            if (inDegree.get(vertex) == 0) {
                queue.offer(vertex);
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            for (String neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        return result;
    }
}