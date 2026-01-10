package by.it.group451002.morozov.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String input = sc.nextLine().trim();
        sc.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        String[] parts = input.split(",");
        for (String part : parts) {
            String[] p = part.trim().split("->");
            String from = p[0].trim();
            String to = p[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);

            indegree.putIfAbsent(from, 0);
            indegree.put(to, indegree.getOrDefault(to, 0) + 1);
        }

        PriorityQueue<String> pq = new PriorityQueue<>();

        for (String v : graph.keySet()) {
            if (indegree.getOrDefault(v, 0) == 0) {
                pq.add(v);
            }
        }

        List<String> topol = new ArrayList<>();

        // Топологическая сортировка
        while (!pq.isEmpty()) {
            String v = pq.poll();
            topol.add(v);

            for (String nei : graph.get(v)) {
                indegree.put(nei, indegree.get(nei) - 1);
                if (indegree.get(nei) == 0) {
                    pq.add(nei);
                }
            }
        }

        System.out.println(String.join(" ", topol));
    }
}