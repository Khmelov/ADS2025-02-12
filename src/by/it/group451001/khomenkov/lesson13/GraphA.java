package by.it.group451001.khomenkov.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        // Чтение входной строки
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсинг строки и построение графа
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // Разделяем по запятым чтобы получить список ребер
        String[] edges = input.split(", ");

        for (String edge : edges) {
            // Разделяем каждое ребро по "->"
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Инициализируем inDegree для всех вершин
            inDegree.putIfAbsent(from, 0);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        // Топологическая сортировка
        List<String> result = topologicalSort(graph, inDegree);

        // Вывод результата
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph,
                                                Map<String, Integer> inDegree) {
        List<String> result = new ArrayList<>();

        // Приоритетная очередь для лексикографического порядка
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем все вершины с inDegree = 0
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Уменьшаем inDegree для всех соседей
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