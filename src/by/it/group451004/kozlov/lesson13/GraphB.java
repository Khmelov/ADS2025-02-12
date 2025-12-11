package by.it.group451004.kozlov.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Пример ввода: 1 -> 2, 1 -> 3, 2 -> 3
        String input = sc.nextLine().trim();

        // Строим список смежности
        Map<String, List<String>> adj = new HashMap<>();
        String[] edges = input.split(",");
        for (String e : edges) {
            e = e.trim();
            String[] parts = e.split("->");
            if (parts.length != 2) continue;
            String from = parts[0].trim();
            String to = parts[1].trim();

            adj.putIfAbsent(from, new ArrayList<>());
            adj.putIfAbsent(to, new ArrayList<>());
            adj.get(from).add(to);
        }

        // Проверка на цикл (DFS)
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        boolean hasCycle = false;
        for (String v : adj.keySet()) {
            if (dfsCycle(v, adj, visited, recStack)) {
                hasCycle = true;
                break;
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }

    private static boolean dfsCycle(String v, Map<String, List<String>> adj,
                                    Set<String> visited, Set<String> recStack) {
        if (recStack.contains(v)) return true;
        if (visited.contains(v)) return false;

        visited.add(v);
        recStack.add(v);

        for (String nei : adj.get(v)) {
            if (dfsCycle(nei, adj, visited, recStack)) return true;
        }

        recStack.remove(v);
        return false;
    }
}
