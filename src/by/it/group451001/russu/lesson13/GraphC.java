package by.it.group451001.russu.lesson13;

import java.util.*;

public class GraphC {
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
        Deque<String> stack = new ArrayDeque<>();

        for (String v : vertices) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, stack);
            }
        }

        Map<String, List<String>> revGraph = new HashMap<>();
        for (String v : vertices) revGraph.put(v, new ArrayList<>());
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            for (String to : entry.getValue()) {
                revGraph.get(to).add(entry.getKey());
            }
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>();
        while (!stack.isEmpty()) {
            String v = stack.pop();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, revGraph, visited, comp);
                Collections.sort(comp);
                components.add(comp);
            }
        }

        Map<String, Integer> compIndex = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (String v : components.get(i)) {
                compIndex.put(v, i);
            }
        }

        int n = components.size();
        List<Set<Integer>> compGraph = new ArrayList<>();
        for (int i = 0; i < n; i++) compGraph.add(new HashSet<>());

        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                int ci = compIndex.get(from);
                int cj = compIndex.get(to);
                if (ci != cj) {
                    compGraph.get(ci).add(cj);
                }
            }
        }

        int[] indegree = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j : compGraph.get(i)) {
                indegree[j]++;
            }
        }

        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++) if (indegree[i] == 0) q.add(i);

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int v = q.poll();
            order.add(v);
            for (int u : compGraph.get(v)) {
                indegree[u]--;
                if (indegree[u] == 0) q.add(u);
            }
        }

        for (int idx : order) {
            List<String> comp = components.get(idx);
            for (String v : comp) System.out.print(v);
            System.out.println();
        }
    }

    private static void dfs1(String v, Map<String, List<String>> graph,
                             Set<String> visited, Deque<String> stack) {
        visited.add(v);
        if (graph.containsKey(v)) {
            for (String to : graph.get(v)) {
                if (!visited.contains(to)) {
                    dfs1(to, graph, visited, stack);
                }
            }
        }
        stack.push(v);
    }

    private static void dfs2(String v, Map<String, List<String>> revGraph,
                             Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);
        if (revGraph.containsKey(v)) {
            for (String to : revGraph.get(v)) {
                if (!visited.contains(to)) {
                    dfs2(to, revGraph, visited, comp);
                }
            }
        }
    }
}
