package by.it.group410901.garkusha.lesson13;
import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Для хранения списков смежности (какие вершины достижимы из каждой вершины)
        Map<String, List<String>> graph = new HashMap<>();
        // Для хранения количества входящих рёбер для каждой вершины
        Map<String, Integer> inDegree = new HashMap<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Инициализируем списки смежности и входящие степени
            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);

            // Добавляем ребро в граф
            graph.get(from).add(to);
            // Обновляем входящую степень
            inDegree.put(to, inDegree.get(to) + 1);
        }

        List<String> sorted = topologicalSort(graph, inDegree);

        for (int i = 0; i < sorted.size(); i++) {
            System.out.print(sorted.get(i));
            if (i < sorted.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph,
                                                Map<String, Integer> inDegree) {
        List<String> result = new ArrayList<>();

        // Используем PriorityQueue для лексикографического порядка
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем все вершины с нулевой входящей степенью
        for (String vertex : inDegree.keySet()) {
            if (inDegree.get(vertex) == 0) {
                queue.offer(vertex);
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Обрабатываем всех соседей текущей вершины
            for (String neighbor : graph.get(current)) {
                // Уменьшаем входящую степень
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                // Если степень стала 0, добавляем в очередь
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        return result;
    }
}
