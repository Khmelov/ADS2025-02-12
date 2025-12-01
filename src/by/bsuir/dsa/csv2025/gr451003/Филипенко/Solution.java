package by.bsuir.dsa.csv2025.gr451003.Филипенко;

import java.util.*;

public class Solution {
    static class Edge {
        int to, weight;
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();

        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            int w = sc.nextInt();
            graph.get(u).add(new Edge(v, w));
        }

        // старт и конец
        int s = sc.nextInt();
        int t = sc.nextInt();

        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[s] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{s, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0];
            int d = cur[1];

            if (d > dist[u]) continue;

            for (Edge e : graph.get(u)) {
                if (dist[e.to] > dist[u] + e.weight) {
                    dist[e.to] = dist[u] + e.weight;
                    pq.add(new int[]{e.to, dist[e.to]});
                }
            }
        }

        if (dist[t] == Integer.MAX_VALUE) {
            System.out.println(-1);
        } else {
            System.out.println(dist[t]);
        }
    }
}