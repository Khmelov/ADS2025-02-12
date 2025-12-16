package by.it.group410901.bandarzheuskaya.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        // Считываем входную строку с описанием орграфа
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Инициализируем граф как список смежности, где ключ — вершина, значение — список её соседей
        Map<String, List<String>> graph = new HashMap<>();

        // Разбиваем входную строку на части (рёбра или изолированные вершины)
        String[] parts = input.split(", ");
        for (String part : parts) {
            // Проверяем, является ли часть ребром (содержит "->")
            if (part.contains("->")) {
                // Разбиваем ребро на источник и приёмник
                String[] edge = part.split("->");
                String from = edge[0].trim();
                String to = edge[1].trim();
                // Добавляем ребро в граф: from -> to
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                // Гарантируем, что приёмник есть в графе (даже если у него нет исходящих рёбер)
                graph.putIfAbsent(to, new ArrayList<>());
            } else {
                // Если часть — изолированная вершина, добавляем её в граф с пустым списком соседей
                String vertex = part.trim();
                graph.putIfAbsent(vertex, new ArrayList<>());
            }
        }

        // Проверяем наличие цикла в графе
        boolean hasCycle = hasCycle(graph);
        // Выводим результат: "yes" если есть цикл, "no" если цикла нет
        System.out.println(hasCycle ? "yes" : "no");
    }


    private static boolean hasCycle(Map<String, List<String>> graph) {

        Set<String> visited = new HashSet<>();//для хранения всех посещённых вершин
        Set<String> recursionStack = new HashSet<>(); //для хранения вершин в текущем пути рекурсии

        // Проверяем каждую вершину графа
        for (String node : graph.keySet()) {
            // Если вершина не посещена, запускаем DFS для неё
            if (!visited.contains(node)) {
                if (dfs(node, graph, visited, recursionStack)) {
                    return true; // Цикл найден
                }
            }
        }
        // Если цикл не найден после проверки всех вершин, возвращаем false
        return false;
    }

    private static boolean dfs(String current, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> recursionStack) {
        // Помечаем текущую вершину как посещённую и добавляем в стек рекурсии
        visited.add(current);
        recursionStack.add(current);

        // Проверяем всех соседей текущей вершины
        if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                // Если сосед не посещён, рекурсивно проверяем его
                if (!visited.contains(neighbor)) {
                    if (dfs(neighbor, graph, visited, recursionStack)) {
                        return true; // Цикл найден в подграфе
                    }
                }
                // Если сосед уже в стеке рекурсии, найден цикл
                else if (recursionStack.contains(neighbor)) {
                    return true;
                }
            }
        }

        // Удаляем текущую вершину из стека рекурсии перед возвратом
        recursionStack.remove(current);
        return false;
    }
}