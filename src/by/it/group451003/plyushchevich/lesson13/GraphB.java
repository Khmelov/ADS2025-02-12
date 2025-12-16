package by.it.group451003.plyushchevich.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextLine()) {
            System.out.println("no");
            return;
        }
        String line = sc.nextLine().trim();
        sc.close();

        if (line.isEmpty()) {
            System.out.println("no");
            return;
        }

        Map<String, Set<String>> adj = new HashMap<>();
        Set<String> nodes = new HashSet<>();

        String[] parts = line.split(",");
        for (String raw : parts) {
            String e = raw.trim();
            if (e.isEmpty()) continue;

            int arrow = e.indexOf("->");
            if (arrow < 0) continue;
            String from = e.substring(0, arrow).trim();
            String to = e.substring(arrow + 2).trim();

            if (from.isEmpty() || to.isEmpty()) continue;

            nodes.add(from);
            nodes.add(to);

            adj.computeIfAbsent(from, k -> new HashSet<>()).add(to);
        }

        if (nodes.isEmpty()) {
            System.out.println("no");
            return;
        }

        Map<String, Integer> indeg = new HashMap<>();
        for (String v : nodes) indeg.put(v, 0);

        for (Map.Entry<String, Set<String>> entry : adj.entrySet()) {
            for (String to : entry.getValue()) {
                indeg.put(to, indeg.getOrDefault(to, 0) + 1);
            }
        }

        ArrayDeque<String> q = new ArrayDeque<>();
        for (String v : nodes) {
            if (indeg.getOrDefault(v, 0) == 0) q.add(v);
        }

        int processed = 0;
        while (!q.isEmpty()) {
            String u = q.poll();
            processed++;

            Set<String> neigh = adj.get(u);
            if (neigh == null) continue;
            for (String v : neigh) {
                indeg.put(v, indeg.get(v) - 1);
                if (indeg.get(v) == 0) q.add(v);
            }
        }

        // если обработано все вершины -> циклов нет
        if (processed == nodes.size()) System.out.println("no");
        else System.out.println("yes");
    }
}
