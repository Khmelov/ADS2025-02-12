package by.it.group410902.gavlev.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new TreeSet<>();

        for (String edge : input.split(",")) {
            String[] parts = edge.trim().split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            vertices.add(from);
            vertices.add(to);
        }

        Map<String, Integer> indegree = new HashMap<>();
        for (String v : vertices) indegree.put(v, 0);
        for (List<String> adj : graph.values()) {
            for (String to : adj) indegree.put(to, indegree.get(to) + 1);
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : vertices) if (indegree.get(v) == 0) pq.add(v);

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String v = pq.poll();
            result.add(v);
            if (graph.containsKey(v)) {
                for (String to : graph.get(v)) {
                    indegree.put(to, indegree.get(to) - 1);
                    if (indegree.get(to) == 0) pq.add(to);
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}
