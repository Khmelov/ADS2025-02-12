package by.it.group410902.dziatko.lesson13;

import java.util.*;

public class GraphB {

    private static Map<String, List<String>> graph = new HashMap<>();

    private static boolean hasCycle(String node, Set<String> visiting, Set<String> visited) {
        if (visiting.contains(node)) {
            return true;
        }
        if (visited.contains(node)) {
            return false;
        }

        visiting.add(node);
        for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (hasCycle(neighbor, visiting, visited)) {
                return true;
            }
        }
        visiting.remove(node);
        visited.add(node);
        return false;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        scan.close();

        String[] connections = input.split(",");
        for (String connection : connections) {
            connection = connection.trim();
            String[] parts = connection.split("->");
            if (parts.length != 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }

        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();
        boolean cycle = false;

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (hasCycle(node, visiting, visited)) {
                    cycle = true;
                    break;
                }
            }
        }

        System.out.println(cycle ? "yes" : "no");
        graph.clear();
    }
}
