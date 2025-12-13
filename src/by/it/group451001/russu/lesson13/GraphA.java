package by.it.group451001.russu.lesson13;
import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        sc.close();

        Map<String, List<String>> graph = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        String[] edges = input.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            vertices.add(from);
            vertices.add(to);
        }

        Map<String, Integer> indegree = new HashMap<>();
        for (String v : vertices) indegree.put(v, 0);
        for (List<String> adj : graph.values()) {
            for (String to : adj) {
                indegree.put(to, indegree.get(to) + 1);
            }
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : vertices) {
            if (indegree.get(v) == 0) {
                pq.add(v);
            }
        }

        List<String> topo = new ArrayList<>();
        while (!pq.isEmpty()) {
            String v = pq.poll();
            topo.add(v);
            if (graph.containsKey(v)) {
                for (String to : graph.get(v)) {
                    indegree.put(to, indegree.get(to) - 1);
                    if (indegree.get(to) == 0) {
                        pq.add(to);
                    }
                }
            }
        }

        for (int i = 0; i < topo.size(); i++) {
            System.out.print(topo.get(i));
            if (i < topo.size() - 1) System.out.print(" ");
        }
    }
}
