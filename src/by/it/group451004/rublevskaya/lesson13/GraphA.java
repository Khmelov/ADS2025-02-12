package by.it.group451004.rublevskaya.lesson13;
import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        scanner.close();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> allNodes = new HashSet<>();

        if (!input.isEmpty()) {
            String[] edges = input.split(",\\s*");
            for (String edge : edges) {
                String[] parts = edge.split("->");
                if (parts.length != 2) continue;
                String from = parts[0].trim();
                String to = parts[1].trim();

                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                allNodes.add(from);
                allNodes.add(to);
            }
        }

        // Include isolated nodes
        for (String node : allNodes) {
            graph.putIfAbsent(node, new ArrayList<>());
        }

        Map<String, Integer> inDegree = new HashMap<>();
        for (String node : allNodes) {
            inDegree.put(node, 0);
        }

        for (String from : graph.keySet()) {
            for (String to : graph.get(from)) {
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String node : allNodes) {
            if (inDegree.get(node) == 0) {
                queue.add(node);
            }
        }

        List<String> topo = new ArrayList<>();
        while (!queue.isEmpty()) {
            String node = queue.poll();
            topo.add(node);

            for (String neighbor : graph.get(node)) {
                int newDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newDegree);
                if (newDegree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        for (int i = 0; i < topo.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(topo.get(i));
        }
        System.out.println();
    }
}