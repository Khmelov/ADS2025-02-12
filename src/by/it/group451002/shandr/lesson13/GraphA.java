package by.it.group451002.shandr.lesson13;

import java.util.*;

/**
 * Класс для выполнения топологической сортировки ориентированного графа
 *
 * Топологическая сортировка - упорядочивание вершин ориентированного графа,
 * при котором для любого ребра (u -> v) вершина u идет перед вершиной v.
 *
 * Особенности:
 * - Работает только с ациклическими ориентированными графами (DAG)
 * - Использует алгоритм на основе поиска в глубину (DFS)
 * - Автоматически проверяет граф на наличие циклов
 */
public class GraphA {
    /**
     * Список смежности графа.
     * Ключи (вершины) хранятся в порядке убывания для удобства обработки.
     * Значения - множества смежных вершин, также отсортированные по убыванию.
     */
    private final SortedMap<String, SortedSet<String>> adjacencyList = new TreeMap<>(Collections.reverseOrder());

    /**
     * Результат топологической сортировки.
     * Список вершин в порядке, удовлетворяющем условиям топологической сортировки.
     */
    private List<String> topologicalOrder = null;

    /**
     * Флаг наличия цикла в графе.
     * Если true - топологическая сортировка невозможна.
     */
    private boolean hasCycle = false;

    /**
     * Возвращает результат топологической сортировки.
     * @return список вершин в топологическом порядке или null если граф содержит цикл
     */
    public List<String> getTopologicalOrder() {
        return topologicalOrder;
    }

    /**
     * Проверяет, содержит ли граф циклы.
     * @return true если граф содержит цикл, иначе false
     */
    public boolean hasCycle() {
        return hasCycle;
    }

    /**
     * Конструктор создает граф на основе входной строки и выполняет топологическую сортировку.
     *
     * Формат входной строки: "A -> B, B -> C, C -> D"
     * Каждая пара вершин разделена " -> ", а ребра разделены ", "
     *
     * @param input строка с описанием графа
     */
    public GraphA(String input) {
        // Разделяем входную строку на отдельные ребра
        var edges = input.split(", ");

        // Обрабатываем каждое ребро для построения списка смежности
        for (var edge : edges) {
            // Разделяем ребро на начальную и конечную вершины
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

    /**
     * Проверяет наличие циклов в графе с использованием алгоритма DFS.
     *
     * Алгоритм:
     * 1. Помечаем вершины как посещенные (marked)
     * 2. Отслеживаем вершины в текущем пути обхода (onStack)
     * 3. Если встречаем вершину, которая уже в текущем пути - найден цикл
     */
    private void checkCycle() {
        var marked  = new HashSet<String>();  // Все посещенные вершины
        var onStack = new HashSet<String>();  // Вершины в текущем пути рекурсии

        // Проверяем каждую вершину графа на наличие циклов
        // Обрабатываем все компоненты связности
        for (var vertex : adjacencyList.keySet())
            if (!marked.contains(vertex))
                checkCycle(vertex, marked, onStack);
    }

    /**
     * Рекурсивно проверяет наличие цикла, начиная с заданной вершины.
     *
     * @param vertex текущая вершина для проверки
     * @param marked множество всех посещенных вершин
     * @param onStack множество вершин в текущем пути обхода
     */
    private void checkCycle(String vertex, HashSet<String> marked, HashSet<String> onStack) {
        // Помечаем вершину как посещенную
        marked.add(vertex);

        // Добавляем вершину в текущий путь обхода
        onStack.add(vertex);

        // Получаем список смежных вершин для текущей вершины
        var neighbors = adjacencyList.get(vertex);

        // Обрабатываем всех соседей текущей вершины
        if (neighbors != null)
            for (var neighbor : neighbors) {
                // Если цикл уже найден, прерываем выполнение
                if (hasCycle)
                    return;

                // Если соседняя вершина еще не посещена, рекурсивно проверяем ее
                if (!marked.contains(neighbor))
                    checkCycle(neighbor, marked, onStack);
                    // Если соседняя вершина уже находится в текущем пути - найден цикл
                else if (onStack.contains(neighbor))
                    hasCycle = true;  // Обнаружен цикл: вершина встречается дважды в одном пути
            }

        // Убираем вершину из текущего пути при возврате из рекурсии
        onStack.remove(vertex);
    }

    /**
     * Выполняет топологическую сортировку графа с использованием DFS.
     *
     * Алгоритм:
     * 1. Выбираем непосещенную вершину
     * 2. Рекурсивно обрабатываем всех ее потомков
     * 3. Добавляем вершину в результат после обработки всех потомков
     * 4. В конце разворачиваем результат
     */
    private void calcTopologicalSort() {
        var marked = new HashSet<String>();  // Множество посещенных вершин

        // Обрабатываем все вершины графа
        for (var vertex : adjacencyList.keySet())
            if (!marked.contains(vertex))
                calcTopologicalSort(vertex, marked);

        // Разворачиваем список, так как вершины добавляются в обратном порядке
        // (после обработки всех потомков)
        Collections.reverse(topologicalOrder);
    }

    /**
     * Рекурсивно выполняет топологическую сортировку, начиная с заданной вершины.
     *
     * @param vertex текущая вершина для обработки
     * @param marked множество посещенных вершин
     */
    private void calcTopologicalSort(String vertex, HashSet<String> marked) {
        // Помечаем вершину как посещенную
        marked.add(vertex);

        // Получаем список смежных вершин
        var neighbors = adjacencyList.get(vertex);

        // Рекурсивно обрабатываем всех непосещенных соседей
        // Это гарантирует, что все зависимости будут обработаны до текущей вершины
        if (neighbors != null)
            for (var neighbor : neighbors)
                if (!marked.contains(neighbor))
                    calcTopologicalSort(neighbor, marked);

        // Добавляем вершину в список после обработки всех ее потомков
        // Принцип: "добавляй вершину когда все исходящие ребра обработаны"
        topologicalOrder.add(vertex);
    }

    /**
     * Точка входа в программу.
     *
     * Пример использования:
     * Вход: "A -> B, B -> C, A -> C"
     * Выход: "A B C" (один из возможных топологических порядков)
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        // Считываем входную строку с описанием графа
        var graph = new GraphA(scanner.nextLine());

        // Получаем результат топологической сортировки
        var topologicalOrder = graph.getTopologicalOrder();

        // Выводим вершины в порядке топологической сортировки
        for (var vertex : topologicalOrder)
            System.out.print(vertex + " ");

        scanner.close();
    }
}