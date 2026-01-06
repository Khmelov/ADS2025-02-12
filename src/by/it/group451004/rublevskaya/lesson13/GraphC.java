package by.it.group451004.rublevskaya.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(",\\s*");
            for (String edge : edges) {
                String[] parts = edge.split("->");
                if (parts.length != 2) continue;
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
                allNodes.add(from);
                allNodes.add(to);
            }
        }

        for (String node : allNodes) {
            graph.putIfAbsent(node, new ArrayList<>());
            reverseGraph.putIfAbsent(node, new ArrayList<>());
        }

        Set<String> visited = new HashSet<>();
        List<String> order = new ArrayList<>();

        for (String node : allNodes) {
            if (!visited.contains(node)) {
                dfs1(node, graph, visited, order);
            }
        }

        visited.clear();
        List<List<String>> sccList = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            String node = order.get(i);
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfs2(node, reverseGraph, visited, component);
                Collections.sort(component);
                sccList.add(component);
            }
        }

        for (List<String> comp : sccList) {
            for (String v : comp) {
                System.out.print(v);
            }
            System.out.println();
        }
    }

    private static void dfs1(String node, Map<String, List<String>> graph, Set<String> visited, List<String> order) {
        visited.add(node);
        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor, graph, visited, order);
            }
        }
        order.add(node);
    }

    private static void dfs2(String node, Map<String, List<String>> revGraph, Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        for (String neighbor : revGraph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, revGraph, visited, component);
            }
        }
    }
}
