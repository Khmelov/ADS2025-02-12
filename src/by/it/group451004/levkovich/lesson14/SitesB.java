package by.it.group451004.levkovich.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        private int[] parent;
        private int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;
            if (size[a] < size[b]) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            parent[b] = a;
            size[a] += size[b];
        }

        int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<String, Integer> siteToId = new HashMap<>();
        List<String[]> edges = new ArrayList<>();

        int idCounter = 0;

        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;

            String[] parts = line.split("\\+");
            if (parts.length != 2) continue;

            String a = parts[0];
            String b = parts[1];

            if (!siteToId.containsKey(a)) {
                siteToId.put(a, idCounter++);
            }
            if (!siteToId.containsKey(b)) {
                siteToId.put(b, idCounter++);
            }

            edges.add(new String[]{a, b});
        }

        int n = siteToId.size();
        DSU dsu = new DSU(n);

        for (String[] e : edges) {
            int u = siteToId.get(e[0]);
            int v = siteToId.get(e[1]);
            dsu.union(u, v);
        }

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.size[root]);
        }

        List<Integer> result = new ArrayList<>(clusterSizes.values());
        Collections.sort(result, Collections.reverseOrder());

        for (int size : result) {
            System.out.print(size + " ");
        }
    }

}