package by.it.group451001.khomenkov.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        // Чтение входной строки
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Парсинг строки и построение графа
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reversedGraph = new HashMap<>();
        Set<String> allVertices = new HashSet<>();

        // Разделяем по запятым чтобы получить список ребер
        String[] edges = input.split(", ");

        for (String edge : edges) {
            // Разделяем каждое ребро по "->"
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Добавляем обратное ребро в транспонированный граф
            reversedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            // Сохраняем все вершины
            allVertices.add(from);
            allVertices.add(to);
        }

        // Находим компоненты сильной связности
        List<List<String>> scc = kosarajuSCC(graph, reversedGraph, allVertices);

        // Вывод результата
        for (List<String> component : scc) {
            // Сортируем вершины компоненты в лексикографическом порядке
            Collections.sort(component);
            // Объединяем в строку без пробелов
            StringBuilder sb = new StringBuilder();
            for (String vertex : component) {
                sb.append(vertex);
            }
            System.out.println(sb.toString());
        }
    }

    private static List<List<String>> kosarajuSCC(Map<String, List<String>> graph,
                                                  Map<String, List<String>> reversedGraph,
                                                  Set<String> allVertices) {
        List<List<String>> scc = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        // Первый проход DFS - заполняем стек порядком завершения обработки
        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack);
            }
        }

        // Второй проход DFS - на транспонированном графе
        visited.clear();
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, reversedGraph, visited, component);
                scc.add(component);
            }
        }

        return scc;
    }

    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(vertex);

        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfsFirstPass(neighbor, graph, visited, stack);
                }
            }
        }

        stack.push(vertex);
    }

    private static void dfsSecondPass(String vertex, Map<String, List<String>> reversedGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);

        if (reversedGraph.containsKey(vertex)) {
            for (String neighbor : reversedGraph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfsSecondPass(neighbor, reversedGraph, visited, component);
                }
            }
        }
    }
}