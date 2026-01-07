package by.it.group451001.shymko.lesson13;

import java.util.*;
import java.util.stream.Collectors;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            if(!graph.containsKey(from)) {
                graph.put(from, new ArrayList<>());
            }
            graph.get(from).add(to);

            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);
        }

        for (List<String> neighbors : graph.values()) {
            for (String neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        List<String> result = topologicalSort(graph, inDegree);

        System.out.println(String.join(" ", result));
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph,
                                                Map<String, Integer> inDegree) {
        PriorityQueue<String> queue = new PriorityQueue<>();
        List<String> result = new ArrayList<>();

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