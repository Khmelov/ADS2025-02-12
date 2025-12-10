package by.it.group410902.kavtsevich.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        // Парсим входную строку в структуру графа
        Map<String, List<String>> graph = parseGraph(input);
        // Находим все компоненты сильной связности
        List<List<String>> sccs = kosaraju(graph);
        // Выводим каждую компоненту на отдельной строке
        for (List<String> scc : sccs) {
            System.out.println(String.join("", scc));
        }
        scanner.close();
    }


    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(",\\s*"); // Разделяем по запятым
        for (String edge : edges) {
            String[] vertices = edge.split("\\s*->\\s*"); // Разделяем по стрелке
            String from = vertices[0].trim(); // Вершина-источник
            String to = vertices[1].trim();   // Вершина-назначение

            // Добавляем ребро from -> to
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            // Гарантируем, что вершина 'to' тоже есть в графе
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }
        return graph;
    }

    /**
     * Алгоритм Косарайю для нахождения компонент сильной связности
     * 1. Первый обход DFS для определения порядка обработки
     * 2. Транспонирование графа
     * 3. Второй обход DFS в обратном порядке для нахождения SCC
     */
    private static List<List<String>> kosaraju(Map<String, List<String>> graph) {
        // Первый DFS для получения порядка завершения (обратный топологический порядок)
        Set<String> visited = new HashSet<>(); // Все посещённые вершины
        Deque<String> stack = new ArrayDeque<>(); // Стек для хранения порядка завершения
        for (String vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfsFirst(vertex, graph, visited, stack);
            }
        }

        // Создание транспонированного графа (все рёбра развёрнуты в обратную сторону)
        Map<String, List<String>> transposed = transposeGraph(graph);

        // Второй DFS для нахождения SCC в транспонированном графе
        visited.clear();
        List<List<String>> sccs = new ArrayList<>(); // Список всех компонент сильной связности
        while (!stack.isEmpty()) {
            String vertex = stack.pop(); // Берём вершины в порядке обратном порядку завершения
            if (!visited.contains(vertex)) {
                List<String> scc = new ArrayList<>(); // Новая компонента сильной связности
                dfsSecond(vertex, transposed, visited, scc);
                Collections.sort(scc); // Сортируем вершины компоненты в лексикографическом порядке
                sccs.add(scc);
            }
        }
        return sccs;
    }

    /**
     * Первый обход в глубину для заполнения стека порядком завершения вершин
     * Вершины добавляются в стек после обработки всех их соседей
     */
    private static void dfsFirst(String vertex, Map<String, List<String>> graph,
                                 Set<String> visited, Deque<String> stack) {
        visited.add(vertex);
        // Рекурсивно обходим всех соседей
        for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsFirst(neighbor, graph, visited, stack);
            }
        }
        // После обработки всех соседей добавляем вершину в стек
        stack.push(vertex);
    }

    /**
     * Второй обход в глубину для нахождения компоненты сильной связности
     * Обходит все достижимые вершины в транспонированном графе
     */
    private static void dfsSecond(String vertex, Map<String, List<String>> graph,
                                  Set<String> visited, List<String> scc) {
        visited.add(vertex);
        scc.add(vertex); // Добавляем вершину в текущую компоненту
        // Обходим всех соседей в транспонированном графе
        for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsSecond(neighbor, graph, visited, scc);
            }
        }
    }

    /**
     * Создаёт транспонированный граф (разворачивает все рёбра)
     * Если в исходном графе было ребро A -> B, то в транспонированном будет B -> A
     */
    private static Map<String, List<String>> transposeGraph(Map<String, List<String>> graph) {
        Map<String, List<String>> transposed = new HashMap<>();
        for (String vertex : graph.keySet()) {
            // Гарантируем, что все вершины есть в транспонированном графе
            transposed.computeIfAbsent(vertex, k -> new ArrayList<>());
            // Для каждого ребра vertex -> neighbor создаём обратное ребро neighbor -> vertex
            for (String neighbor : graph.get(vertex)) {
                transposed.computeIfAbsent(neighbor, k -> new ArrayList<>()).add(vertex);
            }
        }
        return transposed;
    }
}