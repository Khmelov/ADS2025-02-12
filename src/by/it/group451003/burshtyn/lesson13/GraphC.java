package by.it.group451003.burshtyn.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = parseGraph(input);
        Map<String, List<String>> reversedGraph = buildReversedGraph(graph);

        List<List<String>> scc = findStronglyConnectedComponents(graph, reversedGraph);

        for (List<String> component : scc) {
            Collections.sort(component);
            StringBuilder sb = new StringBuilder();
            for (String vertex : component) {
                sb.append(vertex);
            }
            System.out.println(sb);
        }
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();

        if (input.isEmpty()) {
            return graph;
        }

        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());
            graph.get(from).add(to);
        }

        return graph;
    }

    private static Map<String, List<String>> buildReversedGraph(Map<String, List<String>> graph) {
        Map<String, List<String>> reversed = new HashMap<>();

        for (String vertex : graph.keySet()) {
            reversed.put(vertex, new ArrayList<>());
        }

        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                reversed.get(to).add(from);
            }
        }

        return reversed;
    }

    private static List<List<String>> findStronglyConnectedComponents(
            Map<String, List<String>> graph,
            Map<String, List<String>> reversedGraph) {

        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack);
            }
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, reversedGraph, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(vertex);

        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, stack);
            }
        }

        stack.push(vertex);
    }

    private static void dfsSecondPass(String vertex, Map<String, List<String>> reversedGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        for (String neighbor : reversedGraph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, reversedGraph, visited, component);
            }
        }
    }
}