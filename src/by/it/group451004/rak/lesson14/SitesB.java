package by.it.group451004.rak.lesson14;

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
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;

            if (size[a] < size[b]) {
                parent[a] = b;
                size[b] += size[a];
            } else {
                parent[b] = a;
                size[a] += size[b];
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<String, Integer> id = new HashMap<>();
        List<int[]> edges = new ArrayList<>();

        int counter = 0;

        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;

            String[] parts = line.split("\\+");
            String A = parts[0];
            String B = parts[1];

            if (!id.containsKey(A))
                id.put(A, counter++);
            if (!id.containsKey(B))
                id.put(B, counter++);

            edges.add(new int[]{ id.get(A), id.get(B) });
        }

        DSU dsu = new DSU(counter);

        for (int[] e : edges)
            dsu.union(e[0], e[1]);

        Map<Integer, Integer> cnt = new HashMap<>();
        for (int i = 0; i < counter; i++) {
            int root = dsu.find(i);
            cnt.put(root, cnt.getOrDefault(root, 0) + 1);
        }

        List<Integer> res = new ArrayList<>(cnt.values());
        res.sort(Collections.reverseOrder());

        for (int x : res)
            System.out.print(x + " ");
    }
}
