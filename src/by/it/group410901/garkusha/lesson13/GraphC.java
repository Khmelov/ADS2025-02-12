package by.it.group410901.garkusha.lesson13;
import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reversedGraph = new HashMap<>();
        Set<String> allVertices = new TreeSet<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            allVertices.add(from);
            allVertices.add(to);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            reversedGraph.putIfAbsent(to, new ArrayList<>());
            reversedGraph.putIfAbsent(from, new ArrayList<>());
            reversedGraph.get(to).add(from);
        }

        List<List<String>> scc = kosaraju(graph, reversedGraph, allVertices);

        for (List<String> component : scc) {
            Collections.sort(component);
            for (String vertex : component) {
                System.out.print(vertex);
            }
            System.out.println();
        }
    }

    private static List<List<String>> kosaraju(Map<String, List<String>> graph,
                                               Map<String, List<String>> reversedGraph,
                                               Set<String> allVertices) {
        List<List<String>> scc = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();  // Стек для порядка завершения обхода

        // Первый проход DFS - заполняем стек порядком завершения
        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack);
            }
        }

        visited.clear();

        // Второй проход DFS по обратному графу
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, reversedGraph, visited, component);
                scc.add(component);
            }
        }

        return scc;
    }

    private static void dfsFirstPass(String current, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(current);

        if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                if (!visited.contains(neighbor)) {
                    dfsFirstPass(neighbor, graph, visited, stack);
                }
            }
        }

        stack.push(current);
    }

    private static void dfsSecondPass(String current, Map<String, List<String>> reversedGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(current);
        component.add(current);

        if (reversedGraph.containsKey(current)) {
            for (String neighbor : reversedGraph.get(current)) {
                if (!visited.contains(neighbor)) {
                    dfsSecondPass(neighbor, reversedGraph, visited, component);
                }
            }
        }
    }
}
