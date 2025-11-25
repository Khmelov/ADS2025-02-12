package by.it.group410901.galitskiy.lesson13;

import java.util.*;
public class GraphB {
    // Список смежности графа, где ключи — вершины, значения — списки соседей
    private final Map<String, List<String>> adjacencyList = new HashMap<>();

    // Флаг, указывающий на наличие цикла в графе
    private boolean hasCycle = false;

    // Метод возвращает, есть ли цикл в графе
    public boolean hasCycle() { return hasCycle; }

    // Конструктор: создает граф из входной строки
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

    // Метод проверки циклов в графе
    private void checkCycle() {
        var marked  = new HashSet<String>();
        var onStack = new HashSet<String>();
        for (var vertex : adjacencyList.keySet())
            if (!marked.contains(vertex))
                checkCycle(vertex, marked, onStack);
    }

    // Рекурсивный метод для поиска циклов от текущей вершины
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

    // Главный метод main: считывает входные данные, создает граф и выводит результат
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var graph = new GraphB(scanner.nextLine());
        System.out.print(graph.hasCycle() ? "yes" : "no");
    }
}
