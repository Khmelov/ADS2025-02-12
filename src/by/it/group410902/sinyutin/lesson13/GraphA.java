package by.it.group410902.sinyutin.lesson13;
import java.util.*;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String input = scanner.nextLine();

        // Структуры данных используют String вместо Integer
        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        // Парсинг
        String[] edges = input.split(", ");
        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String u = parts[0];
            String v = parts[1];

            allNodes.add(u);
            allNodes.add(v);

            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
        }

        // Инициализация входящих степеней
        for (String node : allNodes) {
            inDegree.putIfAbsent(node, 0);
            if (adj.containsKey(node)) {
                for (String neighbor : adj.get(node)) {
                    inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
                }
            }
        }

        // Очередь с приоритетом для лексикографической сортировки (String реализует Comparable)
        PriorityQueue<String> pq = new PriorityQueue<>();

        // Добавляем истоки (вершины со степенью входа 0)
        for (String node : allNodes) {
            if (inDegree.get(node) == 0) {
                pq.offer(node);
            }
        }

        List<String> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            String u = pq.poll();
            result.add(u);

            if (adj.containsKey(u)) {
                for (String v : adj.get(u)) {
                    inDegree.put(v, inDegree.get(v) - 1);
                    if (inDegree.get(v) == 0) {
                        pq.offer(v);
                    }
                }
            }
        }

        // Вывод
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}