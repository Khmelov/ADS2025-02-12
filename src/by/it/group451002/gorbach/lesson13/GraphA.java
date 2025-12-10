package by.it.group451002.gorbach.lesson13;

import java.util.*;

public class GraphA {

    private Map<String, List<String>> graph;
    private Map<String, Integer> inDegree;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        GraphA graphA = new GraphA();
        graphA.parseInput(input);
        List<String> result = graphA.topologicalSort();

        System.out.println(String.join(" ", result));
    }

    public void parseInput(String input) {
        graph = new HashMap<>();
        inDegree = new HashMap<>();

        String[] edges = input.split(", ");

        for (String edge : edges) {
            String[] parts = edge.split(" -> ");
            String from = parts[0];
            String to = parts[1];

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.putIfAbsent(to, new ArrayList<>());

            inDegree.putIfAbsent(from, 0);
            inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
        }
    }

    public List<String> topologicalSort() {
        List<String> result = new ArrayList<>();

        PriorityQueue<String> queue = new PriorityQueue<>();

        for (String vertex : inDegree.keySet()) {
            if (inDegree.get(vertex) == 0) {
                queue.offer(vertex);
            }
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            for (String neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        return result;
    }
}