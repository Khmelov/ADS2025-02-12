package by.it.group410901.zaverach.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.hasNextLine() ? sc.nextLine().trim() : "";
        sc.close();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new TreeSet<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(",\\s*");
            for (String edge : edges) {
                if (!edge.contains("->")) continue;
                String[] parts = edge.split("->");
                if (parts.length != 2) continue;
                String from = parts[0].trim();
                String to = parts[1].trim();
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                vertices.add(from);
                vertices.add(to);
            }
        }

        for (String v : vertices) graph.putIfAbsent(v, new ArrayList<>());

        Map<String, Integer> indegree = new HashMap<>();
        for (String v : vertices) indegree.put(v, 0);
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                indegree.put(to, indegree.getOrDefault(to, 0) + 1);
            }
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : vertices) if (indegree.get(v) == 0) pq.add(v);

        StringBuilder result = new StringBuilder();
        int removed = 0;
        while (!pq.isEmpty()) {
            String cur = pq.poll();
            if (result.length() > 0) result.append(" ");
            result.append(cur);
            removed++;
            for (String nb : graph.get(cur)) {
                indegree.put(nb, indegree.get(nb) - 1);
                if (indegree.get(nb) == 0) pq.add(nb);
            }
        }


        if (removed != vertices.size() && !vertices.isEmpty()) {
            for (String v : vertices) {
                if (!result.toString().contains(v)) {
                    if (result.length() > 0) result.append(" ");
                    result.append(v);
                }
            }
        }

        System.out.println(result.toString());
    }
}
