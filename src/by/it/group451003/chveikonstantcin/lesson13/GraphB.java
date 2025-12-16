package by.it.group451003.chveikonstantcin.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0];
            String to = parts[1];

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            allNodes.add(from);
            allNodes.add(to);
        }

        Map<String, Integer> visited = new HashMap<>();
        for (String node : allNodes) {
            visited.put(node, 0);
        }

        boolean hasCycle = false;
        for (String node : allNodes) {
            if (visited.get(node) == 0) {
                if (hasCycleDFS(node, graph, visited)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean hasCycleDFS(String node, Map<String, List<String>> graph, Map<String, Integer> visited) {
        visited.put(node, 1);

        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (visited.get(neighbor) == 0) {
                    if (hasCycleDFS(neighbor, graph, visited)) {
                        return true;
                    }
                } else if (visited.get(neighbor) == 1) {
                    return true;
                }
            }
        }

        visited.put(node, 2);
        return false;
    }
}