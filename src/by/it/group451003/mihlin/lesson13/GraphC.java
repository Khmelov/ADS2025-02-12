package by.it.group451003.mihlin.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        // Считываем строку с описанием графа
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсим строку и строим граф
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        Set<String> allVertices = new TreeSet<>(); // TreeSet для автоматической сортировки

        // Разбиваем строку по запятым для получения отдельных рёбер
        String[] edges = input.split(", ");

        for (String edge : edges) {
            // Разделяем вершины по "->"
            String[] vertices = edge.split("->");
            String from = vertices[0].trim();
            String to = vertices[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            // Добавляем обратное ребро в обратный граф
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            // Сохраняем все вершины
            allVertices.add(from);
            allVertices.add(to);
        }

        // Алгоритм Косарайю для нахождения компонент сильной связности
        List<List<String>> scc = kosaraju(graph, reverseGraph, allVertices);

        // Выводим результат в требуемом формате
        for (List<String> component : scc) {
            // Сортируем вершины внутри компоненты лексикографически
            Collections.sort(component);
            // Объединяем вершины в одну строку без пробелов
            StringBuilder sb = new StringBuilder();
            for (String vertex : component) {
                sb.append(vertex);
            }
            System.out.println(sb.toString());
        }
    }

    private static List<List<String>> kosaraju(Map<String, List<String>> graph,
                                               Map<String, List<String>> reverseGraph,
                                               Set<String> allVertices) {
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        List<List<String>> scc = new ArrayList<>();

        // Первый проход DFS - получаем порядок выхода
        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                dfs1(vertex, graph, visited, order);
            }
        }

        // Второй проход DFS по обратному графу в обратном порядке
        visited.clear();
        Collections.reverse(order);

        for (String vertex : order) {
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfs2(vertex, reverseGraph, visited, component);
                scc.add(component);
            }
        }

        return scc;
    }

    private static void dfs1(String current, Map<String, List<String>> graph,
                             Set<String> visited, List<String> order) {
        visited.add(current);

        if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                if (!visited.contains(neighbor)) {
                    dfs1(neighbor, graph, visited, order);
                }
            }
        }

        order.add(current);
    }

    private static void dfs2(String current, Map<String, List<String>> reverseGraph,
                             Set<String> visited, List<String> component) {
        visited.add(current);
        component.add(current);

        if (reverseGraph.containsKey(current)) {
            for (String neighbor : reverseGraph.get(current)) {
                if (!visited.contains(neighbor)) {
                    dfs2(neighbor, reverseGraph, visited, component);
                }
            }
        }
    }
}