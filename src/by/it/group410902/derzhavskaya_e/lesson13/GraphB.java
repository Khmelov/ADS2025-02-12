package by.it.group410902.derzhavskaya_e.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("no");
            return;
        }

        Map<Integer, List<Integer>> graph = new HashMap<>();

        String[] edges = input.split(",");
        for (String e : edges) {
            e = e.trim();
            String[] parts = e.split("->");
            if (parts.length != 2) continue;

            int from = Integer.parseInt(parts[0].trim());
            int to = Integer.parseInt(parts[1].trim());

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.putIfAbsent(to, new ArrayList<>());
        }

        // Проверяем циклы
        boolean hasCycle = containsCycle(graph);
        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean containsCycle(Map<Integer, List<Integer>> graph) {
        // 0 = не посетили, 1 = посещаем, 2 = посетили
        Map<Integer, Integer> state = new HashMap<>();

        for (Integer node : graph.keySet()) {
            if (state.getOrDefault(node, 0) == 0) {
                if (dfs(node, graph, state)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(int node, Map<Integer, List<Integer>> graph, Map<Integer, Integer> state) {
        state.put(node, 1); // посещаем

        for (Integer next : graph.get(node)) {
            int st = state.getOrDefault(next, 0);

            if (st == 1) {
                return true; // цикл
            }

            if (st == 0 && dfs(next, graph, state)) {
                return true;
            }
        }

        state.put(node, 2); // посетили
        return false;
    }
}
