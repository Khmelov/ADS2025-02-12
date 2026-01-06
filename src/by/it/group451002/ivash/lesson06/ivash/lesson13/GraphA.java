package by.it.group451002.ivash.lesson06.ivash.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        sc.close();

        // Создаем граф
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> nodes = new HashSet<>();

        // Разбираем входную строку
        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("\\s*->\\s*");
            String from = parts[0];
            String to = parts[1];

            nodes.add(from);
            nodes.add(to);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
            inDegree.putIfAbsent(from, inDegree.getOrDefault(from, 0));
        }

        // Лексикографически упорядоченная очередь для вершин с нулевой степенью
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String node : nodes) {
            if (inDegree.getOrDefault(node, 0) == 0) {
                queue.add(node);
            }
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String node = queue.poll();
            result.add(node);
            for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Вывод топологической сортировки
        System.out.println(String.join(" ", result));
    }
}
