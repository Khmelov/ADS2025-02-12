package by.it.group451004.kozlov.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> transpose = new HashMap<>();

        String[] edges = input.split(",");
        for (String e : edges) {
            e = e.trim();
            String[] parts = e.split("->");
            if (parts.length != 2) continue;
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            transpose.putIfAbsent(from, new ArrayList<>());
            transpose.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);
            transpose.get(to).add(from);
        }

        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        for (String v : graph.keySet()) {
            if (!visited.contains(v)) {
                dfs(v, graph, visited, stack);
            }
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>();
        while (!stack.isEmpty()) {
            String v = stack.pop();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfsCollect(v, transpose, visited, comp);
                Collections.sort(comp);
                components.add(comp);
            }
        }

        for (List<String> comp : components) {
            System.out.println(String.join("", comp));
        }
    }

    private static void dfs(String v, Map<String, List<String>> graph, Set<String> visited, Deque<String> stack) {
        visited.add(v);
        for (String nei : graph.get(v)) {
            if (!visited.contains(nei)) {
                dfs(nei, graph, visited, stack);
            }
        }
        stack.push(v);
    }

    private static void dfsCollect(String v, Map<String, List<String>> graph, Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);
        for (String nei : graph.get(v)) {
            if (!visited.contains(nei)) {
                dfsCollect(nei, graph, visited, comp);
            }
        }
    }
}
