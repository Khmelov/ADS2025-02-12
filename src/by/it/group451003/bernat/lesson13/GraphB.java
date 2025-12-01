package by.it.group451003.bernat.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        Map<String, List<String>> graph = new TreeMap<>();
        Set<String> allNodes = new TreeSet<>();
        String[] edges = line.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String u = parts[0];
            String v = parts[1];
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            allNodes.add(u);
            allNodes.add(v);
        }
        for (String node : allNodes) {
            graph.computeIfAbsent(node, k -> new ArrayList<>());
        }
        Map<String, Integer> inDegree = new HashMap<>();
        for (String node : allNodes) {
            inDegree.put(node, 0);
        }
        for (List<String> neighbors : graph.values()) {
            for (String v : neighbors) {
                inDegree.put(v, inDegree.get(v) + 1);
            }
        }
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String node : allNodes) {
            if (inDegree.get(node) == 0) {
                queue.add(node);
            }
        }
        int processed = 0;
        while (!queue.isEmpty()) {
            String u = queue.poll();
            processed++;
            List<String> neighbors = graph.get(u);
            Collections.sort(neighbors);
            for (String v : neighbors) {
                inDegree.put(v, inDegree.get(v) - 1);
                if (inDegree.get(v) == 0) {
                    queue.add(v);
                }
            }
        }
        if (processed == allNodes.size()) {
            System.out.println("no");
        } else {
            System.out.println("yes");
        }
    }
}