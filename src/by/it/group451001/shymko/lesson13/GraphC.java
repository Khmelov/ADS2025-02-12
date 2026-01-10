package by.it.group451001.shymko.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        // Считываем строку с описанием графа
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        // Создаем граф и обратный граф
        Map<Character, List<Character>> graph = new HashMap<>();
        Map<Character, List<Character>> reverseGraph = new HashMap<>();

        // Разбиваем строку на ребра
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] vertices = edge.split("->");
            char from = vertices[0].charAt(0);
            char to = vertices[1].charAt(0);

            // Добавляем ребро в граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());

            // Добавляем обратное ребро в обратный граф
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            reverseGraph.computeIfAbsent(from, k -> new ArrayList<>());
        }

        // Алгоритм Косарайю для нахождения компонент сильной связности
        List<List<Character>> scc = kosaraju(graph, reverseGraph);

        // Выводим компоненты
        for (List<Character> component : scc) {
            // Сортируем вершины в лексикографическом порядке
            Collections.sort(component);
            // Выводим компонент без пробелов и табуляции
            for (char vertex : component) {
                System.out.print(vertex);
            }
            System.out.println();
        }
    }

    private static List<List<Character>> kosaraju(Map<Character, List<Character>> graph,
                                                  Map<Character, List<Character>> reverseGraph) {
        Set<Character> visited = new HashSet<>();
        Stack<Character> stack = new Stack<>();

        // Первый проход DFS для заполнения стека
        for (char vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(graph, vertex, visited, stack);
            }
        }

        // Второй проход DFS по обратному графу
        visited.clear();
        List<List<Character>> scc = new ArrayList<>();

        while (!stack.isEmpty()) {
            char vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<Character> component = new ArrayList<>();
                dfsSecondPass(reverseGraph, vertex, visited, component);
                scc.add(component);
            }
        }

        return scc;
    }

    private static void dfsFirstPass(Map<Character, List<Character>> graph, char vertex,
                                     Set<Character> visited, Stack<Character> stack) {
        visited.add(vertex);

        for (char neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsFirstPass(graph, neighbor, visited, stack);
            }
        }

        stack.push(vertex);
    }

    private static void dfsSecondPass(Map<Character, List<Character>> graph, char vertex,
                                      Set<Character> visited, List<Character> component) {
        visited.add(vertex);
        component.add(vertex);

        for (char neighbor : graph.getOrDefault(vertex, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                dfsSecondPass(graph, neighbor, visited, component);
            }
        }
    }
}