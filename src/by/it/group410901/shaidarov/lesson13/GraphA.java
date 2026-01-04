package by.it.group410901.shaidarov.lesson13;

import java.util.*;

public class GraphA {
    private Map<String, List<String>> adjacencyList;
    private Set<String> allVertices;

    public GraphA() {
        adjacencyList = new HashMap<>();
        allVertices = new HashSet<>();
    }

    // Добавление ребра в граф
    public void addEdge(String from, String to) {
        adjacencyList.putIfAbsent(from, new ArrayList<>());
        adjacencyList.get(from).add(to);
        allVertices.add(from);
        allVertices.add(to);
    }








    // Топологическая сортировка (алгоритм Кана)
    public List<String> topologicalSort() {
        // Подсчет входящих степеней для каждой вершины
        Map<String, Integer> inDegree = new HashMap<>();
        for (String vertex : allVertices) {
            inDegree.put(vertex, 0);
        }

        for (String vertex : adjacencyList.keySet()) {
            for (String neighbor : adjacencyList.get(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        // PriorityQueue для лексикографического порядка
        PriorityQueue<String> queue = new PriorityQueue<>();

        // Добавляем вершины с нулевой входящей степенью
        for (String vertex : allVertices) {
            if (inDegree.get(vertex) == 0) {
                queue.offer(vertex);
            }
        }

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Уменьшаем входящую степень соседей
            if (adjacencyList.containsKey(current)) {
                for (String neighbor : adjacencyList.get(current)) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        // Проверка на наличие цикла
        if (result.size() != allVertices.size()) {
            throw new RuntimeException("Граф содержит цикл!");
        }

        return result;
    }

    // Парсинг строки и построение графа
    public void parseGraph(String input) {
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            if (parts.length == 2) {
                String from = parts[0].trim();
                String to = parts[1].trim();
                addEdge(from, to);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Считываем строку структуры орграфа
        String input = scanner.nextLine();

        GraphA graph = new GraphA();
        graph.parseGraph(input);

        // Выполняем топологическую сортировку
        List<String> sorted = graph.topologicalSort();

        // Выводим результат без пробелов в конце
        for (int i = 0; i < sorted.size(); i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(sorted.get(i));
        }
        System.out.println();

        scanner.close();
    }
}
