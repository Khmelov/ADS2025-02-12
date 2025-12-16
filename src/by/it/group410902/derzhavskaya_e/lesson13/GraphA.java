package by.it.group410902.derzhavskaya_e.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String line = sc.nextLine().trim();
        String[] parts = line.split(",");

        // Граф: строка -> список строк
        Map<String, List<String>> graph = new HashMap<>();
        // Входная степень каждой вершины
        Map<String, Integer> indegree = new HashMap<>();

        for (String p : parts) {
            String part = p.trim();            // "A -> B"
            String[] lr = part.split("->");

            String from = lr[0].trim();
            String to   = lr[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to,   new ArrayList<>());

            graph.get(from).add(to);

            indegree.putIfAbsent(from, 0);
            indegree.put(to, indegree.getOrDefault(to, 0) + 1);
        }

        // Лексикографическая очередь
        PriorityQueue<String> q = new PriorityQueue<>();

        for (String v : graph.keySet()) {
            if (indegree.getOrDefault(v, 0) == 0)
                q.add(v);
        }

        List<String> result = new ArrayList<>();

        while (!q.isEmpty()) {
            String v = q.poll();
            result.add(v);

            for (String nx : graph.get(v)) {
                indegree.put(nx, indegree.get(nx) - 1);
                if (indegree.get(nx) == 0)
                    q.add(nx);
            }
        }

        // Вывод в одну строку с пробелом
        for (String s : result) {
            System.out.print(s + " ");
        }
    }
}
