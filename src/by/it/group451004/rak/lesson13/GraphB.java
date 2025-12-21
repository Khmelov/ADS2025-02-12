package by.it.group451004.rak.lesson13;

import java.util.*;

public class GraphB {
    private Map<String, List<String>> adj = new HashMap<>();
    private Set<String> nodes = new HashSet<>();

    public void addEdge(String from, String to) {
        adj.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        nodes.add(from);
        nodes.add(to);
    }

    public boolean hasCycle() {
        Map<String, Integer> color = new HashMap<>();

        for (String node : nodes)
            color.put(node, 0);

        for (String node : nodes)
            if (color.get(node) == 0 && dfs(node, color)) //цикл от каждой непосещенной вершины
                return true;

        return false;
    }

    private boolean dfs(String u, Map<String, Integer> color) {
        color.put(u, 1);
        if (adj.containsKey(u)) { //степень исхода больше 0
            for (String v : adj.get(u)) {
                if (color.get(v) == 1) return true;
                if (color.get(v) == 0 && dfs(v, color)) return true;
            }
        }
        color.put(u, 2);
        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        GraphB graph = new GraphB();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            graph.addEdge(parts[0], parts[1]);
        }

        if (graph.hasCycle()) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }
}
