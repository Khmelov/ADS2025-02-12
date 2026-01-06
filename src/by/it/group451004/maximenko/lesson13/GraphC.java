package by.it.group451004.maximenko.lesson13;
import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        sc.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> rev = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] parts = input.split(",");
        for (String part : parts) {
            String[] lr = part.trim().split("->");
            if (lr.length != 2) continue;
            String from = lr[0].trim();
            String to = lr[1].trim();
            vertices.add(from);
            vertices.add(to);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);

            rev.putIfAbsent(from, new ArrayList<>());
            rev.putIfAbsent(to, new ArrayList<>());
            rev.get(to).add(from);
        }

        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();

        for (String v : vertices) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, order);
            }
        }

        Collections.reverse(order);
        visited.clear();

        List<List<String>> scc = new ArrayList<>();

        for (String v : order) {
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, rev, visited, comp);
                Collections.sort(comp);
                scc.add(comp);
            }
        }

        Map<List<String>, Integer> compIndex = new HashMap<>();
        for (int i = 0; i < scc.size(); i++) compIndex.put(scc.get(i), i);

        List<Set<Integer>> dag = new ArrayList<>();
        for (int i = 0; i < scc.size(); i++) dag.add(new HashSet<>());

        Map<String, Integer> belong = new HashMap<>();
        for (int ci = 0; ci < scc.size(); ci++)
            for (String v : scc.get(ci))
                belong.put(v, ci);

        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                int a = belong.get(from);
                int b = belong.get(to);
                if (a != b) dag.get(a).add(b);
            }
        }

        int n = scc.size();
        int[] indeg = new int[n];
        for (int i = 0; i < n; i++)
            for (int j : dag.get(i))
                indeg[j]++;

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < n; i++)
            if (indeg[i] == 0)
                pq.add(i);

        List<Integer> topo = new ArrayList<>();
        while (!pq.isEmpty()) {
            int v = pq.poll();
            topo.add(v);
            for (int nxt : dag.get(v)) {
                indeg[nxt]--;
                if (indeg[nxt] == 0)
                    pq.add(nxt);
            }
        }

        for (int idx : topo) {
            List<String> comp = scc.get(idx);
            StringBuilder sb = new StringBuilder();
            for (String x : comp) sb.append(x);
            System.out.println(sb);
        }
    }

    private static void dfs1(String v, Map<String, List<String>> g,
                             Set<String> vis, List<String> order) {
        vis.add(v);
        for (String nx : g.get(v))
            if (!vis.contains(nx))
                dfs1(nx, g, vis, order);
        order.add(v);
    }

    private static void dfs2(String v, Map<String, List<String>> g,
                             Set<String> vis, List<String> comp) {
        vis.add(v);
        comp.add(v);
        for (String nx : g.get(v))
            if (!vis.contains(nx))
                dfs2(nx, g, vis, comp);
    }
}

