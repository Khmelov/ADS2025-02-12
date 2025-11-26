package by.it.group410901.getmanchuk.lesson13;

import java.util.*;

// Проверка графа на наличие циклов (DFS с маркировкой вершин)

public class GraphB {

    public static void main(String[] args) {

        // Чтение входной строки с рёбрами графа
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Список смежности для представления графа
        Map<String, List<String>> graph = new HashMap<>();

        // Множество всех вершин графа
        Set<String> vertices = new HashSet<>();

        // Разделение входной строки на отдельные рёбра
        String[] edges = input.split(", ");

        // Построение графа из пар вершин "A -> B"
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        // Проверка графа на наличие циклов
        boolean hasCycle = hasCycle(graph, vertices);

        // Вывод результата: "yes" если цикл найден, иначе "no"
        System.out.println(hasCycle ? "yes" : "no");
    }

    // Проверка всех вершин графа на наличие цикла
    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> vertices) {

        // Хранение статуса посещения каждой вершины (0, 1, 2)
        Map<String, Integer> visited = new HashMap<>();

        // Инициализация всех вершин как не посещённых
        for (String vertex : vertices)
            visited.put(vertex, 0);

        // Запуск DFS для каждой вершины, которая ещё не посещена
        for (String vertex : vertices) {
            if (visited.get(vertex) == 0) {
                if (dfs(vertex, graph, visited))
                    return true;
            }
        }

        // Циклов не найдено
        return false;
    }

    // Рекурсивный поиск в глубину с проверкой обратных рёбер
    private static boolean dfs(String vertex, Map<String, List<String>> graph, Map<String, Integer> visited) {

        // Помечаем вершину как "в процессе обработки"
        visited.put(vertex, 1);

        // Проверяем всех соседей текущей вершины
        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {

                // Если сосед не посещён — продолжаем DFS
                if (visited.get(neighbor) == 0) {
                    if (dfs(neighbor, graph, visited))
                        return true;
                }

                // Если сосед уже "в процессе" — найден цикл
                else if (visited.get(neighbor) == 1) {
                    return true;
                }
            }
        }

        // Помечаем вершину как "обработанную"
        visited.put(vertex, 2);

        // Цикл из этой вершины не найден
        return false;
    }
}