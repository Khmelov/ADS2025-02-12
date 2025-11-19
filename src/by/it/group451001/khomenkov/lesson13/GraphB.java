package by.it.group451001.khomenkov.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        // Чтение входной строки
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсинг строки и построение графа
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        // Разделяем по запятым чтобы получить список ребер
        String[] edges = input.split(", ");

        for (String edge : edges) {
            // Разделяем каждое ребро по "->"
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Сохраняем все вершины
            allVertices.add(from);
            allVertices.add(to);
        }

        // Проверка на наличие циклов
        boolean hasCycle = hasCycle(graph, allVertices);

        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> allVertices) {
        // Три состояния: 0 - не посещена, 1 - посещается, 2 - полностью обработана
        Map<String, Integer> visited = new HashMap<>();

        // Инициализируем все вершины как не посещенные
        for (String vertex : allVertices) {
            visited.put(vertex, 0);
        }

        // Проверяем каждую вершину
        for (String vertex : allVertices) {
            if (visited.get(vertex) == 0) {
                if (dfsHasCycle(vertex, graph, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean dfsHasCycle(String current, Map<String, List<String>> graph,
                                       Map<String, Integer> visited) {
        // Помечаем вершину как посещаемую (в текущем пути)
        visited.put(current, 1);

        // Проверяем всех соседей
        if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                if (visited.get(neighbor) == 0) {
                    // Если сосед не посещен, рекурсивно проверяем его
                    if (dfsHasCycle(neighbor, graph, visited)) {
                        return true;
                    }
                } else if (visited.get(neighbor) == 1) {
                    // Если сосед уже в текущем пути - найден цикл
                    return true;
                }
            }
        }

        // Помечаем вершину как полностью обработанную
        visited.put(current, 2);
        return false;
    }
}