package by.it.group410902.bobrovskaya.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>(); //вершина->список вершин смежных
        Map<String, List<String>> reverseGraph = new HashMap<>(); //вершина->список вход вершин
        Set<String> allNodes = new HashSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            allNodes.add(from);
            allNodes.add(to);
        }

        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        List<String> sortedNodes = new ArrayList<>(allNodes); // все вершины
        Collections.sort(sortedNodes); // сортировка по возрастанию
        for (String node : sortedNodes) {
            if (!visited.contains(node)) {
                dfs(node, graph, visited, stack); //обход в глубину
            }
        }

        visited.clear();
        List<List<String>> components = new ArrayList<>(); //для компонентов сильной связности

        while (!stack.isEmpty()) {
            String node = stack.pop(); //извлекаем исток
            if (!visited.contains(node)) { //поиск в глубину по обратному графу
                List<String> component = new ArrayList<>();
                dfsCollect(node, reverseGraph, visited, component);
                Collections.sort(component);
                components.add(component);
            }
        }

        for (List<String> component : components) {
            System.out.println(String.join("", component));
        }
    }

    private static void dfs(String node, Map<String, List<String>> graph,
                            Set<String> visited, Deque<String> stack) {
        visited.add(node);
        for (String neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited, stack);
            }
        }
        stack.push(node);
    }

    private static void dfsCollect(String node, Map<String, List<String>> graph,
                                   Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        for (String neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsCollect(neighbor, graph, visited, component);
            }
        }
    }
}