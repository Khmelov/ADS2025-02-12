package by.it.group410901.konon.lesson13;

import java.util.*;


public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        //хранение графа: вершина -> список смежных вершин
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allNodes = new TreeSet<>(); //для лексикографического порядка

        String[] edges = input.split(",");

        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            if (parts.length != 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            allNodes.add(from);
            allNodes.add(to);
        }

        //добавляем вершины без исходящих рёбер
        for (String node : allNodes) {
            graph.putIfAbsent(node, new ArrayList<>());
        }

        //подсчёт входящих степеней
        Map<String, Integer> indegree = new HashMap<>();
        for (String node : allNodes) indegree.put(node, 0);
        for (List<String> neighbors : graph.values()) {
            for (String v : neighbors) {
                indegree.put(v, indegree.get(v) + 1);
            }
        }

        //очередь вершин с нулевой степенью входа (лексикографический порядок)
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String node : indegree.keySet()) {
            if (indegree.get(node) == 0)
                pq.add(node);
        }

        List<String> topoOrder = new ArrayList<>();

        while (!pq.isEmpty()) {
            String u = pq.poll();
            topoOrder.add(u);
            for (String v : graph.get(u)) {
                indegree.put(v, indegree.get(v) - 1);
                if (indegree.get(v) == 0)
                    pq.add(v);
            }
        }

        //проверка на наличие цикла
        if (topoOrder.size() != allNodes.size()) {
            System.out.println("Граф содержит цикл!");
        } else {
            System.out.println(String.join(" ", topoOrder));
        }
    }
}