package by.it.group410901.danilova.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // Парсинг входной строки
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        Set<String> allVertices = new TreeSet<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(",\\s*");
            for (String edge : edges) {
                String[] parts = edge.split("\\s*->\\s*");
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

                allVertices.add(from);
                allVertices.add(to);
            }
        }

        // Находим компоненты сильной связности
        List<List<String>> scc = kosaraju(graph, reverseGraph, allVertices);

        // Сортируем вершины внутри каждой компоненты
        for (List<String> component : scc) {
            Collections.sort(component);
        }

        // используем топологическую сортировку компонент
        scc = sortSCC(scc, graph);

        // Вывод результата
        for (int i = 0; i < scc.size(); i++) {
            List<String> component = scc.get(i);
            for (String vertex : component) {
                System.out.print(vertex);
            }
            if (i < scc.size() - 1) {
                System.out.println();
            }
        }
    }

    private static List<List<String>> kosaraju(Map<String, List<String>> graph,
                                               Map<String, List<String>> reverseGraph,
                                               Set<String> allVertices) {
        List<List<String>> scc = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        // Первый проход DFS (прямой граф)
        for (String vertex : allVertices) {
            if (!visited.contains(vertex)) {
                dfsFirstPass(vertex, graph, visited, stack);// Заполняем стек в порядке уменьшения времени завершения
            }
        }

        // Второй проход DFS (обратный граф)
        visited.clear();// Сбрасываем метки посещения
        while (!stack.isEmpty()) {
            String vertex = stack.pop(); // Извлекаем вершины в правильном порядке
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, reverseGraph, visited, component);// Начинаем новый DFS, который находит одну SCC
                scc.add(component);
            }
        }

        return scc;
    }

    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(vertex);

        if (graph.containsKey(vertex)) {
            List<String> neighbors = new ArrayList<>(graph.get(vertex));// Копируем и сортируем соседей
            Collections.sort(neighbors);

            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfsFirstPass(neighbor, graph, visited, stack);
                }
            }
        }

        stack.push(vertex); // Добавляем вершину в стек
    }

    private static void dfsSecondPass(String vertex, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);// Вершина принадлежит текущей SCC

        if (reverseGraph.containsKey(vertex)) {
            List<String> neighbors = new ArrayList<>(reverseGraph.get(vertex));// Копируем и сортируем соседей в обратном графе
            Collections.sort(neighbors);

            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfsSecondPass(neighbor, reverseGraph, visited, component);// Рекурсивно продолжаем собирать текущую SCC
                }
            }
        }
    }

    private static List<List<String>> sortSCC(List<List<String>> scc, Map<String, List<String>> graph) {
        // Создаем граф компонент
        Map<List<String>, List<List<String>>> componentGraph = new HashMap<>();
        Map<String, List<String>> vertexToComponent = new HashMap<>();

        // Сопоставляем вершины с их компонентами
        for (List<String> component : scc) {
            for (String vertex : component) {
                vertexToComponent.put(vertex, component);
            }
            componentGraph.put(component, new ArrayList<>());// Каждая SCC становится вершиной в новом графе
        }

        // Строим ребра между компонентами
        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                List<String> fromComp = vertexToComponent.get(from);
                List<String> toComp = vertexToComponent.get(to);
                // Если ребро соединяет две разные компоненты, добавляем его в граф компонент
                if (fromComp != toComp && !componentGraph.get(fromComp).contains(toComp)) {
                    componentGraph.get(fromComp).add(toComp);
                }
            }
        }

        // Топологическая сортировка компонент
        return topologicalSortSCC(componentGraph, scc);
    }

    private static List<List<String>> topologicalSortSCC(
            Map<List<String>, List<List<String>>> componentGraph,
            List<List<String>> scc) {

        List<List<String>> result = new ArrayList<>();
        Set<List<String>> visited = new HashSet<>();
        Stack<List<String>> stack = new Stack<>();

        for (List<String> component : scc) {// Обходим все SCC
            if (!visited.contains(component)) {
                dfsTopological(component, componentGraph, visited, stack);// заносим SCC в стек в порядке завершения
            }
        }
        // Извлекаем SCC из стека
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        return result;
    }

    private static void dfsTopological(List<String> component,
                                       Map<List<String>, List<List<String>>> graph,
                                       Set<List<String>> visited,
                                       Stack<List<String>> stack) {
        visited.add(component);

        if (graph.containsKey(component)) {
            // Сортируем соседние компоненты
            List<List<String>> neighbors = new ArrayList<>(graph.get(component));
            // Сортировка по первой вершине
            neighbors.sort((a, b) -> a.get(0).compareTo(b.get(0)));

            for (List<String> neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfsTopological(neighbor, graph, visited, stack);
                }
            }
        }
        // Сортировка по первой вершине
        stack.push(component);
    }
}