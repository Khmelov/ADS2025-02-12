package by.it.group410902.sinyutin.lesson14;

import java.util.*;

public class SitesB {

    // ---------- DSU ----------
    static class DSU {
        int[] parent;
        int[] rank;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;

            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else if (rank[rb] < rank[ra]) {
                parent[rb] = ra;
                size[ra] += size[rb];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
                rank[ra]++;
            }
        }
    }

    // main
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<String, Integer> id = new HashMap<>();
        List<int[]> pairs = new ArrayList<>();
        int counter = 0;

        while (true) {
            String s = sc.nextLine().trim();
            if (s.equals("end")) break;

            String[] ab = s.split("\\+");
            String a = ab[0];
            String b = ab[1];

            if (!id.containsKey(a)) id.put(a, counter++);
            if (!id.containsKey(b)) id.put(b, counter++);

            pairs.add(new int[]{id.get(a), id.get(b)});
        }

        DSU dsu = new DSU(counter);

        for (int[] p : pairs)
            dsu.union(p[0], p[1]);

        Map<Integer, Integer> clusters = new HashMap<>();

        for (int i = 0; i < counter; i++) {
            int root = dsu.find(i);
            clusters.put(root, dsu.size[root]);
        }

        List<Integer> result = new ArrayList<>(clusters.values());
        result.sort((a, b) -> b - a);

        for (int i = 0; i < result.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result.get(i));
        }
    }
}
