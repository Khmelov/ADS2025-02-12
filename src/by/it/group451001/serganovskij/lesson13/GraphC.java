package by.it.group451001.serganovskij.lesson13;

import java.util.*;

public class GraphC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine(); // читаем рёбра, например: A->B, B->A, C->D

        // Построение графа и обратного графа
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, List<String>> reverseGraph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        // Разбор рёбер
        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("\\s*->\\s*");
            if (parts.length < 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reverseGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
        }

        // Добавляем изолированные вершины
        for (String vertex : vertices) {
            graph.putIfAbsent(vertex, new ArrayList<>());
            reverseGraph.putIfAbsent(vertex, new ArrayList<>());
        }

        // Находим компоненты сильной связности (алгоритм Косарайю)
        List<List<String>> scc = findStronglyConnectedComponents(graph, reverseGraph, vertices);

        // Сортируем компоненты в топологическом порядке
        sortComponentsByTopologicalOrder(scc, graph, vertices);

        // Выводим результат
        for (List<String> component : scc) {
            Collections.sort(component); // сортировка внутри компоненты
            for (String vertex : component) System.out.print(vertex);
            System.out.println();
        }
    }

    // Алгоритм Косарайю: поиск компонент сильной связности
    private static List<List<String>> findStronglyConnectedComponents(
            Map<String, List<String>> graph,
            Map<String, List<String>> reverseGraph,
            Set<String> vertices) {

        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        // 1-й проход: заполняем стек по времени завершения
        List<String> vertexList = new ArrayList<>(vertices);
        Collections.sort(vertexList);
        for (String vertex : vertexList)
            if (!visited.contains(vertex))
                dfsFirstPass(vertex, graph, visited, stack);

        // 2-й проход по обратному графу
        visited.clear();
        List<List<String>> components = new ArrayList<>();
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfsSecondPass(vertex, reverseGraph, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    // DFS первого прохода
    private static void dfsFirstPass(String vertex, Map<String, List<String>> graph,
                                     Set<String> visited, Stack<String> stack) {
        visited.add(vertex);
        List<String> neighbors = new ArrayList<>(graph.get(vertex));
        Collections.sort(neighbors);
        for (String neighbor : neighbors)
            if (!visited.contains(neighbor))
                dfsFirstPass(neighbor, graph, visited, stack);
        stack.push(vertex);
    }

    // DFS второго прохода (по обратному графу)
    private static void dfsSecondPass(String vertex, Map<String, List<String>> reverseGraph,
                                      Set<String> visited, List<String> component) {
        visited.add(vertex);
        component.add(vertex);
        List<String> neighbors = new ArrayList<>(reverseGraph.get(vertex));
        Collections.sort(neighbors);
        for (String neighbor : neighbors)
            if (!visited.contains(neighbor))
                dfsSecondPass(neighbor, reverseGraph, visited, component);
    }

    // Топологическая сортировка компонент (конденсированный граф)
    private static void sortComponentsByTopologicalOrder(List<List<String>> components,
                                                         Map<String, List<String>> graph,
                                                         Set<String> vertices) {

        // Соотносим вершины с их компонентами
        Map<String, Integer> componentMap = new HashMap<>();
        for (int i = 0; i < components.size(); i++)
            for (String vertex : components.get(i))
                componentMap.put(vertex, i);

        // Строим граф между компонентами
        Map<Integer, List<Integer>> componentGraph = new HashMap<>();
        Map<Integer, Integer> inDegree = new HashMap<>();
        for (int i = 0; i < components.size(); i++) {
            componentGraph.put(i, new ArrayList<>());
            inDegree.put(i, 0);
        }

        // Добавляем рёбра между компонентами
        for (String from : vertices)
            for (String to : graph.get(from)) {
                int fromComp = componentMap.get(from);
                int toComp = componentMap.get(to);
                if (fromComp != toComp && !componentGraph.get(fromComp).contains(toComp)) {
                    componentGraph.get(fromComp).add(toComp);
                    inDegree.put(toComp, inDegree.get(toComp) + 1);
                }
            }

        // Топологическая сортировка компонент
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int i = 0; i < components.size(); i++)
            if (inDegree.get(i) == 0)
                queue.offer(i);

        List<Integer> topologicalOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int comp = queue.poll();
            topologicalOrder.add(comp);
            for (int neighbor : componentGraph.get(comp)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0)
                    queue.offer(neighbor);
            }
        }

        // Переупорядочиваем компоненты
        List<List<String>> sortedComponents = new ArrayList<>();
        for (int index : topologicalOrder)
            sortedComponents.add(components.get(index));

        components.clear();
        components.addAll(sortedComponents);
    }
}
