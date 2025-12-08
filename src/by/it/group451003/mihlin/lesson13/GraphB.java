package by.it.group451003.mihlin.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        // Считываем строку с описанием графа
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсим строку и строим граф
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        // Разбиваем строку по запятым для получения отдельных рёбер
        String[] edges = input.split(", ");

        for (String edge : edges) {
            // Разделяем вершины по "->"
            String[] vertices = edge.split(" -> ");
            String from = vertices[0].trim();
            String to = vertices[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Сохраняем все вершины
            allVertices.add(from);
            allVertices.add(to);
        }

        // Проверяем наличие циклов
        boolean hasCycle = hasCycle(graph, allVertices);

        // Выводим результат
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> allVertices) {
        Map<String, Integer> visited = new HashMap<>(); // 0 - не посещена, 1 - в процессе, 2 - обработана

        // Инициализируем все вершины как не посещенные
        for (String vertex : allVertices) {
            visited.put(vertex, 0);
        }

        // Запускаем DFS для каждой непосещенной вершины
        for (String vertex : allVertices) {
            if (visited.get(vertex) == 0) {
                if (dfs(vertex, graph, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean dfs(String current, Map<String, List<String>> graph,
                               Map<String, Integer> visited) {
        // Помечаем вершину как обрабатываемую
        visited.put(current, 1);

        // Проверяем всех соседей
        if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                if (visited.get(neighbor) == 0) {
                    // Рекурсивно посещаем непосещенного соседа
                    if (dfs(neighbor, graph, visited)) {
                        return true;
                    }
                } else if (visited.get(neighbor) == 1) {
                    // Нашли цикл - вершина в процессе обработки
                    return true;
                }
            }
        }

        // Помечаем вершину как полностью обработанную
        visited.put(current, 2);
        return false;
    }
}