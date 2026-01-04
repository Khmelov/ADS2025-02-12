package by.it.group451002.karbanovich.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        sc.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> rev = new HashMap<>();

        // парсинг формата "C->B, C->I, ..."
        String[] parts = input.split(",");
        for (String part : parts) {
            String[] p = part.trim().split("->");
            String from = p[0].trim();
            String to = p[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            rev.putIfAbsent(from, new ArrayList<>());
            rev.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);     // обычный граф
            rev.get(to).add(from);       // обратный граф
        }

        // 1. Обход графа и создание порядка выхода
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();

        for (String v : graph.keySet()) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, order);
            }
        }

        // 2. Обход в обратном графе в порядке убывания времени выхода
        visited.clear();
        List<List<String>> scc = new ArrayList<>();

        Collections.reverse(order);

        for (String v : order) {
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, rev, visited, comp);
                Collections.sort(comp);      // сортировка внутри КСС
                scc.add(comp);
            }
        }

        // Вывод КСС — каждая на новой строке, без пробелов
        for (List<String> comp : scc) {
            StringBuilder sb = new StringBuilder();
            for (String x : comp) sb.append(x);
            System.out.println(sb.toString());
        }
    }

    // DFS по прямому графу
    private static void dfs1(String v, Map<String, List<String>> g,
                             Set<String> visited, List<String> order) {
        visited.add(v);
        for (String u : g.get(v)) {
            if (!visited.contains(u)) dfs1(u, g, visited, order);
        }
        order.add(v);
    }

    // DFS по обратному графу
    private static void dfs2(String v, Map<String, List<String>> g,
                             Set<String> visited, List<String> comp) {
        visited.add(v);
        comp.add(v);
        for (String u : g.get(v)) {
            if (!visited.contains(u)) dfs2(u, g, visited, comp);
        }
    }
}

