package by.it.group410902.gribach.lesson13;

import java.util.*;

public class GraphA {
    // Список смежности графа, ключи сортируются в порядке убывания
    private final SortedMap<String, SortedSet<String>> adjacencyList = new TreeMap<>(Collections.reverseOrder());

    // Список для хранения результата топологической сортировки
    private List<String> topologicalOrder = null;

    // Флаг, показывающий, содержит ли граф цикл
    private boolean hasCycle = false;

    // Метод для получения результата топологической сортировки
    public List<String> getTopologicalOrder() { return topologicalOrder; }

    // Метод для проверки, есть ли цикл в графе
    public boolean hasCycle() { return hasCycle; }

    // Конструктор: создает граф на основе входной строки
    public GraphA(String input) {
        // Разделяем строки для каждой пары вершин (ребра)
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
        // Если циклов нет, выполняем топологическую сортировку
        if (!hasCycle) {
            topologicalOrder = new ArrayList<>();
            calcTopologicalSort();
        }
    }

    // Метод для проверки наличия цикла в графе (обход в глубину)
    private void checkCycle() {
        var marked  = new HashSet<String>();  // Хранит посещенные вершины
        var onStack = new HashSet<String>();  // Хранит вершины текущего пути
        for (var vertex : adjacencyList.keySet())
            if (!marked.contains(vertex))
                checkCycle(vertex, marked, onStack);
    }

    // Рекурсивный метод для проверки цикла от текущей вершины
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

    // Метод для выполнения топологической сортировки графа
    private void calcTopologicalSort() {
        var marked = new HashSet<String>();
        for (var vertex : adjacencyList.keySet())
            if (!marked.contains(vertex))
                calcTopologicalSort(vertex, marked);
        Collections.reverse(topologicalOrder);
    }

    // Рекурсивный метод для топологической сортировки от текущей вершины
    private void calcTopologicalSort(String vertex, HashSet<String> marked) {
        marked.add(vertex);
        var neighbors = adjacencyList.get(vertex);
        if (neighbors != null)
            for (var neighbor : neighbors)
                if (!marked.contains(neighbor))
                    calcTopologicalSort(neighbor, marked);
        topologicalOrder.add(vertex);
    }

    // Метод main: считывает граф, выполняет сортировку и выводит результат
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var graph = new GraphA(scanner.nextLine());

        var topologicalOrder = graph.getTopologicalOrder();
        for (var vertex : topologicalOrder)
            System.out.print(vertex + " ");
    }
}