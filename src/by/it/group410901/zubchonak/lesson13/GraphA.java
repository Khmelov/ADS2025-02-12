package by.it.group410901.zubchonak.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();
        scanner.close();

        // Разбор входных данных
        Map<String, List<String>> adj = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        if (!line.isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                edge = edge.trim();
                if (edge.contains("->")) {
                    String[] parts = edge.split("->");
                    String from = parts[0].trim();
                    String to = parts[1].trim();
                    adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                    allVertices.add(from);
                    allVertices.add(to);
                }
            }
        }

        // Добавляем вершины без исходящих рёбер
        for (String v : allVertices) {
            adj.putIfAbsent(v, new ArrayList<>());
        }

        // Топологическая сортировка (Kahn + PriorityQueue для лексикографии)
        Map<String, Integer> inDegree = new HashMap<>();
        for (String v : allVertices) {
            inDegree.put(v, 0);
        }
        for (List<String> neighbors : adj.values()) {
            for (String v : neighbors) {
                inDegree.merge(v, 1, Integer::sum);
            }
        }

        PriorityQueue<String> pq = new PriorityQueue<>(); // лексикографический порядок
        for (String v : allVertices) {
            if (inDegree.get(v) == 0) {
                pq.offer(v);
            }
        }

        List<String> topo = new ArrayList<>();
        while (!pq.isEmpty()) {
            String u = pq.poll();
            topo.add(u);
            for (String v : adj.get(u)) {
                int newDegree = inDegree.get(v) - 1;
                inDegree.put(v, newDegree);
                if (newDegree == 0) {
                    pq.offer(v);
                }
            }
        }

        // Вывод
        System.out.println(String.join(" ", topo));
    }
}