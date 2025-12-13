package by.it.group451002.Osadchy.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.hasNextLine() ? sc.nextLine().trim() : "";
        if (line.isEmpty()) { System.out.println("no"); return; }

        Map<String, Set<String>> adj = new HashMap<>();
        Set<String> vertices = new HashSet<>();
        String[] parts = line.split(",\s*");
        for (String p : parts) {
            String[] lr = p.split("->");
            if (lr.length != 2) continue;
            String from = lr[0].trim();
            String to = lr[1].trim();
            vertices.add(from); vertices.add(to);
            adj.computeIfAbsent(from, k -> new HashSet<>()).add(to);
            adj.computeIfAbsent(to, k -> new HashSet<>());
        }

        Map<String, Integer> color = new HashMap<>(); // 0=white,1=gray,2=black
        for (String v : vertices) color.put(v, 0);

        boolean[] hasCycle = {false};
        for (String v : vertices) {
            if (color.get(v) == 0) dfs(v, adj, color, hasCycle);
            if (hasCycle[0]) break;
        }
        System.out.println(hasCycle[0] ? "yes" : "no");
    }

    private static void dfs(String v, Map<String, Set<String>> adj, Map<String, Integer> color, boolean[] hasCycle) {
        color.put(v, 1);
        for (String w : adj.getOrDefault(v, Collections.emptySet())) {
            int c = color.getOrDefault(w, 0);
            if (c == 0) dfs(w, adj, color, hasCycle);
            else if (c == 1) { hasCycle[0] = true; return; }
            if (hasCycle[0]) return;
        }
        color.put(v, 2);
    }
}