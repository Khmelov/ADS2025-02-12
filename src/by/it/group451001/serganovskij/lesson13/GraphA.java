package by.it.group451001.serganovskij.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // читаем ввод, например: A->B, A->C, B->D

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        // Разбираем рёбра и строим граф
        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.trim().split("\\s*->\\s*");
            if (parts.length < 2) continue;

            String from = parts[0];
            String to = parts[1];
            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        // Добавляем вершины без исходящих рёбер
        for (String vertex : vertices) {
            graph.putIfAbsent(vertex, new ArrayList<>());
        }

        // Выполняем топологическую сортировку
        List<String> result = topologicalSort(graph, vertices);

        // Вывод результата
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
        System.out.println();
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph, Set<String> vertices) {
        Map<String, Integer> inDegree = new HashMap<>();

        // Считаем количество входящих рёбер
        for (String v : vertices) inDegree.put(v, 0);
        for (List<String> list : graph.values()) {
            for (String to : list) inDegree.put(to, inDegree.get(to) + 1);
        }

        // Вершины без входящих рёбер — в очередь (лексикографический порядок)
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String v : vertices) if (inDegree.get(v) == 0) queue.offer(v);

        List<String> result = new ArrayList<>();

        // Алгоритм Кана
        while (!queue.isEmpty()) {
            String v = queue.poll();
            result.add(v);

            List<String> neighbors = new ArrayList<>(graph.get(v));
            Collections.sort(neighbors); // для стабильности вывода

            for (String to : neighbors) {
                inDegree.put(to, inDegree.get(to) - 1);
                if (inDegree.get(to) == 0) queue.offer(to);
            }
        }

        return result;
    }
}
