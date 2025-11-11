package by.it.group410901.konon.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        //граф: вершина -> список смежных вершин
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allNodes = new TreeSet<>(); //для лексикографического порядка

        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            if (parts.length != 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            allNodes.add(from);
            allNodes.add(to);
        }

        //добавляем вершины без исходящих рёбер
        for (String node : allNodes) {
            graph.putIfAbsent(node, new ArrayList<>());
        }

        //обход в глубину для определения порядка выхода
        Set<String> visited = new HashSet<>();
        Deque<String> order = new ArrayDeque<>();

        for (String node : allNodes) {
            if (!visited.contains(node)) {
                dfs1(node, graph, visited, order);
            }
        }

        //транспонирование графа
        Map<String, List<String>> transposed = new HashMap<>();
        for (String node : allNodes) {
            transposed.putIfAbsent(node, new ArrayList<>());
        }
        for (String u : graph.keySet()) {
            for (String v : graph.get(u)) {
                transposed.get(v).add(u);
            }
        }

        //поиск компонент на транспонированном графе
        visited.clear();
        List<List<String>> components = new ArrayList<>();

        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, transposed, visited, comp);
                Collections.sort(comp);
                components.add(comp);
            }
        }

        //сначала "истоки" (первые найденные при обходе в порядке убывания времени выхода)
        for (List<String> comp : components) {
            for (String node : comp) {
                System.out.print(node);
            }
            System.out.println();
        }
    }

    private static void dfs1(String node, Map<String, List<String>> graph,
                             Set<String> visited, Deque<String> order) {
        visited.add(node);
        for (String neigh : graph.get(node)) {
            if (!visited.contains(neigh))
                dfs1(neigh, graph, visited, order);
        }
        order.push(node);
    }

    private static void dfs2(String node, Map<String, List<String>> graph,
                             Set<String> visited, List<String> comp) {
        visited.add(node);
        comp.add(node);
        for (String neigh : graph.get(node)) {
            if (!visited.contains(neigh))
                dfs2(neigh, graph, visited, comp);
        }
    }
}