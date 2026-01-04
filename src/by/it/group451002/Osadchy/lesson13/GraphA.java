package by.it.group451002.Osadchy.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.hasNextLine() ? sc.nextLine().trim() : "";
        if (line.isEmpty()) return;

        // Parse edges like "A -> B, C -> D"
        Map<String, Set<String>> adj = new HashMap<>();
        Set<String> vertices = new TreeSet<>(); // for lexicographic order

        String[] parts = line.split(",\s*");
        for (String p : parts) {
            String[] lr = p.split("->");
            if (lr.length != 2) continue;
            String from = lr[0].trim();
            String to = lr[1].trim();
            vertices.add(from);
            vertices.add(to);
            adj.computeIfAbsent(from, k -> new TreeSet<>()).add(to);
            adj.computeIfAbsent(to, k -> new TreeSet<>());
        }

        // Kahn's algorithm with lexicographic order
        Map<String, Integer> indeg = new HashMap<>();
        for (String v : vertices) indeg.put(v, 0);
        for (Map.Entry<String, Set<String>> e : adj.entrySet()) {
            for (String w : e.getValue()) indeg.put(w, indeg.get(w) + 1);
        }

        PriorityQueue<String> q = new PriorityQueue<>();
        for (String v : vertices) if (indeg.get(v) == 0) q.add(v);

        List<String> order = new ArrayList<>();
        while (!q.isEmpty()) {
            String v = q.poll();
            order.add(v);
            for (String w : adj.getOrDefault(v, Collections.emptySet())) {
                int d = indeg.merge(w, -1, Integer::sum);
                if (d == 0) q.add(w);
            }
        }

        System.out.println(String.join(" ", order));
    }
}