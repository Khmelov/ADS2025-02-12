package by.it.group451001.romeyko.lesson14;

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
                parent[x] = find(parent[x]); // path compression
            return parent[x];
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;

            if (size[ra] < size[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
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
            String a = parts[0];
            String b = parts[1];

            if (!id.containsKey(a)) id.put(a, counter++);
            if (!id.containsKey(b)) id.put(b, counter++);

            edges.add(new int[]{id.get(a), id.get(b)});
        }

        DSU dsu = new DSU(counter);
        for (int[] e : edges) {
            dsu.union(e[0], e[1]);
        }

        Map<Integer, Integer> comp = new HashMap<>();
        for (int i = 0; i < counter; i++) {
            int r = dsu.find(i);
            comp.put(r, comp.getOrDefault(r, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(comp.values());
        Collections.sort(sizes, Collections.reverseOrder());

        for (int s : sizes) {
            System.out.print(s + " ");
        }
    }
}
