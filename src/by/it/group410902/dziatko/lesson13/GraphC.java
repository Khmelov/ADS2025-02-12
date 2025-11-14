package by.it.group410902.dziatko.lesson13;

import java.util.*;

public class GraphC {

    private static Map<String, List<String>> graph = new HashMap<>();
    private static Map<String, List<String>> reverseGraph = new HashMap<>();
    private static Set<String> visited = new HashSet<>();
    private static Deque<String> stack = new ArrayDeque<>();

    private static void dfs1(String node) {
        visited.add(node);
        for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor);
            }
        }
        stack.push(node);
    }

    private static void dfs2(String node, List<String> component) {
        visited.add(node);
        component.add(node);
        for (String neighbor : reverseGraph.getOrDefault(node, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, component);
            }
        }
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

            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            reverseGraph.computeIfAbsent(from, k -> new ArrayList<>());
        }

        visited.clear();
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                dfs1(node);
            }
        }

        visited.clear();
        List<List<String>> sccList = new ArrayList<>();
        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfs2(node, component);
                Collections.sort(component);
                sccList.add(component);
            }
        }
        for (List<String> comp : sccList) {
            StringBuilder sb = new StringBuilder();
            for (String v : comp) sb.append(v);
            System.out.println(sb);
        }
        graph.clear();
        reverseGraph.clear();
        stack.clear();
        visited.clear();
    }
}
