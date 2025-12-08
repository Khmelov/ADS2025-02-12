package by.it.group410902.gavlev.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        for (String edge : input.split(",")) {
            String[] parts = edge.trim().split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            vertices.add(from);
            vertices.add(to);
        }

        Map<String, Integer> state = new HashMap<>(); // 0=не посещен, 1=в стеке, 2=обработан
        for (String v : vertices) state.put(v, 0);

        boolean hasCycle = false;
        for (String v : vertices) {
            if (state.get(v) == 0 && dfs(v, graph, state)) {
                hasCycle = true;
                break;
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfs(String v, Map<String, List<String>> graph, Map<String, Integer> state) {
        state.put(v, 1);
        if (graph.containsKey(v)) {
            for (String to : graph.get(v)) {
                if (state.get(to) == 1) return true;
                if (state.get(to) == 0 && dfs(to, graph, state)) return true;
            }
        }
        state.put(v, 2);
        return false;
    }
}
