package by.it.group410902.sulimov.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки и построение графа
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        // Проверка на наличие циклов
        boolean hasCycle = hasCycle(graph);

        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        // Проверяем все вершины графа
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (dfsCycleCheck(node, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }

        // Также проверяем вершины, которые есть только в значениях (как конечные)
        for (List<String> neighbors : graph.values()) {
            for (String node : neighbors) {
                if (!graph.containsKey(node) && !visited.contains(node)) {
                    // Вершина без исходящих ребер - не может быть частью цикла
                    visited.add(node);
                }
            }
        }

        return false;
    }

    private static boolean dfsCycleCheck(String node, Map<String, List<String>> graph, Set<String> visited, Set<String> recursionStack) {
        // Помечаем вершину как посещенную и добавляем в стек рекурсии
        visited.add(node);
        recursionStack.add(node);

        // Проверяем всех соседей
        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    if (dfsCycleCheck(neighbor, graph, visited, recursionStack)) {
                        return true;
                    }
                } else if (recursionStack.contains(neighbor)) {
                    return true;
                }
            }
        }
        // Убираем вершину из стека рекурсии перед возвратом
        recursionStack.remove(node);
        return false;
    }
}
