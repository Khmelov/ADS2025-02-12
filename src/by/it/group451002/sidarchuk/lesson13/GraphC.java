package by.it.group451002.sidarchuk.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки и построение графа
        Map<Character, List<Character>> graph = new HashMap<>();
        Map<Character, List<Character>> reverseGraph = new HashMap<>();
        Set<Character> vertices = new HashSet<>();

        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            char from = parts[0].charAt(0);
            char to = parts[1].charAt(0);

            // Добавляем ребро в прямой граф
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);

            // Добавляем ребро в обратный граф
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            // Добавляем вершины в множество
            vertices.add(from);
            vertices.add(to);
        }

        // Алгоритм Косарайю для нахождения компонент сильной связности
        List<List<Character>> scc = kosaraju(graph, reverseGraph, vertices);

        // Вывод результатов
        for (List<Character> component : scc) {
            // Сортируем вершины в лексикографическом порядке
            Collections.sort(component);

            // Выводим компоненту без пробелов
            for (char vertex : component) {
                System.out.print(vertex);
            }
            System.out.println();
        }
    }

    private static List<List<Character>> kosaraju(Map<Character, List<Character>> graph,
                                                  Map<Character, List<Character>> reverseGraph,
                                                  Set<Character> vertices) {
        Set<Character> visited = new HashSet<>();
        Stack<Character> stack = new Stack<>();

        // Первый проход DFS для заполнения стека
        for (char vertex : vertices) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(graph, vertex, visited, stack);
            }
        }

        // Второй проход DFS по обратному графу
        visited.clear();
        List<List<Character>> stronglyConnectedComponents = new ArrayList<>();

        while (!stack.isEmpty()) {
            char vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<Character> component = new ArrayList<>();
                dfsSecondPass(reverseGraph, vertex, visited, component);
                stronglyConnectedComponents.add(component);
            }
        }

        return stronglyConnectedComponents;
    }

    private static void dfsFirstPass(Map<Character, List<Character>> graph, char vertex,
                                     Set<Character> visited, Stack<Character> stack) {
        visited.add(vertex);

        if (graph.containsKey(vertex)) {
            for (char neighbor : graph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfsFirstPass(graph, neighbor, visited, stack);
                }
            }
        }

        stack.push(vertex);
    }

    private static void dfsSecondPass(Map<Character, List<Character>> reverseGraph, char vertex,
                                      Set<Character> visited, List<Character> component) {
        visited.add(vertex);
        component.add(vertex);

        if (reverseGraph.containsKey(vertex)) {
            for (char neighbor : reverseGraph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfsSecondPass(reverseGraph, neighbor, visited, component);
                }
            }
        }
    }
}
