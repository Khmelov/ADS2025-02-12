package by.it.group410901.kliaus.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        // Парсинг входных данных
        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            allNodes.add(from);
            allNodes.add(to);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            inDegree.putIfAbsent(from, 0);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }

        // Топологическая сортировка (алгоритм Кана)
        Queue<String> queue = new LinkedList<>();
        List<String> result = new ArrayList<>();

        // Добавляем вершины с нулевой входящей степенью
        for (String node : allNodes) {
            if (inDegree.getOrDefault(node, 0) == 0) {
                queue.add(node);
            }
        }

        // Сортируем начальные вершины для детерминированного порядка
        List<String> sortedQueue = new ArrayList<>(queue);
        Collections.sort(sortedQueue);
        queue.clear();
        queue.addAll(sortedQueue);

        while (!queue.isEmpty()) {
            String node = queue.poll();
            result.add(node);

            if (graph.containsKey(node)) {
                List<String> neighbors = new ArrayList<>(graph.get(node));
                Collections.sort(neighbors);

                for (String neighbor : neighbors) {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    if (inDegree.get(neighbor) == 0) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Вывод результата
        System.out.println(String.join(" ", result));
    }
}