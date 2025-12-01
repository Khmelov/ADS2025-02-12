package by.it.group410902.kozincev.lesson13;

import java.util.*;
import java.util.stream.Collectors;

public class GraphC {

    private Map<String, Set<String>> adj;
    private Map<String, Set<String>> revAdj;
    private Set<String> allNodes;
    private Deque<String> stack;
    private Set<String> visited;
    private List<String> sccs;

    public GraphC() {
        adj = new HashMap<>();
        revAdj = new HashMap<>();
        // TreeSet для allNodes обеспечивает детерминированный порядок DFS1
        allNodes = new TreeSet<>();
        stack = new ArrayDeque<>();
        visited = new HashSet<>();
        sccs = new ArrayList<>();
    }

    private void parseGraph(String input) {
        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            if (parts.length == 2) {
                String u = parts[0];
                String v = parts[1];

                allNodes.add(u);
                allNodes.add(v);

                adj.putIfAbsent(u, new HashSet<>());
                revAdj.putIfAbsent(u, new HashSet<>());
                adj.putIfAbsent(v, new HashSet<>());
                revAdj.putIfAbsent(v, new HashSet<>());

                if (adj.get(u).add(v)) {
                    revAdj.get(v).add(u);
                }
            }
        }
    }

    private void dfs1(String u) {
        visited.add(u);
        if (adj.containsKey(u)) {
            for (String v : adj.get(u)) {
                if (!visited.contains(v)) {
                    dfs1(v);
                }
            }
        }
        stack.push(u);
    }

    private void dfs2(String u, TreeSet<String> currentScc) {
        visited.add(u);
        // TreeSet автоматически сортирует вершины лексикографически (требование P.S.)
        currentScc.add(u);

        if (revAdj.containsKey(u)) {
            for (String v : revAdj.get(u)) {
                if (!visited.contains(v)) {
                    dfs2(v, currentScc);
                }
            }
        }
    }

    public String findSccs(String input) {

        adj.clear(); revAdj.clear(); allNodes.clear(); stack.clear(); visited.clear(); sccs.clear();

        // 1. Парсинг графа
        parseGraph(input);

        // 2. Первая DFS на G
        for (String node : allNodes) {
            if (!visited.contains(node)) {
                dfs1(node);
            }
        }

        visited.clear();

        // 3. Вторая DFS на G^T (находит в обратном топологическом порядке)
        while (!stack.isEmpty()) {
            String u = stack.pop();

            if (!visited.contains(u)) {
                TreeSet<String> currentScc = new TreeSet<>();
                dfs2(u, currentScc);

                String sccString = currentScc.stream().collect(Collectors.joining(""));
                sccs.add(sccString);
            }
        }


        // Объединение КСС символом новой строки
        return sccs.stream().collect(Collectors.joining("\n"));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            GraphC graphC = new GraphC();
            String result = graphC.findSccs(input);

            System.out.println(result);
        }

        scanner.close();
    }
}