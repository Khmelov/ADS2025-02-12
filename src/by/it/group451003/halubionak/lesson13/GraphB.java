package by.it.group451003.halubionak.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        // Считываем строку с описанием графа
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсим строку и строим граф
        Map<String, List<String>> graph = new HashMap<>();

        // Разбиваем строку по запятым для получения отдельных рёбер
        String[] edges = input.split(", ");

        for (String edge : edges) {
            // Разбиваем каждое ребро по "->"
            String[] parts = edge.split(" -> ");
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            // Добавляем вершину "to" в граф (если у неё нет исходящих рёбер)
            graph.putIfAbsent(to, new ArrayList<>());
        }

        // Проверяем наличие циклов
        boolean hasCycle = hasCycle(graph);

        // Выводим результат
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycle(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        // Проверяем каждую вершину на наличие циклов
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (dfs(node, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(String node, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> recursionStack) {
        // Помечаем вершину как посещённую и добавляем в стек рекурсии
        visited.add(node);
        recursionStack.add(node);

        // Проверяем всех соседей
        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    if (dfs(neighbor, graph, visited, recursionStack)) {
                        return true;
                    }
                } else if (recursionStack.contains(neighbor)) {
                    // Если сосед уже в стеке рекурсии - найден цикл
                    return true;
                }
            }
        }

        // Убираем вершину из стека рекурсии перед возвратом
        recursionStack.remove(node);
        return false;
    }
}
