package by.it.group410902.derzhavskaya_e.lesson13;

import java.util.*;

public class GraphC {

    private static Map<String, List<String>> graph = new HashMap<>();
    private static Map<String, List<String>> reverseGraph = new HashMap<>();
    private static Set<String> allVertices = new HashSet<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        parseGraph(input);

        // 1. Первая DFS — по исходному графу
        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();
        for (String v : allVertices) {
            if (!visited.contains(v))
                dfs1(v, visited, order);
        }

        // 2. Вторая DFS — по обратному
        visited.clear();
        List<List<String>> scc = new ArrayList<>();

        while (!order.isEmpty()) {
            String v = order.pollLast();
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfs2(v, visited, component);
                Collections.sort(component); // лексикографически
                scc.add(component);
            }
        }

        // Печать компонент
        for (List<String> comp : scc) {
            for (String v : comp) System.out.print(v);
            System.out.println();
        }
    }

    private static void parseGraph(String input) {
        String[] parts = input.split(",");
        for (String p : parts) {
            p = p.trim();
            String[] edge = p.split("->");
            String from = edge[0].trim();
            String to = edge[1].trim();

            allVertices.add(from);
            allVertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            // чтобы не потерять вершины без исходящих/входящих
            graph.putIfAbsent(to, new ArrayList<>());
            reverseGraph.putIfAbsent(from, new ArrayList<>());
        }
    }

    private static void dfs1(String v, Set<String> visited, Deque<String> order) {
        visited.add(v);
        for (String nxt : graph.getOrDefault(v, Collections.emptyList())) {
            if (!visited.contains(nxt))
                dfs1(nxt, visited, order);
        }
        order.add(v);
    }

    private static void dfs2(String v, Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);

        for (String nxt : reverseGraph.getOrDefault(v, Collections.emptyList())) {
            if (!visited.contains(nxt))
                dfs2(nxt, visited, comp);
        }
    }
}
