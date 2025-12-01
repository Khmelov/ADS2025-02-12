package by.it.group451001.yarkovich.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String result = topologicalSort(input);
        System.out.println(result);
        scanner.close();
    }

    // Алгоритм Кана для топологической сортировки:
    // 1. Строим граф и вычисляем in-degree (количество входящих ребер) для каждой вершины
    // 2. Находим вершины с in-degree = 0 (без входящих ребер) и добавляем их в очередь
    // 3. Последовательно извлекаем вершины из очереди, добавляем в результат
    // 4. Уменьшаем in-degree соседей извлеченной вершины
    // 5. Если in-degree соседа стал 0 - добавляем его в очередь
    // 6. Используем PriorityQueue для лексикографического порядка при равнозначности
    public static String topologicalSort(String input) {
        Map<String, List<String>> graph = buildGraph(input);
        Map<String, Integer> inDegree = calculateInDegree(graph);
        return performTopologicalSort(graph, inDegree);
    }

    // Преобразуем строку в структуру графа (список смежности)
    private static Map<String, List<String>> buildGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(",");

        for (String edge : edges) {
            edge = edge.trim();
            String[] nodes = edge.split("->");
            String from = nodes[0].trim();
            String to = nodes[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
            graph.putIfAbsent(to, new ArrayList<>());
        }

        return graph;
    }

    // Вычисляем количество входящих ребер для каждой вершины
    private static Map<String, Integer> calculateInDegree(Map<String, List<String>> graph) {
        Map<String, Integer> inDegree = new HashMap<>();

        for (String node : graph.keySet()) {
            inDegree.put(node, 0);
        }

        for (String node : graph.keySet()) {
            for (String neighbor : graph.get(node)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        return inDegree;
    }

    // Основная логика топологической сортировки
    private static String performTopologicalSort(Map<String, List<String>> graph, Map<String, Integer> inDegree) {
        List<String> result = new ArrayList<>();
        PriorityQueue<String> queue = new PriorityQueue<>();

        for (String node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                queue.offer(node);
            }
        }

        while (!queue.isEmpty()) {
            String currentNode = queue.poll();
            result.add(currentNode);

            for (String neighbor : graph.get(currentNode)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        if (result.size() != graph.size()) {
            throw new IllegalArgumentException("Graph has cycles");
        }

        return String.join(" ", result);
    }
}