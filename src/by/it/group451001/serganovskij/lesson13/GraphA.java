package by.it.group451001.serganovskij.lesson13;

import java.util.*;

public class GraphA {
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

        List<String> result = topologicalSort(graph, vertices);

        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
        System.out.println();
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph, Set<String> vertices) {
        Map<String, Integer> inDegree = new HashMap<>();

        for (String vertex : vertices) {
            inDegree.put(vertex, 0);
        }

        for (List<String> neighbors : graph.values()) {
            for (String neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String vertex : vertices) {
            if (inDegree.get(vertex) == 0) {
                queue.offer(vertex);
            }
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);

            if (graph.containsKey(vertex)) {
                List<String> neighbors = new ArrayList<>(graph.get(vertex));
                Collections.sort(neighbors);

                for (String neighbor : neighbors) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return result;
    }
}