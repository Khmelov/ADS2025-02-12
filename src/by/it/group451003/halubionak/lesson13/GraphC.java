package by.it.group451003.halubionak.lesson13;

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

        // Разбиваем строку по запятым для получения отдельных рёбер
        String[] edges = input.split(", ");

        for (String edge : edges) {
            // Разбиваем каждое ребро по "->"
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            // Добавляем обратное ребро в обратный граф
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            // Убеждаемся, что все вершины есть в обоих графах
            graph.putIfAbsent(to, new ArrayList<>());
            reverseGraph.putIfAbsent(from, new ArrayList<>());
        }

        // Находим компоненты сильной связности
        List<List<String>> scc = kosarajuSCC(graph, reverseGraph);

        // Выводим результат
        for (List<String> component : scc) {
            // Сортируем вершины компоненты в лексикографическом порядке
            Collections.sort(component);
            // Выводим компоненту без пробелов
            for (String vertex : component) {
                System.out.print(vertex);
            }
            System.out.println();
        }
    }

    private static List<List<String>> kosarajuSCC(Map<String, List<String>> graph,
                                                  Map<String, List<String>> reverseGraph) {
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        // Первый проход DFS для заполнения стека
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                dfsFirstPass(node, graph, visited, stack);
            }
        }

        // Второй проход DFS по обратному графу
        visited.clear();
        List<List<String>> scc = new ArrayList<>();

        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(node, reverseGraph, visited, component);
                scc.add(component);
            }
        }

        return scc;
    }

    private static void dfsFirstPass(String node, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(node);

        for (String neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(neighbor, graph, visited, stack);
            }
        }

        stack.push(node);
    }

    private static void dfsSecondPass(String node, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(node);
        component.add(node);

        for (String neighbor : reverseGraph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(neighbor, reverseGraph, visited, component);
            }
        }
    }
}
