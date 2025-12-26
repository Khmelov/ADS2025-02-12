package by.it.group451004.zarivniak.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        Map<String, List<String>> graph = parseGraph(input);
        List<List<String>> scc = findStronglyConnectedComponents(graph);
        sortSCCsInTopologicalOrder(scc, graph);
        for (List<String> component : scc) {
            Collections.sort(component);
        }
        for (List<String> component : scc) {
            StringBuilder sb = new StringBuilder();
            for (String vertex : component) {
                sb.append(vertex);
            }
            System.out.println(sb.toString());
        }

        scanner.close();
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        input = input.replace(", ", ",").replace(" ,", ",").replace(" ", "");
        String[] edges = input.split(",");

        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0];
            String to = parts[1];
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }

        return graph;
    }

    private static List<List<String>> findStronglyConnectedComponents(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                firstDFS(vertex, graph, visited, stack);
            }
        }

        Map<String, List<String>> transposedGraph = transposeGraph(graph);
        visited.clear();
        List<List<String>> scc = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                secondDFS(vertex, transposedGraph, visited, component);
                scc.add(component);
            }
        }

        return scc;
    }

    private static void firstDFS(String vertex, Map<String, List<String>> graph,
                                 Set<String> visited, Stack<String> stack) {
        visited.add(vertex);

        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                firstDFS(neighbor, graph, visited, stack);
            }
        }

        stack.push(vertex);
    }

    private static void secondDFS(String vertex, Map<String, List<String>> graph,
                                  Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                secondDFS(neighbor, graph, visited, component);
            }
        }
    }

    private static Map<String, List<String>> transposeGraph(Map<String, List<String>> graph) {
        Map<String, List<String>> transposed = new HashMap<>();
        for (String vertex : graph.keySet()) {
            transposed.put(vertex, new ArrayList<>());
        }

        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                transposed.get(to).add(from);
            }
        }

        return transposed;
    }

    private static void sortSCCsInTopologicalOrder(List<List<String>> scc, Map<String, List<String>> graph) {
        Map<String, Integer> vertexToComponent = new HashMap<>();
        for (int i = 0; i < scc.size(); i++) {
            for (String vertex : scc.get(i)) {
                vertexToComponent.put(vertex, i);
            }
        }
        Map<Integer, List<Integer>> condensedGraph = new HashMap<>();
        for (int i = 0; i < scc.size(); i++) {
            condensedGraph.put(i, new ArrayList<>());
        }

        for (String from : graph.keySet()) {
            int fromComponent = vertexToComponent.get(from);
            for (String to : graph.get(from)) {
                int toComponent = vertexToComponent.get(to);
                if (fromComponent != toComponent && !condensedGraph.get(fromComponent).contains(toComponent)) {
                    condensedGraph.get(fromComponent).add(toComponent);
                }
            }
        }
        List<Integer> topologicalOrder = topologicalSort(condensedGraph, scc.size());
        List<List<String>> sortedSCC = new ArrayList<>();
        for (int componentIndex : topologicalOrder) {
            sortedSCC.add(scc.get(componentIndex));
        }

        scc.clear();
        scc.addAll(sortedSCC);
    }

    private static List<Integer> topologicalSort(Map<Integer, List<Integer>> graph, int vertexCount) {
        int[] inDegree = new int[vertexCount];
        for (int from = 0; from < vertexCount; from++) {
            for (int to : graph.get(from)) {
                inDegree[to]++;
            }
        }

        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int i = 0; i < vertexCount; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);

            for (int neighbor : graph.get(node)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        return result;
    }
}