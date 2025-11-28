package by.it.group410902.bobrovskaya.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] edges = scanner.nextLine().split(",");

        Map<String, List<String>> graph = new HashMap<>(); // вершина, список вершин, в которые ведут рёбра из этой вершины
        Map<String, Integer> edgesNumber = new HashMap<>(); // вершина, кол-во входящих ребер в нее

        // построение графа
        for (String edge : edges) {
            String[] parts = edge.trim().split("->");
            String from = parts[0].trim(); // начальная вершина
            String to = parts[1].trim();   // конечная вершина

            // проверка наличия вершины в карте (создание списка для to(список смежных вершин))
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            // проверка, добавлена ли в InDegree
            edgesNumber.putIfAbsent(from, 0);
            // увеличиваем входящую степень вершины 
            edgesNumber.put(to, edgesNumber.getOrDefault(to, 0) + 1);
        }
        PriorityQueue<String> queue = new PriorityQueue<>(); // для сортировки
        for (String node : edgesNumber.keySet()) {
            if (edgesNumber.get(node) == 0) {
                queue.add(node);
            }
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            for (String neighbor : graph.getOrDefault(current, Collections.emptyList())) {
                // "удадение вершины current" из числа входящих
                edgesNumber.put(neighbor, edgesNumber.get(neighbor) - 1);
                // кол-во вход вершин = 0
                if (edgesNumber.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }
        System.out.println(String.join(" ", result));
    }
}
