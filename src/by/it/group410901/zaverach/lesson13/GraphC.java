package by.it.group410901.zaverach.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.hasNextLine() ? sc.nextLine().trim() : "";
        sc.close();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new TreeSet<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(",\\s*");
            for (String edge : edges) {
                if (!edge.contains("->")) continue;
                String[] parts = edge.split("->");
                if (parts.length != 2) continue;
                String from = parts[0].trim();
                String to = parts[1].trim();
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                vertices.add(from);
                vertices.add(to);
            }
        }

        for (String v : vertices) graph.putIfAbsent(v, new ArrayList<>());

        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        for (String v : vertices) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, stack);
            }
        }

        Map<String, List<String>> transposed = new HashMap<>();
        for (String v : vertices) transposed.put(v, new ArrayList<>());

        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            for (String v : entry.getValue()) {
                transposed.get(v).add(entry.getKey());
            }
        }

        visited.clear();
        List<List<String>> sccs = new ArrayList<>();

        while (!stack.isEmpty()) {
            String v = stack.pop();
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfs2(v, transposed, visited, component);
                Collections.sort(component); // внутри компоненты сортируем
                sccs.add(component);
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sccs.size(); i++) {
            for (String vertex : sccs.get(i)) {
                result.append(vertex);
            }
            if (i < sccs.size() - 1) result.append("\n");
        }

        System.out.println(result);
    }

    private static void dfs1(String v, Map<String, List<String>> graph,
                             Set<String> visited, Stack<String> stack) {
        visited.add(v);
        for (String n : graph.get(v)) {
            if (!visited.contains(n)) {
                dfs1(n, graph, visited, stack);
            }
        }
        stack.push(v);
    }

    private static void dfs2(String v, Map<String, List<String>> graph,
                             Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);
        for (String n : graph.get(v)) {
            if (!visited.contains(n)) {
                dfs2(n, graph, visited, comp);
            }
        }
    }
}
