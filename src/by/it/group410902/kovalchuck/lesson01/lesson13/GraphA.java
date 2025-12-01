package by.it.group410902.kovalchuck.lesson01.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Построение графа
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(", ");

        // Обработка каждого ребра графа
        for (String edge : edges) {
            // Разделяем ребро на начальную и конечную вершины
            String[] parts = edge.split(" -> ");
            String from = parts[0];
            String to = parts[1];

            // Добавляем вершины в граф, если их еще нет
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
        }

        // Выполняем топологическую сортировку графа
        List<String> result = topologicalSort(graph);

        // Вывод результата сортировки
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    //Реализация алгоритма Кана
    private static List<String> topologicalSort(Map<String, List<String>> graph) {
        List<String> result = new ArrayList<>(); // Результирующий список вершин
        Map<String, Integer> inDegree = new HashMap<>(); // Словарь для хранения степеней входа вершин

        // Инициализация степеней входа для всех вершин
        for (String node : graph.keySet()) {
            inDegree.put(node, 0);
        }

        // Вычисление степеней входа для всех вершин
        for (String node : graph.keySet()) {
            for (String neighbor : graph.get(node)) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        // Приоритетная очередь для вершин с нулевой степенью входа
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем в очередь все вершины с нулевой степенью входа
        for (String node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                queue.offer(node);
            }
        }

        // Алгоритм Кана для топологической сортировки
        while (!queue.isEmpty()) {
            // Извлекаем вершину с наименьшим лексикографическим значением
            String current = queue.poll();
            result.add(current); // Добавляем вершину в результат

            // Обрабатываем всех соседей текущей вершины
            for (String neighbor : graph.get(current)) {
                // Уменьшаем степень входа соседа
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                // Если степень входа стала нулевой, добавляем в очередь
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        return result;
    }
}