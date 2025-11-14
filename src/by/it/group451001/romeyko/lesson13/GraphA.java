package by.it.group451001.romeyko.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().trim();


        Map<String, List<String>> graph = new TreeMap<>();
        Map<String, Integer> indegree = new TreeMap<>();
        String[] parts = line.split(",");

        for (String s : parts) {
            s = s.trim();
            String[] lr = s.split("->");
            String from = lr[0].trim();
            String to = lr[1].trim();

            // Добавляем from
            graph.putIfAbsent(from, new ArrayList<>());
            graph.get(from).add(to);

            // Добавляем to, если нет
            graph.putIfAbsent(to, new ArrayList<>());

            // Обработаем входящие степени
            indegree.putIfAbsent(from, 0);
            indegree.put(to, indegree.getOrDefault(to, 0) + 1);
        }

        // Очередь с приоритетом: вершины выбираются в лексикографическом порядке
        PriorityQueue<String> pq = new PriorityQueue<>();

        for (String v : graph.keySet()) {
            if (indegree.getOrDefault(v, 0) == 0) {
                pq.add(v);
            }
        }

        List<String> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            String v = pq.poll();
            result.add(v);

            for (String neigh : graph.get(v)) {
                indegree.put(neigh, indegree.get(neigh) - 1);
                if (indegree.get(neigh) == 0) {
                    pq.add(neigh);
                }
            }
        }

        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i + 1 < result.size()) System.out.print(" ");
        }
    }
}
