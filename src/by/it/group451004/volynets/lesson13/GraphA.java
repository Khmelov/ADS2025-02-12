package by.it.group451004.volynets.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("");
            return;
        }

        // Множество всех вершин
        Set<String> vertices = new HashSet<>();
        // Список смежности
        Map<String, Set<String>> graph = new HashMap<>();
        // Степени входа
        Map<String, Integer> indegree = new HashMap<>();

        String[] parts = input.split(",");
        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;

            if (!part.contains("->")) {
                // одиночная вершина
                vertices.add(part);
                graph.putIfAbsent(part, new HashSet<>());
                indegree.putIfAbsent(part, 0);
                continue;
            }

            String[] edge = part.split("->");
            String from = edge[0].trim();
            String to = edge[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.putIfAbsent(from, new HashSet<>());
            graph.putIfAbsent(to, new HashSet<>());

            indegree.putIfAbsent(from, 0);
            indegree.putIfAbsent(to, 0);

            // добавляем ребро, избегая дубликатов
            if (graph.get(from).add(to)) {
                indegree.put(to, indegree.get(to) + 1);
            }
        }

        // Очередь с лексикографическим порядком
        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : vertices) {
            if (indegree.getOrDefault(v, 0) == 0) {
                pq.add(v); // кладём все вершины без входящих рёбер
            }
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            String v = pq.poll();       // достаём вершину с минимальным именем
            result.add(v);              // добавляем её в результат

            // уменьшаем степени входа у всех соседей
            for (String u : graph.getOrDefault(v, Collections.emptySet())) {
                indegree.put(u, indegree.get(u) - 1);
                if (indegree.get(u) == 0) {
                    pq.add(u);          // если степень стала 0 — добавляем в очередь
                }
            }
        }


        if (result.size() != vertices.size()) {
            System.out.println("");
            return;
        }

        System.out.println(String.join(" ", result));
    }
}
