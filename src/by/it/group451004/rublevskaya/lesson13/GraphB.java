package by.it.group451004.rublevskaya.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        scanner.close();

        Map<Integer, List<Integer>> graph = new HashMap<>();
        Set<Integer> allNodes = new HashSet<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(",\\s*");
            for (String edge : edges) {
                String[] parts = edge.split("->");
                if (parts.length != 2) continue;
                int from = Integer.parseInt(parts[0].trim());
                int to = Integer.parseInt(parts[1].trim());

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                allNodes.add(from);
                allNodes.add(to);
            }
        }

        for (int node : allNodes) {
            graph.putIfAbsent(node, new ArrayList<>());
        }

        Map<Integer, Integer> color = new HashMap<>(); // 0 - белый, 1 - серый, 2 - чёрный
        for (int node : allNodes) {
            color.put(node, 0);
        }

        boolean hasCycle = false;
        for (int node : allNodes) {
            if (color.get(node) == 0) {
                if (dfsHasCycle(node, graph, color)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfsHasCycle(int node, Map<Integer, List<Integer>> graph, Map<Integer, Integer> color) {
        color.put(node, 1);
        for (int neighbor : graph.get(node)) {
            if (color.get(neighbor) == 1) {
                return true;
            }
            if (color.get(neighbor) == 0) {
                if (dfsHasCycle(neighbor, graph, color)) {
                    return true;
                }
            }
        }
        color.put(node, 2);
        return false;
    }
}
