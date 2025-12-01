package by.it.group410901.kvitchenko.lesson13;

import java.util.*;
// Класс для представления ориентированного графа и проверки наличия в нем циклов
public class GraphB {
    // Список смежности графа.
    // Используется HashMap, что не гарантирует никакого порядка обхода вершин.
    // Ключи — вершины 'from', значения — список их соседей ('to').
    private final Map<String, List<String>> adjacencyList = new HashMap<>();

    // Флаг, указывающий на наличие цикла в графе. Изначально false.
    private boolean hasCycle = false;

    // Метод возвращает, есть ли цикл в графе.
    public boolean hasCycle() { return hasCycle; }

    // Конструктор: создает граф из входной строки и сразу запускает проверку на цикл.
    public GraphB(String input) {
        // Разделяем входную строку на отдельные ребра (например, "A -> B, B -> C").
        var edges = input.split(", ");
        for (var edge : edges) {
            // Разделяем начало и конец ребра (например, "A -> B" на "A" и "B").
            var vertices = edge.split(" -> ");
            var from = vertices[0];
            var to = vertices[1];

            // Добавляем ребро в список смежности.
            // computeIfAbsent: если 'from' еще нет в Map, создает новый ArrayList
            // и добавляет 'to' как соседа.
            adjacencyList.computeIfAbsent(from, _1_ -> new ArrayList<>()).add(to);

            // Также необходимо убедиться, что вершина 'to' существует в Map, даже если у нее нет исходящих ребер.
            // Это гарантирует, что все вершины будут учтены при проверке цикла.
            adjacencyList.putIfAbsent(to, new ArrayList<>());
        }

        // Запускаем проверку графа на наличие циклов.
        checkCycle();
    }

    // Метод проверки циклов в графе с использованием Обхода в Глубину (DFS).
    private void checkCycle() {
        var marked  = new HashSet<String>();  // Хранит все **посещенные** вершины.
        var onStack = new HashSet<String>();  // Хранит вершины в **текущем рекурсивном пути** DFS.

        // Проходим по всем вершинам графа, чтобы учесть несвязанные компоненты.
        for (var vertex : adjacencyList.keySet())
            if (!marked.contains(vertex))
                checkCycle(vertex, marked, onStack);
    }

    // Рекурсивный метод DFS для поиска циклов от текущей вершины.
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

    // Главный метод main: считывает входные данные, создает граф и выводит результат
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        // Создаем объект GraphB, который автоматически парсит входную строку и проверяет на цикл.
        var graph = new GraphB(scanner.nextLine());

        // Выводим "yes", если найден цикл, и "no" в противном случае.
        System.out.print(graph.hasCycle() ? "yes" : "no");
    }
}