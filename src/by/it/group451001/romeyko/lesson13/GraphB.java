package by.it.group451001.romeyko.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();

        // adjacency list
        Map<String, List<String>> graph = new HashMap<>();

        // разбор строки
        String[] parts = line.split(",");
        for (String p : parts) {
            String[] lr = p.trim().split("->");
            String from = lr[0].trim();
            String to = lr[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            graph.putIfAbsent(to, new ArrayList<>());
        }

        // множества посещённых и стека рекурсии
        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();

        boolean hasCycle = false;

        for (String v : graph.keySet()) {
            if (!visited.contains(v)) {
                if (dfs(v, graph, visited, stack)) {
                    hasCycle = true;
                    break;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    // DFS для обнаружения цикла
    private static boolean dfs(String v, Map<String, List<String>> graph,
                               Set<String> visited, Set<String> stack) {

        visited.add(v);
        stack.add(v);

        for (String next : graph.get(v)) {
            if (!visited.contains(next)) {
                if (dfs(next, graph, visited, stack)) return true;
            } else if (stack.contains(next)) {
                // Попадание в вершину, которая сейчас в стеке → цикл
                return true;
            }
        }

        stack.remove(v);
        return false;
    }
}
