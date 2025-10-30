package by.it.group410902.kavtsevich.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        // Парсим входную строку в структуру графа
        Map<String, List<String>> graph = parseGraph(input);
        // Выполняем топологическую сортировку
        List<String> result = topologicalSort(graph);
        // Выводим результат
        System.out.println(String.join(" ", result));
        scanner.close();
    }


    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(",\\s*"); // Разделяем по запятым
        for (String edge : edges) {
            String[] vertices = edge.split("\\s*->\\s*"); // Разделяем по стрелке
            String from = vertices[0].trim(); // Вершина-источник
            String to = vertices[1].trim();   // Вершина-назначение

            // Добавляем ребро from -> to
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            // Гарантируем, что вершина 'to' тоже есть в графе (даже если у неё нет исходящих рёбер)
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }
        return graph;
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>(); // Посещённые вершины
        List<String> result = new ArrayList<>(); // Результат топологической сортировки
        // PriorityQueue гарантирует обработку вершин в лексикографическом порядке
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Находим вершины без входящих рёбер (истоки)
        Set<String> noIncoming = new HashSet<>(graph.keySet());
        // Убираем из множества все вершины, которые являются назначениями рёбер
        for (List<String> neighbors : graph.values()) {
            noIncoming.removeAll(neighbors);
        }
        queue.addAll(noIncoming); // Начинаем с вершин без входящих рёбер

        while (!queue.isEmpty()) {
            String vertex = queue.poll(); // Берём вершину с наименьшим лексикографическим порядком
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                result.add(vertex); // Добавляем в результат
                // Обрабатываем всех соседей текущей вершины
                List<String> neighbors = graph.getOrDefault(vertex, new ArrayList<>());
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        queue.offer(neighbor); // Добавляем соседей в очередь
                    }
                }
            }
        }

        return result;
    }
}