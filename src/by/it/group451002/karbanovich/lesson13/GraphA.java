package by.it.group451002.karbanovich.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Считываем всю строку графа
        String input = sc.nextLine().trim();
        sc.close();

        // adjacency list
        Map<String, List<String>> graph = new HashMap<>();
        // indegree map
        Map<String, Integer> indegree = new HashMap<>();

        // Разбор формата "0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1"
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

        // Лексикографическая очередь
        PriorityQueue<String> pq = new PriorityQueue<>();

        // Добавляем вершины с indegree = 0
        for (String v : graph.keySet()) {
            if (indegree.getOrDefault(v, 0) == 0) {
                pq.add(v);
            }
        }

        List<String> topo = new ArrayList<>();

        // Топологическая сортировка
        while (!pq.isEmpty()) {
            String v = pq.poll();
            topo.add(v);

            for (String nei : graph.get(v)) {
                indegree.put(nei, indegree.get(nei) - 1);
                if (indegree.get(nei) == 0) {
                    pq.add(nei);
                }
            }
        }

        // Вывод результата
        System.out.println(String.join(" ", topo));
    }
}

