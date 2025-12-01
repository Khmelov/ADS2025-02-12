package by.it.group410901.zubchonak.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();
        scanner.close();

        Map<String, List<String>> adj = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        if (!line.isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                edge = edge.trim();
                if (edge.contains("->")) {
                    String[] parts = edge.split("->");
                    String from = parts[0].trim();
                    String to = parts[1].trim();
                    adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                    allVertices.add(from);
                    allVertices.add(to);
                }
            }
        }

        for (String v : allVertices) {
            adj.putIfAbsent(v, new ArrayList<>());
        }

        Map<String, Integer> state = new HashMap<>(); // 0 = unvisited, 1 = visiting, 2 = visited
        for (String v : allVertices) {
            state.put(v, 0);
        }

        boolean hasCycle = false;
        for (String v : new TreeSet<>(allVertices)) { // лексикографический порядок для детерминизма (не обязателен, но хорошо)
            if (state.get(v) == 0) {
                if (dfsHasCycle(v, adj, state)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfsHasCycle(String u, Map<String, List<String>> adj, Map<String, Integer> state) {
        state.put(u, 1);
        for (String v : adj.get(u)) {
            int s = state.get(v);
            if (s == 1) {
                return true; // back edge → cycle
            }
            if (s == 0 && dfsHasCycle(v, adj, state)) {
                return true;
            }
        }
        state.put(u, 2);
        return false;
    }
}