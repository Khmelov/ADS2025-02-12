package by.it.group410902.bobrovskaya.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>(); // вершина->след вершины
        Set<String> allNodes = new HashSet<>(); // список всех вершин

        String[] edges = input.split(",");
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            allNodes.add(from);
            allNodes.add(to);
        }

        Set<String> visited = new HashSet<>(); // для полностью обработанных вершин
        Set<String> recursionStack = new HashSet<>(); // вершины в цикле, который на данный момент проверяем

        boolean hasCycle = false;
        for (String node : allNodes) {
            if (!visited.contains(node)) {
                if (HasCycle(node, graph, visited, recursionStack)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean HasCycle(String node, Map<String, List<String>> graph, Set<String> visited, Set<String> recursionStack) {
        visited.add(node);
        recursionStack.add(node);

        for (String neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (HasCycle(neighbor, graph, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {
                return true;
            }
        }

        recursionStack.remove(node);
        return false;
    }
}
