package by.it.group451003.klimintsionak.lesson13;

import java.util.*;

public class GraphC {
    private static void dfsFinish(Map<String, List<String>> graph, String u, Set<String> visited, Stack<String> stack) {
        visited.add(u);
        List<String> neighbors = graph.getOrDefault(u, new ArrayList<>());
        Collections.sort(neighbors);
        for (String v : neighbors) {
            if (!visited.contains(v)) {
                dfsFinish(graph, v, visited, stack);
            }
        }
        stack.push(u);
    }

    private static void dfsSCC(Map<String, List<String>> graph, String u, Set<String> visited, List<String> component) {
        visited.add(u);
        component.add(u);
        List<String> neighbors = graph.getOrDefault(u, new ArrayList<>());
        Collections.sort(neighbors);
        for (String v : neighbors) {
            if (!visited.contains(v)) {
                dfsSCC(graph, v, visited, component);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        Map<String, List<String>> graph = new TreeMap<>();
        Set<String> allNodes = new TreeSet<>();
        String[] edges = line.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String u = parts[0];
            String v = parts[1];
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            allNodes.add(u);
            allNodes.add(v);
        }
        for (String node : allNodes) {
            graph.computeIfAbsent(node, k -> new ArrayList<>());
        }
        Map<String, List<String>> transpose = new TreeMap<>();
        for (String u : allNodes) {
            transpose.computeIfAbsent(u, k -> new ArrayList<>());
        }
        for (String u : graph.keySet()) {
            for (String v : graph.get(u)) {
                transpose.get(v).add(u);
            }
        }
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();
        List<String> nodes = new ArrayList<>(allNodes);
        Collections.sort(nodes);
        for (String node : nodes) {
            if (!visited.contains(node)) {
                dfsFinish(graph, node, visited, stack);
            }
        }
        visited.clear();
        List<StringBuilder> components = new ArrayList<>();
        while (!stack.isEmpty()) {
            String u = stack.pop();
            if (!visited.contains(u)) {
                List<String> component = new ArrayList<>();
                dfsSCC(transpose, u, visited, component);
                Collections.sort(component);
                StringBuilder compSb = new StringBuilder();
                for (String s : component) {
                    compSb.append(s);
                }
                components.add(compSb);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (StringBuilder comp : components) {
            sb.append(comp).append("\n");
        }
        System.out.println(sb.toString().trim());
    }
}