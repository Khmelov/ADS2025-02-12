package by.it.group410902.grigorev.lesson13;

import java.util.*;

public class GraphA {

    private final SortedMap<String, SortedSet<String>> adjacencyList = new TreeMap<>(Collections.reverseOrder());

    private List<String> topologicalOrder = null;

    private boolean hasCycle = false;

    public List<String> getTopologicalOrder() { return topologicalOrder; }

    public boolean hasCycle() { return hasCycle; }

    public GraphA(String input) {
        var edges = input.split(", ");
        for (var edge : edges) {
            // Разделяем начало и конец ребра
            var vertices = edge.split(" -> ");
            var from = vertices[0];
            var to = vertices[1];
            // Добавляем ребро в список смежности
            adjacencyList.computeIfAbsent(from, _1_ -> new TreeSet<>(Collections.reverseOrder())).add(to);
        }
        // Проверяем граф на наличие циклов
        checkCycle();
        if (!hasCycle) {
            topologicalOrder = new ArrayList<>();
            calcTopologicalSort();
        }
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

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var graph = new GraphA(scanner.nextLine());

        var topologicalOrder = graph.getTopologicalOrder();
        for (var vertex : topologicalOrder)
            System.out.print(vertex + " ");
    }
}
