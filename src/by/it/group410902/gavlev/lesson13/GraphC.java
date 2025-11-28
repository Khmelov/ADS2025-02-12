package by.it.group410902.gavlev.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> revGraph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        for (String edge : input.split(",")) {
            String[] parts = edge.trim().split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            revGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            vertices.add(from);
            vertices.add(to);
        }

        Set<String> visited = new HashSet<>();
        Stack<String> order = new Stack<>();
        for (String v : vertices) if (!visited.contains(v)) dfs1(v, graph, visited, order);

        visited.clear();
        List<List<String>> components = new ArrayList<>();
        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, revGraph, visited, comp);
                Collections.sort(comp);
                components.add(comp);
            }
        }

        for (List<String> comp : components) {
            System.out.println(String.join("", comp));
        }
    }

    private static void dfs1(String v, Map<String, List<String>> graph, Set<String> visited, Stack<String> order) {
        if (visited.contains(v)) return;
        visited.add(v);
        if (graph.containsKey(v)) {
            for (String to : graph.get(v)) dfs1(to, graph, visited, order);
        }
        order.push(v);
    }

    private static void dfs2(String v, Map<String, List<String>> revGraph, Set<String> visited, List<String> comp) {
        if (visited.contains(v)) return;
        visited.add(v);
        comp.add(v);
        if (revGraph.containsKey(v)) {
            for (String to : revGraph.get(v)) dfs2(to, revGraph, visited, comp);
        }
    }
}
