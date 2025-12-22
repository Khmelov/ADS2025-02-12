package by.it.group410902.harkavy.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();

        // граф: список смежности
        Map<String, List<String>> graph = new HashMap<>();
        // множество всех вершин
        Set<String> vertices = new HashSet<>();

        if (!line.isEmpty()) {
            String[] parts = line.split(",");
            for (String part : parts) {
                String edge = part.trim();
                if (edge.isEmpty()) continue;

                String[] uv = edge.split("->");
                if (uv.length != 2) continue;

                String u = uv[0].trim();
                String v = uv[1].trim();

                vertices.add(u);
                vertices.add(v);

                graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            }
        }

        // 0 = не посещён
        // 1 = в рекурсивном стеке
        // 2 = посещён полностью
        Map<String, Integer> state = new HashMap<>();
        for (String v : vertices) state.put(v, 0);

        boolean[] hasCycle = { false };

        // DFS для проверки цикла
        for (String v : vertices) {
            if (state.get(v) == 0) {
                dfs(v, graph, state, hasCycle);
                if (hasCycle[0]) break;
            }
        }

        System.out.println(hasCycle[0] ? "yes" : "no");
    }

    private static void dfs(String v,
                            Map<String, List<String>> graph,
                            Map<String, Integer> state,
                            boolean[] hasCycle) {

        if (hasCycle[0]) return;

        state.put(v, 1); // вершина в рекурсивном стеке

        List<String> next = graph.get(v);
        if (next != null) {
            for (String u : next) {
                int st = state.get(u);

                if (st == 1) {        // цикл найден: ребро в ancestors
                    hasCycle[0] = true;
                    return;
                }

                if (st == 0) {        // продолжаем DFS
                    dfs(u, graph, state, hasCycle);
                    if (hasCycle[0]) return;
                }
            }
        }

        state.put(v, 2); // завершён
    }
}