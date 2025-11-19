package by.it.group410901.zubchonak.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> revGraph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        if (!line.isEmpty()) {
            String[] edges = line.split(",");
            for (String edge : edges) {
                edge = edge.trim();
                if (edge.contains("->")) {
                    String[] parts = edge.split("->");
                    String from = parts[0].trim();
                    String to = parts[1].trim();
                    graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                    revGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
                    allVertices.add(from);
                    allVertices.add(to);
                }
            }
        }

        // Добавляем изолированные вершины
        for (String v : allVertices) {
            graph.putIfAbsent(v, new ArrayList<>());
            revGraph.putIfAbsent(v, new ArrayList<>());
        }

        // Первый DFS — получаем порядок по времени выхода (стек)
        Stack<String> order = new Stack<>();
        Set<String> visited = new HashSet<>();
        for (String v : new TreeSet<>(allVertices)) { // лексикографический порядок для детерминизма
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, order);
            }
        }

        // Второй DFS — на обратном графе, в порядке убывания времени выхода
        visited.clear();
        List<List<String>> sccList = new ArrayList<>();
        while (!order.isEmpty()) {
            String v = order.pop();
            if (!visited.contains(v)) {
                List<String> component = new ArrayList<>();
                dfs2(v, revGraph, visited, component);
                Collections.sort(component); // лексикографический порядок внутри компоненты
                sccList.add(component);
            }
        }

        // Вывод: каждая компонента — новая строка, без пробелов
        for (List<String> comp : sccList) {
            System.out.println(String.join("", comp));
        }
    }

    private static void dfs1(String u, Map<String, List<String>> graph, Set<String> visited, Stack<String> order) {
        visited.add(u);
        for (String v : graph.get(u)) {
            if (!visited.contains(v)) {
                dfs1(v, graph, visited, order);
            }
        }
        order.push(u);
    }

    private static void dfs2(String u, Map<String, List<String>> revGraph, Set<String> visited, List<String> component) {
        visited.add(u);
        component.add(u);
        for (String v : revGraph.get(u)) {
            if (!visited.contains(v)) {
                dfs2(v, revGraph, visited, component);
            }
        }
    }
}