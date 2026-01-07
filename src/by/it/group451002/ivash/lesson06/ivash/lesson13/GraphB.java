package by.it.group451002.ivash.lesson06.ivash.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        sc.close();

        // Создаем граф
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> nodes = new HashSet<>();

        // Разбираем входную строку
        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];

            nodes.add(from);
            nodes.add(to);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
        }

        // Проверка на цикл с помощью DFS
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        boolean hasCycle = false;

        for (String node : nodes) {
            if (!visited.contains(node)) {
                if (dfs(node, graph, visited, recursionStack)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String node, Map<String, List<String>> graph, Set<String> visited,
                               Set<String> recursionStack) {
        visited.add(node);
        recursionStack.add(node);

        for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, graph, visited, recursionStack)) return true;
            } else if (recursionStack.contains(neighbor)) {
                // Найден цикл
                return true;
            }
        }

        recursionStack.remove(node);
        return false;
    }
}
