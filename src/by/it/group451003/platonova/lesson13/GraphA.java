package by.it.group451003.platonova.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] vertices = edge.split(" -> ");
            String from = vertices[0].trim();
            String to = vertices[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);

            inDegree.put(to, inDegree.get(to) + 1);
        }

        List<String> result = topologicalSort(graph, inDegree);

        for (String vertex : result) {
            System.out.print(vertex + " ");
        }
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph,
                                                Map<String, Integer> inDegree) {
        List<String> result = new ArrayList<>();

        PriorityQueue<String> queue = new PriorityQueue<>();

        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            if (graph.containsKey(current)) {
                for (String neighbor : graph.get(current)) {
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
