package by.it.group410901.zaverach.lesson13;

import java.util.*;

public class GraphB {
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

        Map<String, Integer> state = new HashMap<>();
        for (String v : vertices) state.put(v, 0);

        boolean hasCycle = false;
        for (String v : vertices) {
            if (state.get(v) == 0) {
                if (dfsHasCycle(v, graph, state)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfsHasCycle(String v, Map<String, List<String>> graph, Map<String, Integer> state) {
        state.put(v, 1);
        for (String nb : graph.getOrDefault(v, Collections.emptyList())) {
            int s = state.getOrDefault(nb, 0);
            if (s == 1) return true;
            if (s == 0 && dfsHasCycle(nb, graph, state)) return true;
        }
        state.put(v, 2);
        return false;
    }
}
