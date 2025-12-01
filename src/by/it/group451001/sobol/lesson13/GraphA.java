package by.it.group451001.sobol.lesson13;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GraphA {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();

        Map<String, List<String>> adj = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        String[] parts = line.split(",");
        for (String raw : parts) {
            String token = raw.trim();
            if (token.isEmpty()) continue;

            if (token.contains("->")) {
                String[] arrow = token.split("->", 2);
                String src = arrow[0].trim();
                String dst = arrow[1].trim();

                adj.putIfAbsent(src, new ArrayList<>());
                indegree.putIfAbsent(src, 0);

                adj.putIfAbsent(dst, new ArrayList<>());
                indegree.putIfAbsent(dst, 0);

                adj.get(src).add(dst);
                indegree.put(dst, indegree.get(dst) + 1);
            } else {
                String v = token.trim();
                adj.putIfAbsent(v, new ArrayList<>());
                indegree.putIfAbsent(v, 0);
            }
        }

        for (List<String> list : adj.values()) Collections.sort(list);
        List<String> vs = new ArrayList<>(indegree.keySet());
        Collections.sort(vs);

        PriorityQueue<String> pq = new PriorityQueue<>();
        for (String v : vs) if (indegree.get(v) == 0) pq.add(v);

        List<String> topo = new ArrayList<>();
        while (!pq.isEmpty()) {
            String u = pq.poll();
            topo.add(u);
            for (String nb : adj.getOrDefault(u, Collections.emptyList())) {
                indegree.put(nb, indegree.get(nb) - 1);
                if (indegree.get(nb) == 0) pq.add(nb);
            }
        }

        System.out.println(String.join(" ", topo));
    }
}