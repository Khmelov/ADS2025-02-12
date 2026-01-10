package by.it.group451002.shandr.lesson13;


import java.util.*;

// считывается строка структуры орграфа
// Затем в консоль выводятся вершины компонент сильной связности
// каждый компонент с новой строки, первый - исток, последний - сток
public class GraphC {
    // Список смежности графа (для исходного направления ребер)
    // Хранит граф в виде списка смежности, где:
    // Ключ - исходная вершина (String)
    // Значение - множество вершин, в которые ведут ребра из данной вершины (Set<String>)
    private final Map<String, Set<String>> adjacencyList = new HashMap<>();

    // Список для хранения найденных компонент сильной связности
    // Каждая компонента сильной связности - это список вершин, которые достижимы друг из друга
    // Вершины внутри одной компоненты образуют максимальный сильно связный подграф
    private final List<List<String>> strongComponents = new ArrayList<>();

    // Метод возвращает найденные компоненты сильной связности
    // Компоненты отсортированы в порядке, соответствующем топологическому порядку компонент в конденсированном графе
    public List<List<String>> getStrongComponents() { return strongComponents; }

    // Конструктор: создает граф из входной строки
    public GraphC(String input) {
        // Разделяем входную строку на отдельные ребра
        // Разделитель ", " разделяет разные направленные ребра графа
        var edges = input.split(", ");

        // Обрабатываем каждое ребро из входной строки
        for (var edge : edges) {
            // Разделяем каждое ребро на начальную и конечную вершины
            var vertices = edge.split("->");
            var from = vertices[0];  // Начальная вершина ребра (источник)
            var to = vertices[1];    // Конечная вершина ребра (цель)

            // Добавляем ребро в список смежности исходного графа
            adjacencyList.computeIfAbsent(from, _1_ -> new HashSet<>()).add(to);
        }

        // После построения графа вычисляем компоненты сильной связности
        calcStrongComponents();
    }

    // Вспомогательный класс для построения обратного графа (транспонированного графа)
    // Обратный граф получается путем изменения направления всех ребер исходного графа
    private static class ReverseGraph {
        // Список смежности обратного графа
        // Ключ - вершина, Значение - множество вершин, из которых есть ребра в данную вершину в исходном графе
        private final Map<String, Set<String>> adjacencyList = new HashMap<>();

        // Список для хранения топологического порядка вершин обратного графа
        private final List<String> topologicalOrder = new ArrayList<>();

        public List<String> getTopologicalOrder() { return topologicalOrder; }

        // Конструктор обратного графа: строит обратный граф на основе исходного
        public ReverseGraph(Map<String, Set<String>> adjacencyList) {
            // Проходим по всем ребрам исходного графа и меняем их направление
            for (var entry : adjacencyList.entrySet()) {
                var vertex = entry.getKey();  // Вершина исходного графа
                // Для каждого соседа вершины в исходном графе
                for (var neighbor : entry.getValue())
                    // В обратном графе добавляем ребро от neighbor к vertex
                    // То есть меняем направление ребра на противоположное
                    this.adjacencyList.computeIfAbsent(neighbor, _1_ -> new HashSet<>()).add(vertex);
            }
            // Вычисляем топологическую сортировку для обратного графа
            calcTopologicalSort();
        }

        // Метод для вычисления топологического порядка вершин обратного графа
        // Использует алгоритм поиска в глубину
        private void calcTopologicalSort() {
            var marked = new HashSet<String>();  // Множество посещенных вершин

            // Обрабатываем все вершины обратного графа
            for (var vertex : adjacencyList.keySet())
                // Если вершина еще не посещена, запускаем DFS от нее
                if (!marked.contains(vertex))
                    calcTopologicalSort(vertex, marked);


            // Разворачиваем полученный порядок, так как при алгоритме поиска в глубину вершины добавляются в обратном порядке
            // В результате получаем топологический порядок для обратного графа
            Collections.reverse(topologicalOrder);
        }

        // Рекурсивный метод для построения топологической сортировки с использованием алгоритм поиска в глубину
        // обрабатывает всех потомков перед добавлением текущей вершины
        private void calcTopologicalSort(String vertex, HashSet<String> marked) {
            // Помечаем вершину как посещенную
            marked.add(vertex);

            // Получаем список соседей в обратном графе
            var neighbors = adjacencyList.get(vertex);

            // Рекурсивно обрабатываем всех непосещенных соседей
            if (neighbors != null)
                for (var neighbor : neighbors)
                    if (!marked.contains(neighbor))
                        calcTopologicalSort(neighbor, marked);

            // Добавляем вершину в список после обработки всех ее потомков
            // Это гарантирует, что все вершины, достижимые из данной, будут обработаны до нее
            topologicalOrder.add(vertex);
        }
    }

    // Метод для вычисления компонент сильной связности с использованием алгоритма Косарайю
    // Алгоритм состоит из двух проходов поиска в глубину:
    // 1. поиск в глубину на обратном графе для получения порядка обработки вершин
    // 2. поиск в глубину на исходном графе в порядке, полученном на первом шаге
    private void calcStrongComponents() {
        // Шаг 1: Строим обратный граф и получаем топологический порядок его вершин
        var reverse = new ReverseGraph(adjacencyList);
        var topologicalOrder = reverse.getTopologicalOrder();

        // Шаг 2: Выполняем поиск в глубину на исходном графе в порядке, полученном из обратного графа
        var marked = new HashSet<String>();  // Множество вершин, уже вошедших в компоненты

        // Обрабатываем вершины в порядке, полученном из обратного графа
        for (var vertex : topologicalOrder) {
            // Если вершина еще не назначена ни в одну компоненту
            if (!marked.contains(vertex)) {
                // Создаем новую компоненту сильной связности
                List<String> component = new ArrayList<>();

                // Выполняем поиск в глубину из текущей вершины в исходном графе
                // Все вершины, достижимые из данной, образуют одну компоненту сильной связности
                dfs(vertex, marked, component);

                // Добавляем найденную компоненту в общий список
                strongComponents.add(component);
            }
        }

        // Разворачиваем список компонент, чтобы они шли в правильном топологическом порядке
        // Компоненты с меньшими номерами (в конденсированном графе) будут первыми
        Collections.reverse(strongComponents);

        // Сортируем вершины внутри каждой компоненты в лексикографическом порядке
        for (var component : strongComponents)
            Collections.sort(component);
    }

    // Рекурсивный метод для поиска в глубину в исходном графе
    // Находит все вершины, достижимые из данной вершины в исходном графе
    // Эти вершины образуют одну компоненту сильной связности
    private void dfs(String vertex, Set<String> marked, List<String> component) {
        // Помечаем вершину как посещенную и добавляем в текущую компоненту
        marked.add(vertex);
        component.add(vertex);

        // Получаем список соседей текущей вершины в исходном графе
        var neighbors = adjacencyList.get(vertex);

        // Рекурсивно обрабатываем всех непосещенных соседей
        if (neighbors != null)
            for (var neighbor : neighbors)
                if (!marked.contains(neighbor))
                    dfs(neighbor, marked, component);
    }


    // Главный метод main: считывает граф, вычисляет компоненты и выводит их
    // Каждая компонента сильной связности выводится на отдельной строке
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        // Считываем строку с описанием графа из стандартного ввода
        var graph = new GraphC(scanner.nextLine());

        // Получаем список компонент сильной связности
        var strongComponents = graph.getStrongComponents();

        // Выводим каждую компоненту на отдельной строке
        for (var strongComponent : strongComponents) {
            // Выводим все вершины компоненты подряд без разделителей
            for (var vertex : strongComponent)
                System.out.print(vertex);
            System.out.print('\n');  // Переход на новую строку после каждой компоненты
        }
    }
}



