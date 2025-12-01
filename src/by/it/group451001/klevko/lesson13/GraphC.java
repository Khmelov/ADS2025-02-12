package by.it.group451001.klevko.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().replaceAll("\\s+", "");
        sc.close();

        String[] parts = line.split(",");
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> revGraph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        for (String part : parts) {
            String[] uv = part.split("->");
            String u = uv[0], v = uv[1];
            vertices.add(u);
            vertices.add(v);
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            revGraph.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
            graph.putIfAbsent(v, new ArrayList<>());
            revGraph.putIfAbsent(u, new ArrayList<>());
        }

        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();
        for (String v : vertices) {
            if (!visited.contains(v)) dfs1(v, graph, visited, order);
        }

        visited.clear();
        List<List<String>> comps = new ArrayList<>();
        Map<String, Integer> compIndex = new HashMap<>();
        while (!order.isEmpty()) {
            String v = order.pollLast();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, revGraph, visited, comp);
                Collections.sort(comp);
                int idx = comps.size();
                for (String w : comp) compIndex.put(w, idx);
                comps.add(comp);
            }
        }

        int n = comps.size();
        List<Set<Integer>> cadj = new ArrayList<>();
        int[] indegree = new int[n];
        for (int i = 0; i < n; i++) cadj.add(new HashSet<>());

        for (String u : vertices) {
            int cu = compIndex.get(u);
            for (String v : graph.get(u)) {
                int cv = compIndex.get(v);
                if (cu != cv && cadj.get(cu).add(cv)) {
                    indegree[cv]++;
                }
            }
        }

        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) q.add(i);
        }
        List<Integer> topo = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            topo.add(u);
            for (int v : cadj.get(u)) {
                if (--indegree[v] == 0) q.add(v);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int idx : topo) {
            for (String v : comps.get(idx)) sb.append(v);
            sb.append("\n");
        }
        System.out.print(sb.toString().trim());
    }

    private static void dfs1(String v, Map<String, List<String>> g, Set<String> vis, Deque<String> order) {
        vis.add(v);
        for (String w : g.get(v)) {
            if (!vis.contains(w)) dfs1(w, g, vis, order);
        }
        order.add(v);
    }

    private static void dfs2(String v, Map<String, List<String>> rg, Set<String> vis, List<String> comp) {
        vis.add(v);
        comp.add(v);
        for (String w : rg.get(v)) {
            if (!vis.contains(w)) dfs2(w, rg, vis, comp);
        }
    }
}
