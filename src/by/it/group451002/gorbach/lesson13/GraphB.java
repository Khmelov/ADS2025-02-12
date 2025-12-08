package by.it.group451002.gorbach.lesson13;

import java.util.*;

public class GraphB {

    private Map<String, List<String>> graph;
    private Set<String> visited;
    private Set<String> recursionStack;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        GraphB graphB = new GraphB();
        graphB.parseInput(input);
        boolean hasCycle = graphB.hasCycle();

        System.out.println(hasCycle ? "yes" : "no");
    }

    public void parseInput(String input) {
        graph = new HashMap<>();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0];
            String to = parts[1];

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.putIfAbsent(to, new ArrayList<>());
        }
    }

    public boolean hasCycle() {
        visited = new HashSet<>();
        recursionStack = new HashSet<>();

        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                if (dfs(vertex)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean dfs(String vertex) {
        visited.add(vertex);
        recursionStack.add(vertex);

        for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {
                return true;
            }
        }

        recursionStack.remove(vertex);
        return false;
    }
}