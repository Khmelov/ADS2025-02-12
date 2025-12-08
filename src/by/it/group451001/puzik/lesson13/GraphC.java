package by.it.group451001.puzik.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.hasNextLine() ? sc.nextLine().trim() : "";
        if (line.isEmpty()) return;

        Map<String, Set<String>> adj = new HashMap<>();
        Map<String, Set<String>> radj = new HashMap<>();
        Set<String> vertices = new TreeSet<>();

        String[] parts = line.split(",\s*");
        for (String p : parts) {
            String[] lr = p.split("->");
            if (lr.length != 2) continue;
            String from = lr[0].trim();
            String to = lr[1].trim();
            vertices.add(from); vertices.add(to);
            adj.computeIfAbsent(from, k -> new HashSet<>()).add(to);
            adj.computeIfAbsent(to, k -> new HashSet<>());
            radj.computeIfAbsent(to, k -> new HashSet<>()).add(from);
            radj.computeIfAbsent(from, k -> new HashSet<>());
        }

        // Kosaraju
        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();
        for (String v : vertices) if (!visited.contains(v)) dfs1(v, adj, visited, order);

        visited.clear();
        List<String> lines = new ArrayList<>();
        while (!order.isEmpty()) {
            String v = order.removeLast();
            if (visited.contains(v)) continue;
            List<String> comp = new ArrayList<>();
            dfs2(v, radj, visited, comp);
            Collections.sort(comp);
            lines.add(String.join("", comp));
        }
        System.out.println(String.join("\n", lines));
    }

    private static void dfs1(String v, Map<String, Set<String>> adj, Set<String> visited, Deque<String> order) {
        if (!visited.add(v)) return;
        for (String w : adj.getOrDefault(v, Collections.emptySet())) dfs1(w, adj, visited, order);
        order.addLast(v);
    }

    private static void dfs2(String v, Map<String, Set<String>> radj, Set<String> visited, List<String> comp) {
        if (!visited.add(v)) return;
        comp.add(v);
        for (String w : radj.getOrDefault(v, Collections.emptySet())) dfs2(w, radj, visited, comp);
    }
}


