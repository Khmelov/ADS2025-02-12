package by.it.group410902.kavtsevich.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        // Парсим входную строку в структуру графа
        Map<String, List<String>> graph = parseGraph(input);
        // Проверяем наличие циклов в графе
        boolean hasCycle = hasCycle(graph);
        // Выводим результат проверки
        System.out.println(hasCycle ? "yes" : "no");
        scanner.close();
    }


    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(",\\s*"); // Разделяем по запятым
        for (String edge : edges) {
            String[] vertices = edge.split("\\s*->\\s*"); // Разделяем по стрелке
            String from = vertices[0].trim(); // Вершина-источник
            String to = vertices[1].trim();   // Вершина-назначение

            // Добавляем ребро from -> to
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            // Гарантируем, что вершина 'to' тоже есть в графе
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }
        return graph;
    }

    /**
     * Проверяет наличие циклов в ориентированном графе
     */
    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();     // Все посещённые вершины
        Set<String> recStack = new HashSet<>();    // Вершины в текущем пути обхода (рекурсивном стеке)

        // Запускаем проверку для каждой вершины графа
        for (String vertex : graph.keySet()) {
            if (hasCycleUtil(vertex, graph, visited, recStack)) {
                return true; // Найден цикл
            }
        }
        return false; // Циклов не найдено
    }

    /**
     * Вспомогательная рекурсивная функция для поиска циклов
     * @param vertex - текущая вершина
     * @param graph - граф
     * @param visited - множество посещённых вершин
     * @param recStack - множество вершин в текущем пути обхода
     */
    private static boolean hasCycleUtil(String vertex, Map<String, List<String>> graph,
                                        Set<String> visited, Set<String> recStack) {
        // Если вершина уже находится в текущем пути обхода - найден цикл!
        if (recStack.contains(vertex)) {
            return true;
        }

        // Если вершина уже полностью обработана - пропускаем
        if (visited.contains(vertex)) {
            return false;
        }

        // Помечаем вершину как посещённую и добавляем в текущий путь
        visited.add(vertex);
        recStack.add(vertex);

        // Рекурсивно проверяем всех соседей текущей вершины
        List<String> neighbors = graph.getOrDefault(vertex, new ArrayList<>());
        for (String neighbor : neighbors) {
            // Если в поддереве соседа найден цикл - возвращаем true
            if (hasCycleUtil(neighbor, graph, visited, recStack)) {
                return true;
            }
        }

        // Убираем вершину из текущего пути (backtracking)
        recStack.remove(vertex);
        return false;
    }
}