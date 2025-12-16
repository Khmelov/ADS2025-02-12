package by.it.group410902.grigorev.lesson13;

import java.util.*;
public class GraphC {
    private final Map<String, Set<String>> adjacencyList = new HashMap<>();

    private final List<List<String>> strongComponents = new ArrayList<>();

    public List<List<String>> getStrongComponents() { return strongComponents; }

    public GraphC(String input) {
        var edges = input.split(", ");
        for (var edge : edges) {
            var vertices = edge.split("->");
            var from = vertices[0];
            var to = vertices[1];
            adjacencyList.computeIfAbsent(from, _1_ -> new HashSet<>()).add(to);
        }
        calcStrongComponents();
    }

    private static class ReverseGraph {
        private final Map<String, Set<String>> adjacencyList = new HashMap<>();
        private final List<String> topologicalOrder = new ArrayList<>();
        public List<String> getTopologicalOrder() { return topologicalOrder; }
        public ReverseGraph(Map<String, Set<String>> adjacencyList) {
            for (var entry : adjacencyList.entrySet()) {
                var vertex = entry.getKey();
                for (var neighbor : entry.getValue())
                    this.adjacencyList.computeIfAbsent(neighbor, _1_ -> new HashSet<>()).add(vertex);
            }
            calcTopologicalSort();
        }

        private void calcTopologicalSort() {
            var marked = new HashSet<String>();
            for (var vertex : adjacencyList.keySet())
                if (!marked.contains(vertex))
                    calcTopologicalSort(vertex, marked);
            Collections.reverse(topologicalOrder);
        }

        private void calcTopologicalSort(String vertex, HashSet<String> marked) {
            marked.add(vertex);
            var neighbors = adjacencyList.get(vertex);
            if (neighbors != null)
                for (var neighbor : neighbors)
                    if (!marked.contains(neighbor))
                        calcTopologicalSort(neighbor, marked);
            topologicalOrder.add(vertex);
        }
    }

    private void calcStrongComponents() {
        var reverse = new ReverseGraph(adjacencyList);
        var topologicalOrder = reverse.getTopologicalOrder();

        var marked = new HashSet<String>();
        for (var vertex : topologicalOrder) {
            if (!marked.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfs(vertex, marked, component);
                strongComponents.add(component);
            }
        }
        Collections.reverse(strongComponents);
        for (var component : strongComponents)
            Collections.sort(component);
    }

    // Рекурсивный метод для DFS в исходном графе
    private void dfs(String vertex, Set<String> marked, List<String> component) {
        marked.add(vertex);
        component.add(vertex);
        var neighbors = adjacencyList.get(vertex);
        if (neighbors != null)
            for (var neighbor : neighbors)
                if (!marked.contains(neighbor))
                    dfs(neighbor, marked, component);
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var graph = new GraphC(scanner.nextLine());

        var strongComponents = graph.getStrongComponents();
        for (var strongComponent : strongComponents) {
            for (var vertex : strongComponent)
                System.out.print(vertex);
            System.out.print('\n');
        }
    }
}

