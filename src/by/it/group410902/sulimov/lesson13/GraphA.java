package by.it.group410902.sulimov.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // Разделяем по запятым для получения списка ребер
        String[] edges = input.split(", ");

        for (String edge : edges) {
            // Разделяем каждое ребро по "->"
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Инициализируем inDegree для вершин если нужно
            inDegree.putIfAbsent(from, 0);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        // Находим все вершины (могут быть вершины без исходящих ребер)
        Set<String> allVertices = new TreeSet<>(); // TreeSet для автоматической сортировки
        allVertices.addAll(graph.keySet());
        for (String vertex : inDegree.keySet()) {
            allVertices.add(vertex);
        }

        // Топологическая сортировка с использованием PriorityQueue для лексикографического порядка
        List<String> result = topologicalSort(graph, inDegree, allVertices);

        // Вывод результата
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph, Map<String, Integer> inDegree, Set<String> allVertices) {
        List<String> result = new ArrayList<>();

        // PriorityQueue для лексикографического порядка
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем вершины с нулевой входящей степенью
        for (String vertex : allVertices) {
            if (inDegree.getOrDefault(vertex, 0) == 0) {
                queue.offer(vertex);
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Обрабатываем соседей
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