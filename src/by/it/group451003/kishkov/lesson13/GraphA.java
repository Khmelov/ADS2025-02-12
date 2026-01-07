package by.it.group451003.kishkov.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.trim().split("\\s*->\\s*");

            if (parts.length < 2)
                continue;

            String from = parts[0];
            String to = parts[1];
            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        for (String vertex : vertices)
            graph.putIfAbsent(vertex, new ArrayList<>());

        List<String> result = topologicalSort(graph, vertices);

        for (int i = 0; i < result.size(); i++) {
            if (i > 0)
                System.out.print(" ");
            System.out.print(result.get(i));
        }
        System.out.println();
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph, Set<String> vertices) {
        Map<String, Integer> inDegree = new HashMap<>();

        for (String v : vertices)
            inDegree.put(v, 0);

        for (List<String> list : graph.values())
            for (String to : list)
                inDegree.put(to, inDegree.get(to) + 1);

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String v : vertices)
            if (inDegree.get(v) == 0)
                queue.offer(v);

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String v = queue.poll();
            result.add(v);

            List<String> neighbors = new ArrayList<>(graph.get(v));
            Collections.sort(neighbors);

            for (String to : neighbors) {
                inDegree.put(to, inDegree.get(to) - 1);
                if (inDegree.get(to) == 0)
                    queue.offer(to);
            }
        }

        return result;
    }
}