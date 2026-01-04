package by.it.group451002.karbanovich.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Считываем строку с описанием графа
        String input = sc.nextLine().trim();
        sc.close();

        Map<String, List<String>> graph = new HashMap<>();

        // Разбор строк формата "1 -> 2, 1 -> 3, 2 -> 3"
        String[] parts = input.split(",");
        for (String part : parts) {
            String[] p = part.trim().split("->");
            String from = p[0].trim();
            String to = p[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);
        }

        // Проверка на циклы (DFS)
        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (hasCycle(node, graph, visited, stack)) {
                    System.out.println("yes");
                    return;
                }
            }
        }

        System.out.println("no");
    }

    private static boolean hasCycle(
            String node,
            Map<String, List<String>> graph,
            Set<String> visited,
            Set<String> stack
    ) {
        visited.add(node);
        stack.add(node);

        for (String next : graph.get(node)) {
            if (!visited.contains(next)) {
                if (hasCycle(next, graph, visited, stack))
                    return true;
            } else if (stack.contains(next)) {
                return true; // цикл
            }
        }

        stack.remove(node);
        return false;
    }
}

