package by.it.group410901.bandarzheuskaya.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        // Считываем входную строку с описанием орграфа
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        // Инициализируем прямой и обратный граф как списки смежности
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();

        // Разбиваем входную строку на части (рёбра или изолированные вершины)
        String[] parts = input.split(", ");
        for (String part : parts) {
            // Проверяем, является ли часть ребром (содержит "->")
            if (part.contains("->")) {
                // Разбиваем ребро на источник и приёмник
                String[] edge = part.split("->");
                String from = edge[0].trim();
                String to = edge[1].trim();
                // Добавляем ребро в прямой граф: from -> to
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                // Добавляем ребро в обратный граф: to -> from
                reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
                // Гарантируем, что обе вершины есть в обоих графах
                graph.putIfAbsent(to, new ArrayList<>());
                reverseGraph.putIfAbsent(from, new ArrayList<>());
            } else {
                // Если часть — изолированная вершина, добавляем её в оба графа
                String vertex = part.trim();
                graph.putIfAbsent(vertex, new ArrayList<>());
                reverseGraph.putIfAbsent(vertex, new ArrayList<>());
            }
        }

        // Находим компоненты сильной связности с помощью алгоритма Косарайю
        List<List<String>> scc = kosaraju(graph, reverseGraph);

        // Проверяем, все ли компоненты состоят из одной вершины (ациклический случай)
        boolean isAcyclic = scc.stream().allMatch(component -> component.size() == 1);

        // Если граф ациклический, сортируем компоненты лексикографически
        if (isAcyclic) {
            scc.sort((c1, c2) -> c1.get(0).compareTo(c2.get(0)));
        }

        // Выводим компоненты: вершины в лексикографическом порядке, каждая компонента на новой строке
        for (List<String> component : scc) {
            // Сортируем вершины внутри компоненты лексикографически
            Collections.sort(component);
            StringBuilder sb = new StringBuilder();
            for (String vertex : component) {
                sb.append(vertex);
            }
            System.out.println(sb.toString());
        }
    }


    private static List<List<String>> kosaraju(Map<String, List<String>> graph,
                                               Map<String, List<String>> reverseGraph) {
        Set<String> visited = new HashSet<>();// для отслеживания посещённых вершин
        Stack<String> stack = new Stack<>();// для хранения порядка завершения вершин

        // Первый проход DFS: заполняем стек порядком завершения вершин
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                dfsFirstPass(node, graph, visited, stack);
            }
        }

        // Очищаем visited для второго прохода
        visited.clear();
        List<List<String>> scc = new ArrayList<>();   // Список для хранения компонент сильной связности

        // Второй проход выделяем компоненты сильной связности
        while (!stack.isEmpty()) {
            String node = stack.pop();
            if (!visited.contains(node)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(node, reverseGraph, visited, component);
                scc.add(component);
            }
        }

        // Возвращаем список компонент в топологическом порядке
        return scc;
    }

    // Первый проход определяет порядок завершения вершин
    private static void dfsFirstPass(String current, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        // Помечаем текущую вершину как посещённую
        visited.add(current);
        // Проверяем всех соседей текущей вершины
        if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                if (!visited.contains(neighbor)) {
                    dfsFirstPass(neighbor, graph, visited, stack);
                }
            }
        }
        // Добавляем вершину в стек после обработки всех соседей
        stack.push(current);
    }

    // Второй проход DFS: собирает вершины в компоненту сильной связности
    private static void dfsSecondPass(String current, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, List<String> component) {
        // Помечаем текущую вершину как посещённую и добавляем в компоненту
        visited.add(current);
        component.add(current);
        // Проверяем всех соседей в обратном графе
        if (reverseGraph.containsKey(current)) {
            for (String neighbor : reverseGraph.get(current)) {
                if (!visited.contains(neighbor)) {
                    dfsSecondPass(neighbor, reverseGraph, visited, component);
                }
            }
        }
    }
}