package by.it.group451004.volynets.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextLine()) {
            System.out.print("no");
            scanner.close();
            return;
        }

        String input = scanner.nextLine();

        if (input == null || input.trim().isEmpty()) {
            System.out.print("no");
            scanner.close();
            return;
        }

        // Парсинг строки и построение графа
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        input = input.replaceAll("\\s*->\\s*", "->");
        input = input.replaceAll("\\s*,\\s*", ",");
        String[] edges = input.split(",");

        for (String edge : edges) {
            if (edge.trim().isEmpty()) continue;

            String[] parts = edge.split("->");
            if (parts.length != 2) {
                // Вершина без ребер
                String vertex = parts[0].trim();
                graph.putIfAbsent(vertex, new ArrayList<>());
                allVertices.add(vertex);
                continue;
            }

            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.putIfAbsent(to, new ArrayList<>());
            allVertices.add(from);
            allVertices.add(to);
        }

        boolean hasCycle = hasCycle(graph, allVertices);

        System.out.print(hasCycle ? "yes" : "no");

        scanner.close();
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

    // Рекурсивный DFS для обнаружения циклов
    private static boolean dfs(String vertex, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> recursionStack) {
        visited.add(vertex);          // отмечаем вершину как посещённую
        recursionStack.add(vertex);   // добавляем её в текущий стек рекурсии

        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {   // идём по всем соседям
                if (!visited.contains(neighbor)) {
                    if (dfs(neighbor, graph, visited, recursionStack)) {
                        return true;                      // цикл найден в глубине
                    }
                } else if (recursionStack.contains(neighbor)) {
                    // сосед уже в текущем пути → цикл
                    return true;
                }
            }
        }

        recursionStack.remove(vertex); // убираем вершину из стека при выходе
        return false;                  // цикла нет
    }
}
