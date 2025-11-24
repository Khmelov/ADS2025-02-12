package by.it.group451002.kureichuk.lesson13;


import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входных данных
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reversedGraph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(",\\s*");

            for (String edge : edges) {
                String[] vertices = edge.split("\\s*->\\s*");
                String from = vertices[0].trim();
                String to = vertices[1].trim();

                // Оригинальный граф
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

                // Обратный граф
                reversedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

                allVertices.add(from);
                allVertices.add(to);
            }
        }

        // Поиск компонент сильной связности
        List<List<String>> scc = findStronglyConnectedComponents(graph, reversedGraph, allVertices);

        // Сортировка компонент в порядке от истоков к стокам и вывод
        printStronglyConnectedComponents(scc, graph);
    }

    private static List<List<String>> findStronglyConnectedComponents(
            Map<String, List<String>> graph,
            Map<String, List<String>> reversedGraph,
            Set<String> allVertices) {

        List<List<String>> components = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        // Первый проход DFS для заполнения стека
        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                fillOrder(vertex, graph, visited, stack);
            }
        }

        // Второй проход DFS по обратному графу
        visited.clear();
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsReverse(vertex, reversedGraph, visited, component);
                components.add(component);
            }
        }

        return components;
    }

    private static void fillOrder(String vertex, Map<String, List<String>> graph,
                                  Set<String> visited, Stack<String> stack) {
        visited.add(vertex);

        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    fillOrder(neighbor, graph, visited, stack);
                }
            }
        }
        stack.push(vertex);
    }

    private static void dfsReverse(String vertex, Map<String, List<String>> reversedGraph,
                                   Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        if (reversedGraph.containsKey(vertex)) {
            for (String neighbor : reversedGraph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfsReverse(neighbor, reversedGraph, visited, component);
                }
            }
        }
    }

    private static void printStronglyConnectedComponents(List<List<String>> components,
                                                         Map<String, List<String>> graph) {
        // Создаем отображение вершина -> компонента
        Map<String, Integer> vertexToComponent = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            for (String vertex : components.get(i)) {
                vertexToComponent.put(vertex, i);
            }
        }

        // Строим граф конденсации
        Map<Integer, Set<Integer>> condensationGraph = new HashMap<>();
        int[] inDegree = new int[components.size()];

        for (int i = 0; i < components.size(); i++) {
            condensationGraph.put(i, new HashSet<>());
            for (String vertex : components.get(i)) {
                if (graph.containsKey(vertex)) {
                    for (String neighbor : graph.get(vertex)) {
                        int j = vertexToComponent.get(neighbor);
                        if (i != j) {
                            if (condensationGraph.get(i).add(j)) {
                                inDegree[j]++;
                            }
                        }
                    }
                }
            }
        }

        // Топологическая сортировка графа конденсации
        List<Integer> topologicalOrder = topologicalSort(condensationGraph, inDegree, components.size());

        // Сортируем каждую компоненту лексикографически
        for (List<String> component : components) {
            Collections.sort(component);
        }

        // Выводим компоненты в порядке топологической сортировки
        for (int compIndex : topologicalOrder) {
            List<String> component = components.get(compIndex);
            StringBuilder sb = new StringBuilder();
            for (String vertex : component) {
                sb.append(vertex);
            }
            System.out.println(sb.toString());
        }
    }

    private static List<Integer> topologicalSort(Map<Integer, Set<Integer>> condensationGraph,
                                                 int[] inDegree, int n) {
        List<Integer> result = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        // Добавляем компоненты с нулевой степенью захода
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        while (!queue.isEmpty()) {
            int current = queue.poll();
            result.add(current);

            for (int neighbor : condensationGraph.get(current)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        return result;
    }
}