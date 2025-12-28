package by.it.group410901.korneew.lesson13;
import java.util.*;


import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки и построение графа
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reversedGraph = new HashMap<>();
        Set<String> allVertices = new TreeSet<>(); // TreeSet для автоматической сортировки

        if (!input.isEmpty()) {
            String[] edges = input.split(", ");
            for (String edge : edges) {
                String[] parts = edge.split("->");
                String from = parts[0];
                String to = parts[1];

                // Прямой граф
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                // Обратный граф
                reversedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

                allVertices.add(from);
                allVertices.add(to);
            }
        }

        // Первый проход DFS - получаем порядок выхода
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack);
            }
        }

        // Второй проход DFS по обратному графу
        visited.clear();
        List<List<String>> sccList = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, reversedGraph, visited, component);
                Collections.sort(component); // лексикографическая сортировка
                sccList.add(component);
            }
        }

        // Вывод компонент сильной связности
        for (List<String> component : sccList) {
            StringBuilder sb = new StringBuilder();
            for (String vertex : component) {
                sb.append(vertex);
            }
            System.out.println(sb.toString());
        }
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
