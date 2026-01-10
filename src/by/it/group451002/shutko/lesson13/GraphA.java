package by.it.group451002.shutko.lesson13;

import java.util.*;

// считывается строка структуры орграфа
// Затем в консоль выводится его топологическая сортировка
public class GraphA {
    // Список смежности графа, ключи сортируются в порядке убывания
    // Ключ - вершина графа, значение - множество смежных вершин
    private final SortedMap<String, SortedSet<String>> adjacencyList = new TreeMap<>(Collections.reverseOrder());

    // Список для хранения результата топологической сортировки
    private List<String> topologicalOrder = null;

    // Флаг, показывающий, содержит ли граф цикл
    // Если граф содержит цикл, топологическая сортировка невозможна
    private boolean hasCycle = false;

    // Метод для получения результата топологической сортировки
    // Возвращает null если граф содержит цикл
    public List<String> getTopologicalOrder() { return topologicalOrder; }

    // Метод для проверки, есть ли цикл в графе
    public boolean hasCycle() { return hasCycle; }

    // Конструктор: создает граф на основе входной строки
    public GraphA(String input) {
        // Разделяем строки для каждой пары вершин (ребра)
        var edges = input.split(", ");

        // Обрабатываем каждое ребро из входной строки
        for (var edge : edges) {
            // Разделяем начало и конец ребра
            var vertices = edge.split(" -> ");
            var from = vertices[0];  // Начальная вершина ребра
            var to = vertices[1];    // Конечная вершина ребра

            // Добавляем ребро в список смежности
            // computeIfAbsent создает новую запись если ключ отсутствует
            adjacencyList.computeIfAbsent(from, _1_ -> new TreeSet<>(Collections.reverseOrder())).add(to);
        }

        // Проверяем граф на наличие циклов перед выполнением топологической сортировки
        checkCycle();

        // Если циклов нет, выполняем топологическую сортировку
        if (!hasCycle) {
            topologicalOrder = new ArrayList<>();
            calcTopologicalSort();
        }
    }

    // Метод для проверки наличия цикла в графе с использованием обхода в глубину
    // Алгоритм использует два множества: marked (посещенные) и onStack (вершины текущего пути)
    private void checkCycle() {
        var marked  = new HashSet<String>();  // Хранит все посещенные вершины
        var onStack = new HashSet<String>();  // Хранит вершины текущего пути рекурсии

        // Проверяем каждую вершину графа на наличие циклов
        // Используем итерацию по всем вершинам чтобы охватить все компоненты связности
        for (var vertex : adjacencyList.keySet())
            if (!marked.contains(vertex))
                checkCycle(vertex, marked, onStack);
    }

    // Рекурсивный метод для проверки цикла от текущей вершины
    private void checkCycle(String vertex, HashSet<String> marked, HashSet<String> onStack) {
        // Помечаем вершину как посещенную
        marked.add(vertex);

        // Добавляем вершину в текущий стек (текущий путь обхода)
        onStack.add(vertex);

        // Получаем список смежных вершин для текущей вершины
        var neighbors = adjacencyList.get(vertex);

        // Если у вершины есть соседи, обрабатываем их
        if (neighbors != null)
            for (var neighbor : neighbors) {
                // Если цикл уже найден, прерываем выполнение
                if (hasCycle)
                    return;

                // Если соседняя вершина еще не посещена, рекурсивно проверяем ее
                if (!marked.contains(neighbor))
                    checkCycle(neighbor, marked, onStack);
                    // Если соседняя вершина уже находится в текущем стеке, найден цикл
                else if (onStack.contains(neighbor))
                    hasCycle = true;  // Обнаружен цикл: вершина встречается дважды в одном пути
            }

        // Убираем вершину из текущего стека при возврате из рекурсии
        onStack.remove(vertex);
    }

    // Метод для выполнения топологической сортировки графа
    private void calcTopologicalSort() {
        var marked = new HashSet<String>();  // Множество посещенных вершин

        // Обрабатываем все вершины графа
        for (var vertex : adjacencyList.keySet())
            if (!marked.contains(vertex))
                calcTopologicalSort(vertex, marked);

        // Разворачиваем список, так как вершины добавляются в обратном порядке
        Collections.reverse(topologicalOrder);
    }

    // Рекурсивный метод для топологической сортировки от текущей вершины
    // Выполняет обход в глубину, добавляя вершины в список после обработки всех потомков
    private void calcTopologicalSort(String vertex, HashSet<String> marked) {
        // Помечаем вершину как посещенную
        marked.add(vertex);

        // Получаем список смежных вершин
        var neighbors = adjacencyList.get(vertex);

        // Рекурсивно обрабатываем всех непосещенных соседей
        if (neighbors != null)
            for (var neighbor : neighbors)
                if (!marked.contains(neighbor))
                    calcTopologicalSort(neighbor, marked);

        // Добавляем вершину в список после обработки всех ее потомков
        // Это гарантирует, что все зависимости вершины будут обработаны до нее самой
        topologicalOrder.add(vertex);
    }

    // Метод main: считывает граф, выполняет сортировку и выводит результат
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        // Считываем входную строку с описанием графа
        var graph = new GraphA(scanner.nextLine());

        // Получаем результат топологической сортировки
        var topologicalOrder = graph.getTopologicalOrder();

        // Выводим вершины в порядке топологической сортировки
        for (var vertex : topologicalOrder)
            System.out.print(vertex + " ");
    }
}