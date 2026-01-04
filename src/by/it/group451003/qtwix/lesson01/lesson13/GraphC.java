package by.it.group451003.qtwix.lesson01.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Map<String, List<String>> graph = parseGraph(input);
        List<List<String>> sccs = kosaraju(graph);
        for (List<String> scc : sccs) {
            System.out.println(String.join("", scc));
        }
        scanner.close();
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] vertices = edge.split("\\s*->\\s*");
            String from = vertices[0].trim();
            String to = vertices[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }
        return graph;
    }

    private static List<List<String>> kosaraju(Map<String, List<String>> graph) {
        // Шаг 1: Первый DFS для получения порядка завершения
        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfsFirst(vertex, graph, visited, stack);
            }
        }

        // Шаг 2: Создание транспонированного графа
        Map<String, List<String>> transposed = transposeGraph(graph);

        // Шаг 3: Второй DFS для нахождения SCC
        visited.clear();
        List<List<String>> sccs = new ArrayList<>();
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> scc = new ArrayList<>();
                dfsSecond(vertex, transposed, visited, scc);
                Collections.sort(scc); // Лексикографический порядок
                sccs.add(scc);
            }
        }
        return sccs;
    }

    private static void dfsFirst(String vertex, Map<String, List<String>> graph,
                                 Set<String> visited, Deque<String> stack) {
        visited.add(vertex);
        for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsFirst(neighbor, graph, visited, stack);
            }
        }
        stack.push(vertex);
    }

    private static void dfsSecond(String vertex, Map<String, List<String>> graph,
                                  Set<String> visited, List<String> scc) {
        visited.add(vertex);
        scc.add(vertex);
        for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsSecond(neighbor, graph, visited, scc);
            }
        }//
    }

    private static Map<String, List<String>> transposeGraph(Map<String, List<String>> graph) {
        Map<String, List<String> > transposed = new HashMap<>();
        for (String vertex : graph.keySet()) {
            transposed.computeIfAbsent(vertex, k -> new ArrayList<>());
            for (String neighbor : graph.get(vertex)) {
                transposed.computeIfAbsent(neighbor, k -> new ArrayList<>()).add(vertex);
            }
        }
        return transposed;
    }
}