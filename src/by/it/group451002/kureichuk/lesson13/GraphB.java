package by.it.group451002.kureichuk.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входных данных
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(",\\s*");

            for (String edge : edges) {
                String[] vertices = edge.split("\\s*->\\s*");
                String from = vertices[0].trim();
                String to = vertices[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                allVertices.add(from);
                allVertices.add(to);
            }
        }

        // Проверка на наличие циклов
        boolean hasCycle = hasCycle(graph, allVertices);

        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph, Set<String> allVertices) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                if (dfs(vertex, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(String current, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> recursionStack) {
        // Помещаем вершину в стек рекурсии
        recursionStack.add(current);
        visited.add(current);

        // Проверяем всех соседей
        if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                if (!visited.contains(neighbor)) {
                    if (dfs(neighbor, graph, visited, recursionStack)) {
                        return true;
                    }
                } else if (recursionStack.contains(neighbor)) {
                    // Найдено обратное ребро - цикл существует
                    return true;
                }
            }
        }

        // Убираем вершину из стека рекурсии перед возвратом
        recursionStack.remove(current);
        return false;
    }
}