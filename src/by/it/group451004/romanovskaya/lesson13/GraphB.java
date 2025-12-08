package by.it.group451004.romanovskaya.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();


        Map<Integer, List<Integer>> graph = new HashMap<>();


        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);

            graph.putIfAbsent(a, new ArrayList<>());
            graph.get(a).add(b);
        }


        if (hasCycle(graph)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }


    public static boolean hasCycle(Map<Integer, List<Integer>> graph) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recStack = new HashSet<>();


        for (Integer node : graph.keySet()) {
            if (dfs(graph, node, visited, recStack)) {
                return true;
            }
        }

        return false;
    }


    private static boolean dfs(Map<Integer, List<Integer>> graph, int node, Set<Integer> visited, Set<Integer> recStack) {

        if (recStack.contains(node)) {
            return true;
        }


        if (visited.contains(node)) {
            return false;
        }


        visited.add(node);
        recStack.add(node);


        if (graph.containsKey(node)) {
            for (int neighbor : graph.get(node)) {
                if (dfs(graph, neighbor, visited, recStack)) {
                    return true;
                }
            }
        }


        recStack.remove(node);
        return false;
    }
}

