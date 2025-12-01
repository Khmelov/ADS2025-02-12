package by.it.group410901.evtuhovskaya.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();

        //граф: вершина -> список смежных вершин
        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            if (parts.length != 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            allNodes.add(from);
            allNodes.add(to);
        }

        //добавляем вершины без исходящих рёбер
        for (String node : allNodes) {
            graph.putIfAbsent(node, new ArrayList<>());
        }

        //проверка на наличие цикла с помощью DFS
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();
        boolean hasCycle = false;

        for (String node : allNodes) {
            if (dfsCycle(node, graph, visited, recStack)) {
                hasCycle = true;
                break;
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfsCycle(String node, Map<String, List<String>> graph,
                                    Set<String> visited, Set<String> recStack) {
        if (recStack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        recStack.add(node);

        for (String neighbor : graph.get(node)) {
            if (dfsCycle(neighbor, graph, visited, recStack))
                return true;
        }

        recStack.remove(node);
        return false;
    }
}