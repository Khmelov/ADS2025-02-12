package by.it.group451003.platonova.lesson14;

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
                parent[x] = find(parent[x]);  // path compression
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;

            // union by size
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
            if (line.equals("end"))
                break;

            String[] parts = line.split("\\+");
            String a = parts[0];
            String b = parts[1];

            id.putIfAbsent(a, counter++);
            id.putIfAbsent(b, counter++);

            edges.add(new int[]{id.get(a), id.get(b)});
        }

        DSU dsu = new DSU(counter);

        for (int[] e : edges) {
            dsu.union(e[0], e[1]);
        }

        // root -> actual cluster size
        Map<Integer, Integer> clusters = new HashMap<>();
        for (int i = 0; i < counter; i++) {
            int root = dsu.find(i);
            clusters.put(root, clusters.getOrDefault(root, 0) + 1);
        }

        List<Integer> result = new ArrayList<>(clusters.values());

        // сортировка по убыванию
        result.sort(Collections.reverseOrder());

        // вывод
        for (int x : result)
            System.out.print(x + " ");
    }
}
