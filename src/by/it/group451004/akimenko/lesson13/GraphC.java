package lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг рёбер графа
        String[] edges = input.split(", ");
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reversedGraph = new HashMap<>();

        // Построение графа и обратного графа
        for (String edge : edges) {
            String[] vertices = edge.split("->");
            String from = vertices[0];
            String to = vertices[1];

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reversedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            // Убедимся, что все вершины присутствуют в обоих графах
            graph.putIfAbsent(to, new ArrayList<>());
            reversedGraph.putIfAbsent(from, new ArrayList<>());
        }

        // Собираем все вершины и сортируем их
        List<String> allVertices = new ArrayList<>(graph.keySet());
        Collections.sort(allVertices);

        // Первый DFS для получения порядка выхода
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                dfsFirst(vertex, graph, visited, stack);
            }
        }

        // Второй DFS на обратном графе
        visited.clear();
        List<List<String>> components = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsSecond(vertex, reversedGraph, visited, component);
                Collections.sort(component);
                components.add(component);
            }
        }

        // Вывод компонент в порядке от истоков к стокам
        for (List<String> component : components) {
            for (String vertex : component) {
                System.out.print(vertex);
            }
            System.out.println();
        }
    }

    private static void dfsFirst(String vertex, Map<String, List<String>> graph,
                                 Set<String> visited, Stack<String> stack) {
        visited.add(vertex);
        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsFirst(neighbor, graph, visited, stack);
            }
        }
        stack.push(vertex);
    }

    private static void dfsSecond(String vertex, Map<String, List<String>> reversedGraph,
                                  Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);
        for (String neighbor : reversedGraph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsSecond(neighbor, reversedGraph, visited, component);
            }
        }
    }
}
