package by.it.group451001.klevko.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new TreeSet<>();

        if (!input.trim().isEmpty()) {
            String[] edges = input.split(",");
            for (String edge : edges) {
                edge = edge.trim();
                String[] parts = edge.split("->");
                String from = parts[0].trim();
                String to = parts[1].trim();

                allVertices.add(from);
                allVertices.add(to);

                graph.putIfAbsent(from, new ArrayList<>());
                graph.get(from).add(to);
            }
        }

        Map<String, Integer> inDegree = new HashMap<>();

        for (String vertex : allVertices) inDegree.put(vertex, 0);

        for (String vertex : graph.keySet()) {
            for (String neighbor : graph.get(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        PriorityQueue<String> queue = new PriorityQueue<>();

        for (String vertex : allVertices) {
            if (inDegree.get(vertex) == 0) queue.offer(vertex);
        }

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            if (graph.containsKey(current)) {
                for (String neighbor : graph.get(current)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) queue.offer(neighbor);
                }
            }
        }
        System.out.println(String.join(" ", result));
    }
}
