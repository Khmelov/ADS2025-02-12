package by.it.group451003.sorokin.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0];
            String to = parts[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);
        }

        boolean hasCycle = false;
        for (String node : graph.keySet()) {
            if (hasCycleDFS(graph, node, new HashSet<>(), new HashSet<>())) {
                hasCycle = true;
                break;
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycleDFS(Map<String, List<String>> graph, String node,
                                       Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        recursionStack.add(node);

        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (hasCycleDFS(graph, neighbor, visited, recursionStack)) {
                    return true;
                }
            }
        }

        recursionStack.remove(node);
        return false;
    }
}
