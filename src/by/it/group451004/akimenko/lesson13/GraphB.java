package lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Разбиваем входную строку на рёбра
        String[] edges = input.split(", ");
        Map<Integer, List<Integer>> graph = new HashMap<>();

        // Строим граф на основе рёбер
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            int from = Integer.parseInt(parts[0]);
            int to = Integer.parseInt(parts[1]);
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        // Проверяем наличие циклов
        if (hasCycle(graph)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

    private static boolean hasCycle(Map<Integer, List<Integer>> graph) {
        // Множества для отслеживания состояний вершин
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recursionStack = new HashSet<>();

        // Проверяем каждую вершину графа
        for (Integer node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (dfs(node, graph, visited, recursionStack)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(Integer node, Map<Integer, List<Integer>> graph,
                               Set<Integer> visited, Set<Integer> recursionStack) {
        // Помечаем вершину как посещённую и добавляем в стек рекурсии
        visited.add(node);
        recursionStack.add(node);

        // Проверяем всех соседей текущей вершины
        if (graph.containsKey(node)) {
            for (Integer neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    // Если сосед не посещён, рекурсивно проверяем его
                    if (dfs(neighbor, graph, visited, recursionStack)) {
                        return true;
                    }
                } else if (recursionStack.contains(neighbor)) {
                    // Если сосед уже в стеке рекурсии, найден цикл
                    return true;
                }
            }
        }

        // Убираем вершину из стека рекурсии после обработки
        recursionStack.remove(node);
        return false;
    }
}
