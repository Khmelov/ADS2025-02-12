package by.it.group451001.suprunovich.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = parseGraph(input);

        List<List<String>> scc = findStronglyConnectedComponents(graph);


        printSCC(scc, graph);
    }

    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();


        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            graph.putIfAbsent(to, new ArrayList<>());
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

        Map<String, List<String>> transposedGraph = transposeGraph(graph);

        visited.clear();
        List<List<String>> scc = new ArrayList<>();

        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(node, transposedGraph, visited, component);
                scc.add(component);
            }
        }

        return scc;
    }

    private static void dfsFirstPass(String node, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(node);
        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, stack);
            }
        }
        stack.push(node);
    }

    private static Map<String, List<String>> transposeGraph(Map<String, List<String>> graph) {
        Map<String, List<String>> transposed = new HashMap<>();

        for (String node : graph.keySet()) {
            transposed.putIfAbsent(node, new ArrayList<>());
        }

        for (String node : graph.keySet()) {
            for (String neighbor : graph.get(node)) {
                transposed.putIfAbsent(neighbor, new ArrayList<>());
                transposed.get(neighbor).add(node);
            }
        }

        return transposed;
    }

    private static void dfsSecondPass(String node, Map<String, List<String>> transposedGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);
        for (String neighbor : transposedGraph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, transposedGraph, visited, component);
            }
        }
    }

    private static void printSCC(List<List<String>> scc, Map<String, List<String>> graph) {
        for (List<String> component : scc) {
            Collections.sort(component);
        }

        Map<List<String>, List<List<String>>> componentGraph = new HashMap<>();
        Map<String, List<String>> nodeToComponent = new HashMap<>();

        for (List<String> component : scc) {
            componentGraph.put(component, new ArrayList<>());
            for (String node : component) {
                nodeToComponent.put(node, component);
            }
        }

        for (List<String> component : scc) {
            for (String node : component) {
                for (String neighbor : graph.get(node)) {
                    List<String> neighborComponent = nodeToComponent.get(neighbor);
                    if (neighborComponent != component &&
                            !componentGraph.get(component).contains(neighborComponent)) {
                        componentGraph.get(component).add(neighborComponent);
                    }
                }
            }
        }

        List<List<String>> sources = new ArrayList<>();
        for (List<String> component : scc) {
            boolean hasIncoming = false;
            for (List<String> otherComponent : scc) {
                if (componentGraph.get(otherComponent).contains(component)) {
                    hasIncoming = true;
                    break;
                }
            }
            if (!hasIncoming) {
                sources.add(component);
            }
        }

        List<List<String>> sortedComponents = topologicalSortComponents(componentGraph, sources);

        for (int i = 0; i < sortedComponents.size(); i++) {
            List<String> component = sortedComponents.get(i);
            for (String node : component) {
                System.out.print(node);
            }
            if (i < sortedComponents.size() - 1) {
                System.out.println();
            }
        }
    }

    private static List<List<String>> topologicalSortComponents(
            Map<List<String>, List<List<String>>> componentGraph,
            List<List<String>> sources) {

        List<List<String>> result = new ArrayList<>();
        Set<List<String>> visited = new HashSet<>();
        Stack<List<String>> stack = new Stack<>();

        for (List<String> source : sources) {
            if (!visited.contains(source)) {
                dfsTopological(source, componentGraph, visited, stack);
            }
        }

        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        return result;
    }

    private static void dfsTopological(List<String> component,
                                       Map<List<String>, List<List<String>>> componentGraph,
                                       Set<List<String>> visited,
                                       Stack<List<String>> stack) {
        visited.add(component);

        for (List<String> neighbor : componentGraph.get(component)) {
            if (!visited.contains(neighbor)) {
                dfsTopological(neighbor, componentGraph, visited, stack);
            }
        }

        stack.push(component);
    }
}