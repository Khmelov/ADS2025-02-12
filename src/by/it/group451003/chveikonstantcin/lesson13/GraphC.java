package by.it.group451003.chveikonstantcin.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            allNodes.add(from);
            allNodes.add(to);
        }

        for (String node : allNodes) {
            graph.putIfAbsent(node, new ArrayList<>());
            reverseGraph.putIfAbsent(node, new ArrayList<>());
        }

        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        List<String> sortedNodes = new ArrayList<>(allNodes);
        Collections.sort(sortedNodes);

        for (String node : sortedNodes) {
            if (!visited.contains(node)) {
                fillOrder(node, graph, visited, stack);
            }
        }

        visited.clear();

        List<String> result = new ArrayList<>();

        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsReverse(node, reverseGraph, visited, component);

                Collections.sort(component);

                StringBuilder sb = new StringBuilder();
                for (String s : component) {
                    sb.append(s);
                }
                result.add(sb.toString());
            }
        }

        for (String component : result) {
            System.out.println(component);
        }
    }

    private static void fillOrder(String node, Map<String, List<String>> graph, Set<String> visited, Stack<String> stack) {
        visited.add(node);
        List<String> neighbors = graph.get(node);

        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                fillOrder(neighbor, graph, visited, stack);
            }
        }
        stack.push(node);
    }

    private static void dfsReverse(String node, Map<String, List<String>> reverseGraph, Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        List<String> neighbors = reverseGraph.get(node);

        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                dfsReverse(neighbor, reverseGraph, visited, component);
            }
        }
    }
}