package by.it.group410901.kvitchenko.lesson13;

import java.util.*;

// Класс для представления ориентированного графа и выполнения топологической сортировки
public class GraphA {
    // Список смежности графа.
    // SortedMap обеспечивает сортировку ключей (вершин 'from') в порядке убывания
    // благодаря Collections.reverseOrder().
    // SortedSet обеспечивает сортировку смежных вершин ('to') также в порядке убывания.
    private final SortedMap<String, SortedSet<String>> adjacencyList = new TreeMap<>(Collections.reverseOrder());

    // Список для хранения результата топологической сортировки.
    // Инициализируется как null, пока сортировка не будет выполнена.
    private List<String> topologicalOrder = null;

    // Флаг, показывающий, содержит ли граф цикл.
    // Цикл делает топологическую сортировку невозможной (для DAG).
    private boolean hasCycle = false;

    // Метод для получения результата топологической сортировки.
    public List<String> getTopologicalOrder() { return topologicalOrder; }

    // Метод для проверки, есть ли цикл в графе.
    public boolean hasCycle() { return hasCycle; }

    // Конструктор: создает граф на основе входной строки,
    // выполняет проверку на цикл и, если цикла нет, выполняет сортировку.
    public GraphA(String input) {
        // Разделяем входную строку на отдельные ребра (например, "A -> B, B -> C").
        var edges = input.split(", ");
        for (var edge : edges) {
            // Разделяем начало и конец ребра (например, "A -> B" на "A" и "B").
            var vertices = edge.split(" -> ");
            var from = vertices[0];
            var to = vertices[1];

            // Добавляем ребро в список смежности.
            // computeIfAbsent: если 'from' еще нет в Map, создает новый TreeSet
            // (отсортированный в обратном порядке) и добавляет 'to' как соседа.
            adjacencyList.computeIfAbsent(from, _1_ -> new TreeSet<>(Collections.reverseOrder())).add(to);

            // Также необходимо убедиться, что вершина 'to' существует в Map, даже если у нее нет исходящих ребер.
            // Это важно для корректной обработки всех вершин графа.
            adjacencyList.putIfAbsent(to, new TreeSet<>(Collections.reverseOrder()));
        }

        // Проверяем граф на наличие циклов.
        checkCycle();

        // Если циклов нет (т.е. это направленный ациклический граф - DAG),
        // выполняем топологическую сортировку.
        if (!hasCycle) {
            topologicalOrder = new ArrayList<>();
            calcTopologicalSort();
        }
    }

    // Метод для проверки наличия цикла в графе с использованием Обхода в Глубину (DFS).
    private void checkCycle() {
        var marked  = new HashSet<String>();  // Хранит все **посещенные** вершины.
        var onStack = new HashSet<String>();  // Хранит вершины в **текущем рекурсивном пути** DFS.

        // Проходим по всем вершинам графа (ключи Map), чтобы учесть несвязанные компоненты.
        for (var vertex : adjacencyList.keySet())
            if (!marked.contains(vertex))
                checkCycle(vertex, marked, onStack);
    }

    // Рекурсивный метод DFS для проверки цикла от текущей вершины.
    private void checkCycle(String vertex, HashSet<String> marked, HashSet<String> onStack) {
        marked.add(vertex);  // Отмечаем вершину как посещенную.
        onStack.add(vertex); // Добавляем вершину в текущий рекурсивный стек пути.

        var neighbors = adjacencyList.get(vertex);
        if (neighbors != null) // Проверяем, есть ли исходящие ребра.
            for (var neighbor : neighbors) {
                if (hasCycle)
                    return; // Если цикл уже найден в другом месте, прекращаем поиск.

                if (!marked.contains(neighbor))
                    // Если сосед не посещен, продолжаем DFS.
                    checkCycle(neighbor, marked, onStack);
                else if (onStack.contains(neighbor))
                    // Если сосед посещен **И** находится в текущем стеке пути (onStack),
                    // это означает, что мы нашли обратное ребро к предку в дереве DFS, т.е. **ЦИКЛ**.
                    hasCycle = true;
            }

        onStack.remove(vertex); // Удаляем вершину из стека, когда все ее потомки обработаны.
    }

    // Метод для выполнения топологической сортировки графа (DFS-подход).
    private void calcTopologicalSort() {
        var marked = new HashSet<String>(); // Хранит посещенные вершины.

        // Проходим по всем вершинам, чтобы обработать все компоненты.
        // Порядок обхода определен TreeMap (обратный порядок).
        for (var vertex : adjacencyList.keySet())
            if (!marked.contains(vertex))
                calcTopologicalSort(vertex, marked);

        // В методе DFS вершины добавляются в список **после** посещения всех их потомков.
        // Это дает **обратный** топологический порядок, поэтому мы его инвертируем.
        Collections.reverse(topologicalOrder);
    }

    // Рекурсивный метод DFS для топологической сортировки от текущей вершины.
    private void calcTopologicalSort(String vertex, HashSet<String> marked) {
        marked.add(vertex); // Отмечаем вершину как посещенную.

        var neighbors = adjacencyList.get(vertex);
        if (neighbors != null) // Проверяем, есть ли исходящие ребра.
            for (var neighbor : neighbors)
                if (!marked.contains(neighbor))
                    // Рекурсивный вызов для всех непосещенных соседей.
                    calcTopologicalSort(neighbor, marked);

        // Добавляем вершину в список только после того, как все ее соседи (и их потомки)
        // были обработаны. Это ключевой шаг топологической сортировки DFS.
        topologicalOrder.add(vertex);
    }

    // Метод main: считывает граф, выполняет сортировку и выводит результат.
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        // Создаем объект GraphA, который автоматически парсит входную строку,
        // проверяет на цикл и выполняет сортировку.
        var graph = new GraphA(scanner.nextLine());

        // Получаем отсортированный список.
        var topologicalOrder = graph.getTopologicalOrder();

        // Выводим результат.
        if (topologicalOrder != null) {
            for (var vertex : topologicalOrder)
                System.out.print(vertex + " ");
        } else {
            // Если topologicalOrder == null, значит был найден цикл.
            System.out.println("Граф содержит цикл, топологическая сортировка невозможна.");
        }
    }
}