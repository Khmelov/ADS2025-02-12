package by.it.group410902.sulimov.lesson13;

import java.util.*;
import java.util.stream.Collectors;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки и построение графа
        Map<String, List<String>> graph = new HashMap<>();
        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        }

        // Находим все вершины графа
        Set<String> allVertices = new HashSet<>();
        for (String from : graph.keySet()) {
            allVertices.add(from);
            allVertices.addAll(graph.get(from));
        }

        // Алгоритм Косарайю для нахождения компонент сильной связности
        List<Set<String>> scc = kosaraju(graph, allVertices);

        // Сортируем вершины внутри каждой компоненты
        List<List<String>> sortedSCC = scc.stream()
                .map(component -> {
                    List<String> list = new ArrayList<>(component);
                    Collections.sort(list);
                    return list;
                })
                .collect(Collectors.toList());

        // Строим граф конденсации
        Map<String, Integer> vertexToComponent = new HashMap<>();
        for (int i = 0; i < sortedSCC.size(); i++) {
            for (String vertex : sortedSCC.get(i)) {
                vertexToComponent.put(vertex, i);
            }
        }

        // Строим граф между компонентами
        Map<Integer, Set<Integer>> condensationGraph = new HashMap<>();
        int[] inDegree = new int[sortedSCC.size()];

        for (String from : graph.keySet()) {
            int fromComp = vertexToComponent.get(from);
            for (String to : graph.get(from)) {
                int toComp = vertexToComponent.get(to);
                if (fromComp != toComp) {
                    condensationGraph.computeIfAbsent(fromComp, k -> new HashSet<>()).add(toComp);
                }
            }
        }

        // Вычисляем входящие степени для компонент
        for (Set<Integer> neighbors : condensationGraph.values()) {
            for (int neighbor : neighbors) {
                inDegree[neighbor]++;
            }
        }

        // Топологическая сортировка конденсации с использованием PriorityQueue
        // для лексикографического порядка по первой вершине в компоненте
        PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> {
            String firstA = sortedSCC.get(a).get(0);
            String firstB = sortedSCC.get(b).get(0);
            return firstA.compareTo(firstB);
        });

        for (int i = 0; i < sortedSCC.size(); i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            topoOrder.add(current);

            if (condensationGraph.containsKey(current)) {
                for (int neighbor : condensationGraph.get(current)) {
                    inDegree[neighbor]--;
                    if (inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        // Вывод результата
        for (int compIndex : topoOrder) {
            List<String> component = sortedSCC.get(compIndex);
            String componentStr = component.stream().collect(Collectors.joining(""));
            System.out.println(componentStr);
        }
    }

    private static List<Set<String>> kosaraju(Map<String, List<String>> graph, Set<String> allVertices) {
        // Первый проход DFS - получаем порядок выхода
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack);
            }
        }

        // Строим транспонированный граф
        Map<String, List<String>> reversedGraph = new HashMap<>();
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                reversedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
            }
        }

        // Второй проход DFS в обратном порядке
        visited.clear();
        List<Set<String>> scc = new ArrayList<>();

        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                Set<String> component = new HashSet<>();
                dfsSecondPass(vertex, reversedGraph, visited, component);
                scc.add(component);
            }
        }

        return scc;
    }

    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph, Set<String> visited, Stack<String> stack) {
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

    private static void dfsSecondPass(String vertex, Map<String, List<String>> reversedGraph, Set<String> visited, Set<String> component) {
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
