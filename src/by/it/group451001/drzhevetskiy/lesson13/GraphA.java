package by.it.group451001.drzhevetskiy.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();

        // adjacency list
        Map<String, List<String>> graph = new HashMap<>();
        // indegree
        Map<String, Integer> indegree = new HashMap<>();

        // Parse: "0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1"
        String[] parts = line.split(",");

        for (String p : parts) {
            p = p.trim();                 // "0 -> 2"
            String[] arr = p.split("->");
            String from = arr[0].trim();  // "0"
            String to = arr[1].trim();    // "2"

            // add nodes
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            indegree.putIfAbsent(from, 0);
            indegree.putIfAbsent(to, 0);

            // add edge
            graph.get(from).add(to);

            // indegree increment
            indegree.put(to, indegree.get(to) + 1);
        }

        // PriorityQueue for lexicographically smallest first
        PriorityQueue<String> pq = new PriorityQueue<>();

        for (String v : indegree.keySet()) {
            if (indegree.get(v) == 0) pq.add(v);
        }

        List<String> topo = new ArrayList<>();

        while (!pq.isEmpty()) {
            String v = pq.poll();
            topo.add(v);

            for (String neigh : graph.get(v)) {
                indegree.put(neigh, indegree.get(neigh) - 1);
                if (indegree.get(neigh) == 0) {
                    pq.add(neigh);
                }
            }
        }

        // print result
        System.out.println(String.join(" ", topo));
    }
}

