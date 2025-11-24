package by.it.group451002.kureichuk.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входных данных
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // Разделяем по запятой для получения отдельных ребер
        String[] edges = input.split(",\\s*");

        for (String edge : edges) {
            // Разделяем каждое ребро по "->"
            String[] vertices = edge.split("\\s*->\\s*");
            String from = vertices[0].trim();
            String to = vertices[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Инициализируем степени захода для всех вершин
            inDegree.putIfAbsent(from, 0);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        // Топологическая сортировка
        List<String> result = topologicalSort(graph, inDegree);

        // Вывод результата
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph,
                                                Map<String, Integer> inDegree) {
        List<String> result = new ArrayList<>();

        // Приоритетная очередь для лексикографического порядка
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем вершины с нулевой степенью захода
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Уменьшаем степень захода для всех соседей
            if (graph.containsKey(current)) {
                for (String neighbor : graph.get(current)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return result;
    }
}