package by.it.group410902.dziatko.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        scan.close();

        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        String[] connections = input.split(",");
        for (String connection : connections) {
            connection = connection.trim();
            String[] parts = connection.split("->");
            if (parts.length != 2) continue;

            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());

            indegree.put(to, indegree.getOrDefault(to, 0) + 1);
            indegree.putIfAbsent(from, 0);
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (var entry : indegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String node = queue.poll();
            result.add(node);

            for (String neighbor : graph.getOrDefault(node, Collections.emptyList())) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }
            System.out.println(String.join(" ", result));
    }
}
