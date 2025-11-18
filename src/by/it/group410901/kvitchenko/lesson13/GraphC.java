package by.it.group410901.kvitchenko.lesson13;

import java.util.*;

// Класс для нахождения Компонент Сильной Связности (КСС) с использованием алгоритма Косарайю.
public class GraphC {
    // Список смежности исходного графа G.
    // Ключи — вершины 'from', значения — множество их соседей 'to'.
    private final Map<String, Set<String>> adjacencyList = new HashMap<>();

    // Список для хранения найденных компонент сильной связности.
    // Каждая внутренняя List<String> представляет одну КСС.
    private final List<List<String>> strongComponents = new ArrayList<>();

    // Метод возвращает найденные компоненты.
    public List<List<String>> getStrongComponents() { return strongComponents; }

    // Конструктор: создает граф из входной строки и запускает расчет КСС.
    public GraphC(String input) {
        // Разделяем входную строку на отдельные ребра.
        var edges = input.split(", ");
        for (var edge : edges) {
            // Разделяем начало и конец ребра.
            var vertices = edge.split("->");
            var from = vertices[0];
            var to = vertices[1];

            // Добавляем ребро в список смежности исходного графа G.
            // Используем Set для соседей для автоматического исключения дубликатов.
            adjacencyList.computeIfAbsent(from, _1_ -> new HashSet<>()).add(to);

            // Добавляем также конечную вершину, чтобы учесть ее, даже если у нее нет исходящих ребер.
            adjacencyList.putIfAbsent(to, new HashSet<>());
        }

        // Запускаем основной алгоритм поиска КСС (Косарайю).
        calcStrongComponents();
    }

    // Вспомогательный класс для построения обратного графа G_rev и вычисления топологической сортировки.
    private static class ReverseGraph {
        // Список смежности обратного графа G_rev.
        private final Map<String, Set<String>> adjacencyList = new HashMap<>();

        // Список для хранения вершин в порядке завершения DFS (обратный топологический порядок).
        private final List<String> topologicalOrder = new ArrayList<>();

        // Метод возвращает топологический порядок.
        public List<String> getTopologicalOrder() { return topologicalOrder; }

        // Конструктор: строит G_rev из G и вычисляет порядок обхода.
        public ReverseGraph(Map<String, Set<String>> adjacencyList) {
            // Итерация по ребрам исходного графа G.
            for (var entry : adjacencyList.entrySet()) {
                var vertex = entry.getKey();
                // Для каждого ребра (vertex -> neighbor) в G, создаем обратное ребро
                // (neighbor -> vertex) в G_rev.
                for (var neighbor : entry.getValue())
                    this.adjacencyList.computeIfAbsent(neighbor, _1_ -> new HashSet<>()).add(vertex);
            }
            // Вызываем метод для вычисления топологического порядка (Шаг 1 алгоритма Косарайю).
            calcTopologicalSort();
        }

        // Метод для вычисления топологического порядка вершин в G_rev.
        private void calcTopologicalSort() {
            var marked = new HashSet<String>();
            // Обход всех вершин G_rev.
            for (var vertex : adjacencyList.keySet())
                if (!marked.contains(vertex))
                    calcTopologicalSort(vertex, marked);

            // Полученный порядок - это обратный порядок завершения DFS в G_rev.
            // Для алгоритма Косарайю он должен быть инвертирован, чтобы получить правильный порядок обхода для Шага 2.
            Collections.reverse(topologicalOrder);
        }

        // Рекурсивный метод DFS для построения топологической сортировки.
        private void calcTopologicalSort(String vertex, HashSet<String> marked) {
            marked.add(vertex);
            var neighbors = adjacencyList.get(vertex);
            if (neighbors != null)
                for (var neighbor : neighbors)
                    if (!marked.contains(neighbor))
                        calcTopologicalSort(neighbor, marked);

            // Добавляем вершину в список **после** посещения всех ее соседей.
            // Это формирует порядок завершения DFS.
            topologicalOrder.add(vertex);
        }
    }

    // Метод для вычисления компонент сильной связности (Шаг 2 алгоритма Косарайю).
    private void calcStrongComponents() {
        // Шаг 1: Построение G_rev и получение порядка обхода.
        var reverse = new ReverseGraph(adjacencyList);
        var topologicalOrder = reverse.getTopologicalOrder(); // Порядок обхода для Шага 2 (обратный порядок завершения DFS в G_rev).

        var marked = new HashSet<String>();
        // Шаг 2: Обход исходного графа G в порядке, полученном на Шаге 1.
        for (var vertex : topologicalOrder) {
            if (!marked.contains(vertex)) {
                // Если вершина не посещена, начинаем новый DFS:
                // все вершины, достижимые из этой вершины в G, образуют одну КСС.
                List<String> component = new ArrayList<>();
                dfs(vertex, marked, component);
                strongComponents.add(component);
            }
        }

        // Приведение результата в соответствие с требованиями (возможно, для тестирования):
        // Разворот списка КСС.
        Collections.reverse(strongComponents);
        // Сортировка вершин внутри каждой КСС.
        for (var component : strongComponents)
            Collections.sort(component);
    }

    // Рекурсивный метод для DFS в исходном графе G (используется на Шаге 2).
    private void dfs(String vertex, Set<String> marked, List<String> component) {
        marked.add(vertex);
        component.add(vertex);
        var neighbors = adjacencyList.get(vertex);
        if (neighbors != null)
            for (var neighbor : neighbors)
                if (!marked.contains(neighbor))
                    // Продолжаем DFS.
                    dfs(neighbor, marked, component);
    }

    // Главный метод main: считывает граф, вычисляет компоненты и выводит их.
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        // Создаем объект GraphC, который автоматически парсит входную строку и вычисляет КСС.
        var graph = new GraphC(scanner.nextLine());

        var strongComponents = graph.getStrongComponents();
        // Вывод каждой компоненты сильной связности построчно.
        for (var strongComponent : strongComponents) {
            for (var vertex : strongComponent)
                System.out.print(vertex);
            System.out.print('\n');
        }
    }
}