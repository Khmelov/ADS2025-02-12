package by.it.group410902.grigorev.lesson13;

import java.util.*;
public class GraphB {
    private final Map<String, List<String>> adjacencyList = new HashMap<>();

    private boolean hasCycle = false;

    public boolean hasCycle() { return hasCycle; }

    public GraphB(String input) {
        var edges = input.split(", ");
        for (var edge : edges) {
            var vertices = edge.split(" -> ");
            var from = vertices[0];
            var to = vertices[1];
            adjacencyList.computeIfAbsent(from, _1_ -> new ArrayList<>()).add(to);
        }
        checkCycle();
    }

    private void checkCycle() {
        var marked  = new HashSet<String>();
        var onStack = new HashSet<String>();
        for (var vertex : adjacencyList.keySet())
            if (!marked.contains(vertex))
                checkCycle(vertex, marked, onStack);
    }

    private void checkCycle(String vertex, HashSet<String> marked, HashSet<String> onStack) {
        marked.add(vertex);
        onStack.add(vertex);
        var neighbors = adjacencyList.get(vertex);
        if (neighbors != null)
            for (var neighbor : neighbors) {
                if (hasCycle)
                    return;

                if (!marked.contains(neighbor))
                    checkCycle(neighbor, marked, onStack);
                else if (onStack.contains(neighbor))
                    hasCycle = true;
            }
        onStack.remove(vertex);
    }

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var graph = new GraphB(scanner.nextLine());
        System.out.print(graph.hasCycle() ? "yes" : "no");
    }
}

