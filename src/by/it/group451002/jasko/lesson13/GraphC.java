package by.it.group451002.jasko.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки с ребрами графа
        Map<String, List<String>> graph = new HashMap<>();        // Прямой граф
        Map<String, List<String>> reverseGraph = new HashMap<>(); // Обратный граф
        Set<String> allVertices = new HashSet<>();               // Все вершины графа

        // Разбиваем входную строку на отдельные ребра
        String[] edges = input.split(",\\s*");

        // Обрабатываем каждое ребро формата "вершина1 -> вершина2"
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim(); // Начальная вершина
            String to = parts[1].trim();   // Конечная вершина

            // Добавляем вершины в множество
            allVertices.add(from);
            allVertices.add(to);

            // Строим прямой граф: from -> to
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            // Строим обратный граф: to -> from
            reverseGraph.putIfAbsent(to, new ArrayList<>());
            reverseGraph.get(to).add(from);
        }

        // Убеждаемся, что все вершины есть в обоих графах
        for (String vertex : allVertices) {
            graph.putIfAbsent(vertex, new ArrayList<>());
            reverseGraph.putIfAbsent(vertex, new ArrayList<>());
        }

        // Поиск компонент сильной связности алгоритмом Косарайю
        List<List<String>> scc = findStronglyConnectedComponents(graph, reverseGraph, allVertices);

        // Сортируем вершины внутри каждой компоненты лексикографически
        for (List<String> component : scc) {
            Collections.sort(component);
        }

        // Сортируем компоненты в топологическом порядке (истоки -> стоки)
        scc = sortSCCTopologically(scc, graph);

        // Вывод результата - каждая компонента на новой строке
        for (List<String> component : scc) {
            for (String vertex : component) {
                System.out.print(vertex);
            }
            System.out.println();
        }
    }

    private static List<List<String>> findStronglyConnectedComponents(
            Map<String, List<String>> graph,
            Map<String, List<String>> reverseGraph,
            Set<String> allVertices) {

        // Первый проход DFS на прямом графе для получения порядка завершения
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        List<String> vertices = new ArrayList<>(allVertices);
        for (String vertex : vertices) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack);
            }
        }

        // Второй проход DFS на обратном графе для нахождения компонент
        visited.clear();
        List<List<String>> scc = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, reverseGraph, visited, component);
                scc.add(component);
            }
        }

        return scc;
    }

    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(vertex);

        // Рекурсивно обходим всех соседей в прямом графе
        for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, stack);
            }
        }

        // Добавляем вершину в стек после обработки всех соседей
        stack.push(vertex);
    }

    private static void dfsSecondPass(String vertex, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        // Рекурсивно обходим всех соседей в обратном графе
        for (String neighbor : reverseGraph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, reverseGraph, visited, component);
            }
        }
    }

    private static List<List<String>> sortSCCTopologically(List<List<String>> scc, Map<String, List<String>> graph) {
        // Создаем отображение вершин на их компоненты
        Map<String, Integer> vertexToComponent = new HashMap<>();
        for (int i = 0; i < scc.size(); i++) {
            for (String vertex : scc.get(i)) {
                vertexToComponent.put(vertex, i);
            }
        }

        // Подсчитываем количество входящих ребер для каждой компоненты
        int[] inDegree = new int[scc.size()];
        for (int i = 0; i < scc.size(); i++) {
            for (String vertex : scc.get(i)) {
                for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
                    int neighborComponent = vertexToComponent.get(neighbor);
                    // Учитываем только ребра между разными компонентами
                    if (neighborComponent != i) {
                        inDegree[neighborComponent]++;
                    }
                }
            }
        }

        // Топологическая сортировка компонент алгоритмом Кана
        List<List<String>> sortedSCC = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        // Начинаем с компонент без входящих ребер (истоков)
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int componentIndex = queue.poll();
            sortedSCC.add(scc.get(componentIndex));

            // Обновляем входящие степени соседних компонент
            for (String vertex : scc.get(componentIndex)) {
                for (String neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
                    int neighborComponent = vertexToComponent.get(neighbor);
                    if (neighborComponent != componentIndex) {
                        inDegree[neighborComponent]--;
                        if (inDegree[neighborComponent] == 0) {
                            queue.add(neighborComponent);
                        }
                    }
                }
            }
        }

        return sortedSCC;
    }
}