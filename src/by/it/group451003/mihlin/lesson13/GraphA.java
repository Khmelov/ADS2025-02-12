package by.it.group451003.mihlin.lesson13;
import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        // Считываем строку с описанием графа
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсим строку и строим граф
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // Разбиваем строку по запятым для получения отдельных рёбер
        String[] edges = input.split(", ");

        for (String edge : edges) {
            // Разделяем вершины по "->"
            String[] vertices = edge.split(" -> ");
            String from = vertices[0].trim();
            String to = vertices[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Инициализируем степени для вершин
            inDegree.putIfAbsent(from, 0);
            inDegree.putIfAbsent(to, 0);

            // Увеличиваем входящую степень для целевой вершины
            inDegree.put(to, inDegree.get(to) + 1);
        }

        // Топологическая сортировка с использованием алгоритма Кана
        List<String> result = topologicalSort(graph, inDegree);

        // Выводим результат
        for (String vertex : result) {
            System.out.print(vertex + " ");
        }
    }

    private static List<String> topologicalSort(Map<String, List<String>> graph,
                                                Map<String, Integer> inDegree) {
        List<String> result = new ArrayList<>();

        // PriorityQueue для лексикографического порядка при равнозначности
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем вершины с нулевой входящей степенью
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Обрабатываем соседей текущей вершины
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