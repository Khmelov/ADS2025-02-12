package by.it.group451004.maximenko.lesson13;
import java.util.*;

public class GraphA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        sc.close();
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        String[] parts = input.split(",");

        for (String part : parts) {
            part = part.trim();
            String[] lr = part.split("->");
            if (lr.length != 2) continue;

            String from = lr[0].trim();
            String to = lr[1].trim();

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            indegree.putIfAbsent(from, 0);
            indegree.putIfAbsent(to, 0);

            graph.get(from).add(to);

            indegree.put(to, indegree.get(to) + 1);
        }

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : indegree.keySet()) {
            if (indegree.get(v) == 0) {
                pq.add(v);
            }
        }

        List<String> topo = new ArrayList<>();

        while (!pq.isEmpty()) {
            String v = pq.poll();
            topo.add(v);

            for (String nei : graph.get(v)) {
                indegree.put(nei, indegree.get(nei) - 1);
                if (indegree.get(nei) == 0) {
                    pq.add(nei);
                }
            }
        }

        System.out.println(String.join(" ", topo));
    }
}

