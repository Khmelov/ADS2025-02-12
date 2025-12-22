package by.it.group410902.shahov.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки в граф
        Map<String, List<String>> graph = parseGraph(input);

        // Поиск компонент сильной связности (КСС)
        List<List<String>> scc = findStronglyConnectedComponents(graph);

        // Вывод компонент в топологическом порядке
        printComponentsInOrder(scc, graph);
    }

    /**
     * Парсит строку с описанием графа и возвращает представление в виде списка смежности
     * Формат входной строки: "A->B, B->C, C->A" (ребра разделены запятыми)
     */
    private static Map<String, List<String>> parseGraph(String input) {
        Map<String, List<String>> graph = new HashMap<>();

        if (input == null || input.trim().isEmpty()) {
            return graph;
        }

        // Разделение строки на отдельные ребра по запятым
        String[] edges = input.split(",");

        for (String edge : edges) {
            String cleanedEdge = edge.trim();
            String[] parts = cleanedEdge.split("->");

            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();

                // Добавление ребра в граф
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                // Гарантируем, что конечная вершина тоже есть в графе
                graph.putIfAbsent(to, new ArrayList<>());
            }
        }

        return graph;
    }

    /**
     * Находит компоненты сильной связности (КСС) используя алгоритм Косарайю
     * Алгоритм состоит из двух проходов DFS:
     * 1. Первый проход: обход исходного графа и заполнение стека временами завершения
     * 2. Второй проход: обход транспонированного графа в порядке стека
     */
    private static List<List<String>> findStronglyConnectedComponents(Map<String, List<String>> graph) {
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        // Первый проход DFS: заполнение стека временами завершения обработки вершин
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                dfsFirstPass(node, graph, visited, stack);
            }
        }

        // Транспонирование графа (обращение направления всех ребер)
        Map<String, List<String>> reversedGraph = reverseGraph(graph);

        visited.clear();
        List<List<String>> components = new ArrayList<>();

        // Второй проход DFS: обработка вершин в порядке стека (от больших времен к малым)
        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(node, reversedGraph, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    /**
     * Первый проход DFS для алгоритма Косарайю
     * Рекурсивно обходит граф и добавляет вершины в стек после обработки всех потомков
     */
    private static void dfsFirstPass(String node, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(node);

        // Рекурсивный обход всех соседей
        for (String neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, stack);
            }
        }

        // Добавляем вершину в стек после обработки всех ее потомков
        stack.push(node);
    }

    /**
     * Транспонирует граф (обращает направление всех ребер)
     */
    private static Map<String, List<String>> reverseGraph(Map<String, List<String>> graph) {
        Map<String, List<String>> reversed = new HashMap<>();

        // Инициализация всех вершин в транспонированном графе
        for (String node : graph.keySet()) {
            reversed.putIfAbsent(node, new ArrayList<>());
        }

        // Обращение направления ребер
        for (Map.Entry<String, List<String>> entry : graph.entrySet()) {
            String from = entry.getKey();
            for (String to : entry.getValue()) {
                reversed.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            }
        }

        return reversed;
    }

    /**
     * Второй проход DFS для алгоритма Косарайю
     * Обходит транспонированный граф и собирает вершины в одну компоненту связности
     */
    private static void dfsSecondPass(String node, Map<String, List<String>> reversedGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node); // Добавляем вершину в текущую компоненту

        // Рекурсивный обход в транспонированном графе
        for (String neighbor : reversedGraph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, reversedGraph, visited, component);
            }
        }
    }

    /**
     * Выводит компоненты сильной связности в топологическом порядке
     * Строит граф компонент и выполняет топологическую сортировку
     */
    private static void printComponentsInOrder(List<List<String>> components, Map<String, List<String>> originalGraph) {
        // Сортируем вершины внутри каждой компоненты для единообразия
        for (List<String> component : components) {
            Collections.sort(component);
        }

        // Создаем отображение: вершина -> её компонента
        Map<String, List<String>> nodeToComponent = new HashMap<>();
        for (List<String> component : components) {
            for (String node : component) {
                nodeToComponent.put(node, component);
            }
        }

        // Строим граф компонент (condensation graph)
        Map<List<String>, List<List<String>>> componentGraph = new HashMap<>();
        Map<List<String>, Integer> inDegree = new HashMap<>();

        // Инициализация графа компонент и степеней входа
        for (List<String> component : components) {
            componentGraph.put(component, new ArrayList<>());
            inDegree.put(component, 0);
        }

        // Построение ребер между компонентами
        for (Map.Entry<String, List<String>> entry : originalGraph.entrySet()) {
            String from = entry.getKey();
            List<String> fromComponent = nodeToComponent.get(from);

            for (String to : entry.getValue()) {
                List<String> toComponent = nodeToComponent.get(to);

                // Добавляем ребро между разными компонентами (исключаем петли)
                if (fromComponent != toComponent && !componentGraph.get(fromComponent).contains(toComponent)) {
                    componentGraph.get(fromComponent).add(toComponent);
                }
            }
        }

        // Вычисление степеней входа для компонент
        for (List<String> component : components) {
            for (List<String> target : componentGraph.get(component)) {
                inDegree.put(target, inDegree.get(target) + 1);
            }
        }

        // Топологическая сортировка компонент (алгоритм Кана)
        List<List<String>> sortedComponents = new ArrayList<>();
        Queue<List<String>> queue = new LinkedList<>();

        // Находим компоненты с нулевой степенью входа (источники)
        for (List<String> component : components) {
            if (inDegree.get(component) == 0) {
                queue.add(component);
            }
        }

        // Обработка очереди
        while (!queue.isEmpty()) {
            List<String> current = queue.poll();
            sortedComponents.add(current);

            // Уменьшаем степени входа соседей
            for (List<String> neighbor : componentGraph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Вывод компонент в топологическом порядке
        for (List<String> component : sortedComponents) {
            StringBuilder sb = new StringBuilder();
            for (String node : component) {
                sb.append(node);
            }
            System.out.println(sb.toString());
        }
    }
}
