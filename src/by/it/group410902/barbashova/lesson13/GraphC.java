package by.it.group410902.barbashova.lesson13;

import java.util.*;

public class GraphC {
    Map<String, List<String>> graph = new HashMap<>();
    Map<String, List<String>> reverseGraph = new HashMap<>();

    public static void main(String[] args) {
        GraphC graphC = new GraphC();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        graphC.parseGraph(input);
        graphC.sortAndPrintSC();
    }

    public void parseGraph(String input) {
        String[] edges = input.split(", ");
        for(String edge : edges) {
            String[] vertices = edge.split("->");
            String from = vertices[0].trim();
            String to = vertices[1].trim();
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
            reverseGraph.putIfAbsent(to, new ArrayList<>());
            reverseGraph.get(to).add(from);
        }
    }

    public void dfs1(String vertex, Set<String> visited, Stack<String> stack) {
        visited.add(vertex);
        for(String neighbor: graph.getOrDefault(vertex, new ArrayList<>())) {
            if(!visited.contains(neighbor)) {
                dfs1(neighbor, visited, stack);
            }
        }
        stack.push(vertex);
    }

    public void dfs2(String vertex, Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);
        for(String neighbor: reverseGraph.getOrDefault(vertex, new ArrayList<>())) {
            if(!visited.contains(neighbor)) {
                dfs2(neighbor, visited, component);
            }
        }
    }

    public List<List<String>> findSC() {
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        for(String vertex: graph.keySet()) {
            if(!visited.contains(vertex)) {
                dfs1(vertex, visited, stack);
            }
        }
        List<List<String>> components = new ArrayList<>();
        visited.clear();
        while(!stack.isEmpty()) {
            String vertex = stack.pop();
            if(!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfs2(vertex, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    public void sortAndPrintSC() {
        List<List<String>> components = findSC();

        for (List<String> component : components) {
            Collections.sort(component);
        }

        for (List<String> component : components) {
            String result = String.join("", component);
            System.out.println(result);
        }
    }
}