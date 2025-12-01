package by.it.group451004.kozlov.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        String[] edges = input.split(",");
        for (String e : edges) {
            e = e.trim();
            String[] parts = e.split("->");
            if (parts.length != 2) continue;
            String from = parts[0].trim();
            String to = parts[1].trim();

            adj.putIfAbsent(from, new ArrayList<>());
            adj.putIfAbsent(to, new ArrayList<>());
            indegree.putIfAbsent(from, 0);
            indegree.putIfAbsent(to, 0);

            adj.get(from).add(to);
            indegree.put(to, indegree.get(to) + 1);
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : indegree.keySet()) {
            if (indegree.get(v) == 0) {
                pq.add(v);
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String u = pq.poll();
            result.add(u);
            for (String v : adj.get(u)) {
                indegree.put(v, indegree.get(v) - 1);
                if (indegree.get(v) == 0) {
                    pq.add(v);
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}
