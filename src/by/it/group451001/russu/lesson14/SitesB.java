package by.it.group451001.russu.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        int[] parent;
        int[] size;

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

        Map<String, Integer> idMap = new HashMap<>();
        List<String[]> edges = new ArrayList<>();

        int idx = 0;
        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            String[] parts = line.split("\\+");
            String a = parts[0].trim();
            String b = parts[1].trim();

            if (!idMap.containsKey(a)) {
                idMap.put(a, idx++);
            }
            if (!idMap.containsKey(b)) {
                idMap.put(b, idx++);
            }
            edges.add(new String[]{a, b});
        }

        DSU dsu = new DSU(idx);

        for (String[] e : edges) {
            int u = idMap.get(e[0]);
            int v = idMap.get(e[1]);
            dsu.union(u, v);
        }


        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < idx; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, dsu.size[root]);
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        sizes.sort(Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i + 1 < sizes.size()) System.out.print(" ");
        }
    }
}
