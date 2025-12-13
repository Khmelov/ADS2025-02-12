package by.it.group451001.russu.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        sc.close();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            vertices.add(from);
            vertices.add(to);
        }

        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();
        boolean hasCycle = false;

        for (String v : vertices) {
            if (!visited.contains(v)) {
                if (dfs(v, graph, visited, stack)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String v, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> stack) {
        visited.add(v);
        stack.add(v);

        if (graph.containsKey(v)) {
            for (String to : graph.get(v)) {
                if (!visited.contains(to)) {
                    if (dfs(to, graph, visited, stack)) {
                        return true;
                    }
                } else if (stack.contains(to)) {
                    return true; // цикл найден
                }
            }
        }

        stack.remove(v);
        return false;
    }
}
