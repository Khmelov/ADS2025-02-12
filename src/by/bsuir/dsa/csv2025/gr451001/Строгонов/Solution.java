package by.bsuir.dsa.csv2025.gr451001.Строгонов;

import java.util.*;

public class Solution {
    public static void dfs(Map<Long, List<Long>> adj, Set<Long> vis, long u) {
        if (vis.contains(u)) return;
        vis.add(u);
        if (adj.containsKey(u)) {
            for (long v : adj.get(u)) {
                dfs(adj, vis, v);
            }
        }
    }

    public static void solve() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long[] A = new long[n];
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextLong();
        }
        
        Map<Long, List<Long>> adj = new HashMap<>();
        for (int i = 1; i < n; i++) {
            long u = A[i] + i;
            long v = u - i;  // Обратите внимание на изменение знака!
            if (!adj.containsKey(u)) {
                adj.put(u, new ArrayList<>());
            }
            adj.get(u).add(v);
        }
        
        Set<Long> vis = new HashSet<>();
        dfs(adj, vis, n);
        
        long max = Long.MIN_VALUE;
        for (long x : vis) {
            if (x > max) max = x;
        }
        System.out.println(max);
        scanner.close();
    }

    public static void main(String[] args) {
        solve();
    }
}