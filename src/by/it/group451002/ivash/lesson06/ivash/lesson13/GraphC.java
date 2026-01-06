package by.it.group451002.ivash.lesson06.ivash.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        sc.close();

        // Создаем граф
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> nodes = new HashSet<>();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0];
            String to = parts[1];

            nodes.add(from);
            nodes.add(to);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
        }

        // Сортируем соседей для DFS в лексикографическом порядке
        for (List<String> adj : graph.values()) {
            Collections.sort(adj);
        }

        // Первый DFS для получения порядка выхода
        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();
        for (String node : nodes) {
            if (!visited.contains(node)) {
                dfs1(node, graph, visited, order);
            }
        }

        // Транспонируем граф
        Map<String, List<String>> transposed = new HashMap<>();
        for (String node : nodes) {
            transposed.put(node, new ArrayList<>());
        }
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                transposed.get(to).add(from);
            }
        }

        // Сортируем соседей для транспонированного графа
        for (List<String> adj : transposed.values()) {
            Collections.sort(adj);
        }

        // Второй DFS по транспонированному графу в порядке убывания выхода
        visited.clear();
        Collections.reverse(order);
        for (String node : order) {
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfs2(node, transposed, visited, component);
                Collections.sort(component); // лексикографический порядок внутри компоненты
                System.out.println(String.join("", component));
            }
        }
    }

    private static void dfs1(String node, Map<String, List<String>> graph, Set<String> visited, List<String> order) {
        visited.add(node);
        for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor, graph, visited, order);
            }
        }
        order.add(node);
    }

    private static void dfs2(String node, Map<String, List<String>> graph, Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, graph, visited, component);
            }
        }
    }
}
