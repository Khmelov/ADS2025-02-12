package by.it.group410902.sivtsov.lesson13;

import java.util.*;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = parseGraph(input);

        List<List<String>> scc = findStronglyConnectedComponents(graph);

        printComponentsInOrder(scc, graph);
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();

        if (input == null || input.trim().isEmpty()) {
            return graph;
        }

        String[] edges = input.split(",");

        for (String edge : edges) {
            String cleanedEdge = edge.trim();
            String[] parts = cleanedEdge.split("->");

            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                graph.putIfAbsent(to, new ArrayList<>());
            }
        }

        return graph;
    }

    private static List<List<String>> findStronglyConnectedComponents(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                dfsFirstPass(node, graph, visited, stack);
            }
        }

        Map<String, List<String>> reversedGraph = reverseGraph(graph);

        visited.clear();
        List<List<String>> components = new ArrayList<>();

        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(node, reversedGraph, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private static void dfsFirstPass(String node, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(node);

        for (String neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, stack);
            }
        }

        stack.push(node);
    }

    private static Map<String, List<String>> reverseGraph(Map<String, List<String>> graph) {
        Map<String, List<String>> reversed = new HashMap<>();

        for (String node : graph.keySet()) {
            reversed.putIfAbsent(node, new ArrayList<>());
        }

        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                reversed.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            }
        }

        return reversed;
    }

    private static void dfsSecondPass(String node, Map<String, List<String>> reversedGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);

        for (String neighbor : reversedGraph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, reversedGraph, visited, component);
            }
        }
    }

    private static void printComponentsInOrder(List<List<String>> components, Map<String, List<String>> originalGraph) {
        for (List<String> component : components) {
            Collections.sort(component);
        }

        Map<String, List<String>> nodeToComponent = new HashMap<>();
        for (List<String> component : components) {
            for (String node : component) {
                nodeToComponent.put(node, component);
            }
        }

        Map<List<String>, List<List<String>>> componentGraph = new HashMap<>();
        Map<List<String>, Integer> inDegree = new HashMap<>();

        for (List<String> component : components) {
            componentGraph.put(component, new ArrayList<>());
            inDegree.put(component, 0);
        }

        for (Map.Entry<String, List<String>> entry : originalGraph.entrySet()) {
            String from = entry.getKey();
            List<String> fromComponent = nodeToComponent.get(from);

            for (String to : entry.getValue()) {
                List<String> toComponent = nodeToComponent.get(to);

                if (fromComponent != toComponent && !componentGraph.get(fromComponent).contains(toComponent)) {
                    componentGraph.get(fromComponent).add(toComponent);
                }
            }
        }

        for (List<String> component : components) {
            for (List<String> target : componentGraph.get(component)) {
                inDegree.put(target, inDegree.get(target) + 1);
            }
        }

        List<List<String>> sortedComponents = new ArrayList<>();
        Queue<List<String>> queue = new LinkedList<>();

        for (List<String> component : components) {
            if (inDegree.get(component) == 0) {
                queue.add(component);
            }
        }

        while (!queue.isEmpty()) {
            List<String> current = queue.poll();
            sortedComponents.add(current);

            for (List<String> neighbor : componentGraph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        for (List<String> component : sortedComponents) {
            StringBuilder sb = new StringBuilder();
            for (String node : component) {
                sb.append(node);
            }
            System.out.println(sb.toString());
        }
    }
}
